package com.rohg007.android.huddle01androiddemoapp.viewmodels;

import android.app.Application;

import com.rohg007.android.huddle01_android_sdk.ConnectionState;
import com.rohg007.android.huddle01_android_sdk.models.HuddleProducer;
import com.rohg007.android.huddle01_android_sdk.models.Me;
import com.rohg007.android.huddle01_android_sdk.models.Producers;
import com.rohg007.android.huddle01androiddemoapp.state.StateStore;

import org.webrtc.AudioTrack;
import org.webrtc.VideoTrack;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LifecycleOwner;

public class MeProps extends PeerViewProps {

    public enum DeviceState {
        UNSUPPORTED,
        ON,
        OFF
    }

    private final ObservableField<Boolean> mConnected;
    private final ObservableField<Me> mMe;
    private final ObservableField<DeviceState> mMicState;
    private final ObservableField<DeviceState> mCamState;
    private final ObservableField<DeviceState> mChangeCamState;
    private final StateComposer mStateComposer;

    public MeProps(@NonNull Application application, @NonNull StateStore roomStore) {
        super(application, roomStore);
        setMe(true);
        mConnected = new ObservableField<>(Boolean.FALSE);
        mMe = new ObservableField<>();
        mMicState = new ObservableField<>(DeviceState.UNSUPPORTED);
        mCamState = new ObservableField<>(DeviceState.UNSUPPORTED);
        mChangeCamState = new ObservableField<>(DeviceState.UNSUPPORTED);
        mStateComposer = new StateComposer();
        mStateComposer.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable sender, int propertyId) {
                        Me me = mStateComposer.mMe;
                        Producers.ProducersWrapper audioPW = mStateComposer.mAudioPW;
                        HuddleProducer audioProducer = audioPW != null ? audioPW.getProducer() : null;
                        Producers.ProducersWrapper videoPW = mStateComposer.mVideoPW;
                        HuddleProducer videoProducer = videoPW != null ? videoPW.getProducer() : null;

                        mAudioProducerId.set(audioProducer != null ? audioProducer.getId() : null);
                        mVideoProducerId.set(videoProducer != null ? videoProducer.getId() : null);
                        mAudioRtpParameters.set(
                                audioProducer != null ? audioProducer.getRtpParameters() : null);
                        mVideoRtpParameters.set(
                                videoProducer != null ? videoProducer.getRtpParameters() : null);
                        mAudioTrack.set(audioProducer != null ? (AudioTrack) audioProducer.getTrack() : null);
                        mVideoTrack.set(videoProducer != null ? (VideoTrack) videoProducer.getTrack() : null);
                        mAudioScore.set(audioPW != null ? audioPW.getScore() : null);
                        mVideoScore.set(videoPW != null ? videoPW.getScore() : null);
                        DeviceState micState;
                        if (me == null || !me.isCanSendMic()) {
                            micState = DeviceState.UNSUPPORTED;
                        } else if (audioProducer == null) {
                            micState = DeviceState.UNSUPPORTED;
                        } else if (!audioProducer.getPaused()) {
                            micState = DeviceState.ON;
                        } else {
                            micState = DeviceState.OFF;
                        }
                        mMicState.set(micState);

                        DeviceState camState;
                        if (me == null || !me.isCanSendMic()) {
                            camState = DeviceState.UNSUPPORTED;
                        } else if (videoPW != null
                                && !Producers.ProducersWrapper.TYPE_SHARE.equals(videoPW.getType())) {
                            camState = DeviceState.ON;
                        } else {
                            camState = DeviceState.OFF;
                        }
                        mCamState.set(camState);

                        DeviceState changeCamState;
                        if (me == null) {
                            changeCamState = DeviceState.UNSUPPORTED;
                        } else if (videoPW != null
                                && !Producers.ProducersWrapper.TYPE_SHARE.equals(videoPW.getType())
                                && me.isCanChangeCam()) {
                            changeCamState = DeviceState.ON;
                        } else {
                            changeCamState = DeviceState.OFF;
                        }
                        mChangeCamState.set(changeCamState);
                    }
                });
    }

    public ObservableField<Boolean> getConnected() {
        return mConnected;
    }

    public ObservableField<Me> getMe() {
        return mMe;
    }

    public ObservableField<DeviceState> getMicState() {
        return mMicState;
    }

    public ObservableField<DeviceState> getCamState() {
        return mCamState;
    }

    @Override
    public void connect(LifecycleOwner owner) {
        getRoomStore().getMe().observe(owner, me -> {
            mMe.set(me);
            mPeer.set(me);
        });
        getRoomStore().getRoomInfo().observe(owner, roomInfo -> {
            mConnected.set(ConnectionState.CONNECTED.equals(roomInfo.getConnectionState()));
        });
        mStateComposer.connect(owner, getRoomStore());
    }

    public static class StateComposer extends BaseObservable {

        private Producers.ProducersWrapper mAudioPW;
        private Producers.ProducersWrapper mVideoPW;
        private Me mMe;

        void connect(@NonNull LifecycleOwner owner, StateStore store) {
            store.getProducers().observe(owner, (producers) -> {
                mAudioPW = producers.filter("audio");
                mVideoPW = producers.filter("video");
                notifyChange();
            });
            store.getMe().observe(owner, (me) -> {
                mMe = me;
                notifyChange();
            });
        }
    }
}
