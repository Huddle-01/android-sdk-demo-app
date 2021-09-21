package com.rohg007.android.huddle01androiddemoapp.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.rohg007.android.huddle01_android_sdk.Utils.PeerConnectionUtils;
import com.rohg007.android.huddle01androiddemoapp.R;
import com.rohg007.android.huddle01androiddemoapp.viewmodels.PeerProps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

public class PeerView extends RelativeLayout {

    public PeerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PeerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PeerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PeerView(
            @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    com.rohg007.android.huddle01androiddemoapp.databinding.ViewPeerBinding mBinding;

    private void init(Context context) {
        mBinding =
                DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_peer, this, true);
        mBinding.peerView.videoRenderer.init(PeerConnectionUtils.getEglContext(), null);
    }

    public void setProps(PeerProps props) {
        // set view model into included layout
        mBinding.peerView.setPeerViewProps(props);
        // set view model
        mBinding.setPeerProps(props);
    }
}
