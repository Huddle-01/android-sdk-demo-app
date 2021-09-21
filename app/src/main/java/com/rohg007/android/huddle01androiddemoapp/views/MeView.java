package com.rohg007.android.huddle01androiddemoapp.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;

import com.rohg007.android.huddle01_android_sdk.HuddleClient;
import com.rohg007.android.huddle01_android_sdk.Utils.PeerConnectionUtils;
import com.rohg007.android.huddle01androiddemoapp.R;
import com.rohg007.android.huddle01androiddemoapp.viewmodels.MeProps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

public class MeView extends RelativeLayout {

    public MeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MeView(
            @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    com.rohg007.android.huddle01androiddemoapp.databinding.ViewMeBinding mBinding;

    private void init(Context context) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_me, this, true);
        mBinding.peerView.videoRenderer.init(PeerConnectionUtils.getEglContext(), null);
    }

    public void setProps(MeProps props, final HuddleClient huddleClient) {

        // set view model.
        mBinding.peerView.setPeerViewProps(props);

        mBinding.peerView.meDisplayName.setOnEditorActionListener(
                (textView, actionId, keyEvent) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        huddleClient.changeDisplayName(textView.getText().toString().trim());
                        return true;
                    }
                    return false;
                });

        mBinding.peerView.videoRenderer.setZOrderMediaOverlay(true);

        // set view model.
        mBinding.setMeProps(props);

        // register click listener.
        mBinding.mic.setOnClickListener(
                view -> {
                    if (MeProps.DeviceState.ON.equals(props.getMicState().get())) {
                        huddleClient.muteMic();
                    } else {
                        huddleClient.unmuteMic();
                    }
                });
        mBinding.cam.setOnClickListener(
                view -> {
                    if (MeProps.DeviceState.ON.equals(props.getCamState().get())) {
                        huddleClient.disableCam();
                    } else {
                        huddleClient.enableCam();
                    }
                });
        mBinding.changeCam.setOnClickListener(view -> huddleClient.changeCam());
    }
}