package com.rohg007.android.huddle01androiddemoapp.viewmodels;

import android.app.Application;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.rohg007.android.huddle01_android_sdk.ConnectionState;
import com.rohg007.android.huddle01_android_sdk.models.RoomInfo;
import com.rohg007.android.huddle01androiddemoapp.R;
import com.rohg007.android.huddle01androiddemoapp.state.StateStore;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LifecycleOwner;

public class RoomProps extends EdiasProps {

    private final Animation mConnectingAnimation;
    private final ObservableField<String> mInvitationLink;
    private final ObservableField<ConnectionState> mConnectionState;

    public RoomProps(@NonNull Application application, @NonNull StateStore roomStore) {
        super(application, roomStore);
        mConnectingAnimation = AnimationUtils.loadAnimation(getApplication(), R.anim.ani_connecting);
        mInvitationLink = new ObservableField<>();
        mConnectionState = new ObservableField<>();
    }

    public Animation getConnectingAnimation() {
        return mConnectingAnimation;
    }

    public ObservableField<String> getInvitationLink() {
        return mInvitationLink;
    }

    public ObservableField<ConnectionState> getConnectionState() {
        return mConnectionState;
    }

    private void receiveState(RoomInfo roomInfo) {
        mConnectionState.set(roomInfo.getConnectionState());
        mInvitationLink.set(roomInfo.getUrl());
    }

    @Override
    public void connect(LifecycleOwner owner) {
        StateStore roomStore = getRoomStore();
        roomStore.getRoomInfo().observe(owner, this::receiveState);
    }
}
