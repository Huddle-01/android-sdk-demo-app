package com.rohg007.android.huddle01androiddemoapp.viewmodels;

import android.app.Application;

import com.rohg007.android.huddle01_android_sdk.models.Info;
import com.rohg007.android.huddle01androiddemoapp.state.StateStore;

import org.json.JSONArray;
import org.webrtc.AudioTrack;
import org.webrtc.VideoTrack;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

public abstract class PeerViewProps extends EdiasProps {

    boolean mIsMe;
    ObservableField<Info> mPeer;
    ObservableField<String> mAudioProducerId;
    ObservableField<String> mVideoProducerId;
    ObservableField<String> mAudioConsumerId;
    ObservableField<String> mVideoConsumerId;
    ObservableField<String> mAudioRtpParameters;
    ObservableField<String> mVideoRtpParameters;
    ObservableField<AudioTrack> mAudioTrack;
    ObservableField<VideoTrack> mVideoTrack;
    ObservableField<Boolean> mAudioMuted;
    ObservableField<Boolean> mVideoVisible;
    ObservableField<JSONArray> mAudioScore;
    ObservableField<JSONArray> mVideoScore;

    public PeerViewProps(@NonNull Application application, @NonNull StateStore roomStore) {
        super(application, roomStore);
        mPeer = new ObservableField<>(new Info());
        mAudioProducerId = new ObservableField<>();
        mVideoProducerId = new ObservableField<>();
        mAudioConsumerId = new ObservableField<>();
        mVideoConsumerId = new ObservableField<>();
        mAudioRtpParameters = new ObservableField<>();
        mVideoRtpParameters = new ObservableField<>();
        mAudioTrack = new ObservableField<>();
        mVideoTrack = new ObservableField<>();
        mAudioMuted = new ObservableField<>(Boolean.FALSE);
        mVideoVisible = new ObservableField<>(Boolean.FALSE);
        mAudioScore = new ObservableField<>();
        mVideoScore = new ObservableField<>();
    }

    public void setMe(boolean me) {
        mIsMe = me;
    }

    public boolean isMe() {
        return mIsMe;
    }

    public ObservableField<Info> getPeer() {
        return mPeer;
    }

    public ObservableField<String> getAudioProducerId() {
        return mAudioProducerId;
    }

    public ObservableField<String> getVideoProducerId() {
        return mVideoProducerId;
    }

    public ObservableField<String> getAudioConsumerId() {
        return mAudioConsumerId;
    }

    public ObservableField<String> getVideoConsumerId() {
        return mVideoConsumerId;
    }

    public ObservableField<VideoTrack> getVideoTrack() {
        return mVideoTrack;
    }

    public ObservableField<Boolean> getVideoVisible() {
        return mVideoVisible;
    }
}
