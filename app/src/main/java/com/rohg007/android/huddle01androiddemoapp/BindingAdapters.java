package com.rohg007.android.huddle01androiddemoapp;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohg007.android.huddle01_android_sdk.ConnectionState;
import com.rohg007.android.huddle01_android_sdk.models.DeviceInfo;
import com.rohg007.android.huddle01androiddemoapp.viewmodels.MeProps;

import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    private static final String TAG = "BindingAdapters";

    @BindingAdapter({"bind:edias_state", "bind:edias_state_animation"})
    public static void roomState(
            ImageView view, ConnectionState state, Animation animation) {
        if (state == null) {
            return;
        }
        if (ConnectionState.CONNECTING.equals(state)) {
            view.setImageResource(com.rohg007.android.huddle01androiddemoapp.R.drawable.ic_state_connecting);
            view.startAnimation(animation);
        } else if (ConnectionState.CONNECTED.equals(state)) {
            view.setImageResource(com.rohg007.android.huddle01androiddemoapp.R.drawable.ic_state_connected);
            animation.cancel();
            view.clearAnimation();
        } else {
            view.setImageResource(com.rohg007.android.huddle01androiddemoapp.R.drawable.ic_state_new_close);
            animation.cancel();
            view.clearAnimation();
        }
    }

    @BindingAdapter({"bind:edias_link"})
    public static void inviteLink(TextView view, String inviteLink) {
        view.setVisibility(TextUtils.isEmpty(inviteLink) ? View.INVISIBLE : View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter({"bind:edias_device"})
    public static void deviceInfo(TextView view, DeviceInfo deviceInfo) {
        if (deviceInfo == null) {
            return;
        }

        int deviceIcon = com.rohg007.android.huddle01androiddemoapp.R.drawable.ic_unknown;
        if (!TextUtils.isEmpty(deviceInfo.getFlag())) {
            switch (deviceInfo.getFlag().toLowerCase()) {
                case "chrome":
                    deviceIcon = com.rohg007.android.huddle01androiddemoapp.R.mipmap.chrome;
                    break;
                case "firefox":
                    deviceIcon = com.rohg007.android.huddle01androiddemoapp.R.mipmap.firefox;
                    break;
                case "safari":
                    deviceIcon = com.rohg007.android.huddle01androiddemoapp.R.mipmap.safari;
                    break;
                case "opera":
                    deviceIcon = com.rohg007.android.huddle01androiddemoapp.R.mipmap.opera;
                    break;
                case "edge":
                    deviceIcon = com.rohg007.android.huddle01androiddemoapp.R.mipmap.edge;
                    break;
                case "android":
                    deviceIcon = com.rohg007.android.huddle01androiddemoapp.R.mipmap.android;
                    break;
            }
            view.setText(deviceInfo.getName() + " " + deviceInfo.getVersion());
        } else {
            view.setText("");
        }
        view.setCompoundDrawablesWithIntrinsicBounds(deviceIcon, 0, 0, 0);
    }

    @BindingAdapter({"edias_mic_state"})
    public static void deviceMicState(ImageView imageView, MeProps.DeviceState state) {
        if (state == null) {
            return;
        }
        Log.d(TAG, "edias_mic_state: " + state.name());
        if (MeProps.DeviceState.ON.equals(state)) {
            imageView.setBackgroundResource(com.rohg007.android.huddle01androiddemoapp.R.drawable.bg_media_box_on);
        } else {
            imageView.setBackgroundResource(com.rohg007.android.huddle01androiddemoapp.R.drawable.bg_media_box_off);
        }

        switch (state) {
            case ON:
                imageView.setImageResource(com.rohg007.android.huddle01androiddemoapp.R.drawable.icon_mic_black_on);
                break;
            case OFF:
                imageView.setImageResource(com.rohg007.android.huddle01androiddemoapp.R.drawable.icon_mic_white_off);
                break;
            case UNSUPPORTED:
                imageView.setImageResource(com.rohg007.android.huddle01androiddemoapp.R.drawable.icon_mic_white_unsupported);
                break;
        }
    }

    @BindingAdapter({"edias_cam_state"})
    public static void deviceCamState(ImageView imageView, MeProps.DeviceState state) {
        if (state == null) {
            return;
        }
        Log.d(TAG, "edias_cam_state: " + state.name());
        if (MeProps.DeviceState.ON.equals(state)) {
            imageView.setBackgroundResource(com.rohg007.android.huddle01androiddemoapp.R.drawable.bg_media_box_on);
        } else {
            imageView.setBackgroundResource(com.rohg007.android.huddle01androiddemoapp.R.drawable.bg_media_box_off);
        }

        switch (state) {
            case ON:
                imageView.setImageResource(com.rohg007.android.huddle01androiddemoapp.R.drawable.icon_webcam_black_on);
                break;
            case OFF:
                imageView.setImageResource(com.rohg007.android.huddle01androiddemoapp.R.drawable.icon_webcam_white_off);
                break;
            case UNSUPPORTED:
                imageView.setImageResource(com.rohg007.android.huddle01androiddemoapp.R.drawable.icon_webcam_white_unsupported);
                break;
        }
    }

    @BindingAdapter({"edias_change_came_state"})
    public static void changeCamState(View view, MeProps.DeviceState state) {
        if (state == null) {
            return;
        }
        Log.d(TAG, "edias_change_came_state: " + state.name());
        view.setEnabled(MeProps.DeviceState.ON.equals(state));
    }

    @BindingAdapter({"edias_share_state"})
    public static void shareState(View view, MeProps.DeviceState state) {
        if (state == null) {
            return;
        }
        Log.d(TAG, "edias_share_state: " + state.name());
        view.setEnabled(MeProps.DeviceState.ON.equals(state));
    }

    @BindingAdapter({"edias_render"})
    public static void render(SurfaceViewRenderer renderer, VideoTrack track) {
        Log.d(TAG, "edias_render: " + (track != null));
        if (track != null) {
            track.addSink(renderer);
            renderer.setVisibility(View.VISIBLE);
        } else {
            renderer.setVisibility(View.GONE);
        }
    }

    @BindingAdapter({"edias_render_empty"})
    public static void renderEmpty(View renderer, VideoTrack track) {
        Log.d(TAG, "edias_render_empty: " + (track != null));
        if (track == null) {
            renderer.setVisibility(View.VISIBLE);
        } else {
            renderer.setVisibility(View.GONE);
        }
    }
}
