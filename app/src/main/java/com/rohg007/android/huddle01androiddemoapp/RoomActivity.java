package com.rohg007.android.huddle01androiddemoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.rohg007.android.huddle01_android_sdk.ConnectionState;
import com.rohg007.android.huddle01_android_sdk.HuddleClient;
import com.rohg007.android.huddle01_android_sdk.MeListener;
import com.rohg007.android.huddle01_android_sdk.RoomEventListener;
import com.rohg007.android.huddle01_android_sdk.Utils.Constants;
import com.rohg007.android.huddle01_android_sdk.Utils.Reactions;
import com.rohg007.android.huddle01_android_sdk.Utils.Utils;
import com.rohg007.android.huddle01_android_sdk.models.DeviceInfo;
import com.rohg007.android.huddle01_android_sdk.models.HuddleConsumer;
import com.rohg007.android.huddle01_android_sdk.models.HuddleDataConsumer;
import com.rohg007.android.huddle01_android_sdk.models.HuddleDataProducer;
import com.rohg007.android.huddle01_android_sdk.models.HuddleProducer;
import com.rohg007.android.huddle01_android_sdk.models.Notify;
import com.rohg007.android.huddle01_android_sdk.models.Peer;
import com.rohg007.android.huddle01androiddemoapp.adapters.PeerAdapter;
import com.rohg007.android.huddle01androiddemoapp.databinding.ActivityRoomBinding;
import com.rohg007.android.huddle01androiddemoapp.state.StateStore;
import com.rohg007.android.huddle01androiddemoapp.utils.ClipboardCopy;
import com.rohg007.android.huddle01androiddemoapp.viewmodels.EdiasProps;
import com.rohg007.android.huddle01androiddemoapp.viewmodels.MeProps;
import com.rohg007.android.huddle01androiddemoapp.viewmodels.RoomProps;

import org.json.JSONObject;

import java.util.List;

public class RoomActivity extends AppCompatActivity {

    private static final String TAG = RoomActivity.class.getSimpleName();
    private static final int REQUEST_CODE_SETTING = 1;

    private String mRoomId, mPeerId, mDisplayName;

    private ActivityRoomBinding activityRoomBinding;
    private HuddleClient huddleClient;
    private StateStore stateStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRoomBinding = DataBindingUtil.setContentView(this, com.rohg007.android.huddle01androiddemoapp.R.layout.activity_room);
        stateStore = new StateStore();
        createRoom();
        checkPermission();
        setupReactions();
    }

    @SuppressLint("NonConstantResourceId")
    private void setupReactions(){
        activityRoomBinding.reactionFab.inflate(com.rohg007.android.huddle01androiddemoapp.R.menu.reactions_menu);
        activityRoomBinding.reactionFab.setOnActionSelectedListener(actionItem -> {
            switch (actionItem.getId()){
                case com.rohg007.android.huddle01androiddemoapp.R.id.tears_of_joy: {
                    huddleClient.sendReaction(Reactions.TEARS_OF_JOY);
                    activityRoomBinding.reactionFab.close();
                    return true;
                }
                case com.rohg007.android.huddle01androiddemoapp.R.id.crying_face: {
                    huddleClient.sendReaction(Reactions.CRYING_FACE);
                    activityRoomBinding.reactionFab.close();
                    return true;
                }
                case com.rohg007.android.huddle01androiddemoapp.R.id.hundred: {
                    huddleClient.sendReaction(Reactions.HUNDRED);
                    activityRoomBinding.reactionFab.close();
                    return true;
                }
                case com.rohg007.android.huddle01androiddemoapp.R.id.thumbs_up: {
                    huddleClient.sendReaction(Reactions.THUMBS_UP);
                    activityRoomBinding.reactionFab.close();
                    return true;
                }
                case com.rohg007.android.huddle01androiddemoapp.R.id.thumbs_down: {
                    huddleClient.sendReaction(Reactions.THUMBS_DOWN);
                    activityRoomBinding.reactionFab.close();
                    return true;
                }
                case com.rohg007.android.huddle01androiddemoapp.R.id.rocket: {
                    huddleClient.sendReaction(Reactions.ROCKET);
                    activityRoomBinding.reactionFab.close();
                    return true;
                }
                default: {
                    Toast.makeText(this, "invalid reaction", Toast.LENGTH_SHORT).show();
                    activityRoomBinding.reactionFab.close();
                    return false;
                }
            }
        });
    }

    private void createRoom(){
        loadRoomConfig();
        activityRoomBinding.invitationLink.setText("RoomId: "+mRoomId);
        huddleClient = new HuddleClient.Builder(getApplicationContext(), "i4pzqbpxza8vpijQMwZsP1H7nZZEH0TN3vR4NdNS")
                .setPeerId(mPeerId)
                .setRoomId(mRoomId)
                .setDisplayName(mDisplayName)
                .setCanConsume(true)
                .setCanProduce(true)
                .setCanUseDataChannel(true)
                .setRoomEventListener(listener)
                .setMeListener(meListener)
                .setFrontCamEnabledOnInit(true)
                .build();

        getViewModelStore().clear();
        initViewModel();
    }

    void joinRoom(){
        huddleClient.joinRoom();
    }

    private void loadRoomConfig(){
        Intent intent = getIntent();
        String roomId = intent.getStringExtra("roomId");
        if(roomId!=null){
            mRoomId = roomId;
        } else {
            mRoomId = Utils.getRandomString(8);
        }
        mPeerId = Utils.getRandomString(8);
        mDisplayName = Utils.getRandomString(8);
    }


    private void initViewModel(){
        EdiasProps.Factory factory = new EdiasProps.Factory(getApplication(), stateStore);
        RoomProps roomProps = new ViewModelProvider(this, factory).get(RoomProps.class);
        roomProps.connect(this);
        activityRoomBinding.invitationLink.setOnClickListener(v -> {
            String invitationLink = roomProps.getInvitationLink().get();
            ClipboardCopy.clipboardCopy(getApplication(), invitationLink, com.rohg007.android.huddle01androiddemoapp.R.string.invite_link_copied);
        });
        activityRoomBinding.setRoomProps(roomProps);

        MeProps meProps = new ViewModelProvider(this, factory).get(MeProps.class);
        meProps.connect(this);
        activityRoomBinding.me.setProps(meProps, huddleClient);

        activityRoomBinding.chatInput.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                huddleClient.sendChatMessage(v.getText().toString());
                v.setText("");
                return true;
            } else
                return false;
        });

        PeerAdapter peerAdapter = new PeerAdapter(stateStore, this);
        activityRoomBinding.remotePeers.setLayoutManager(new LinearLayoutManager(this));
        activityRoomBinding.remotePeers.setAdapter(peerAdapter);

        stateStore.getPeers().observe(this, peers -> {
            List<Peer> peerList = peers.getAllPeers();
            if(peerList.isEmpty()){
                activityRoomBinding.remotePeers.setVisibility(View.GONE);
                activityRoomBinding.roomState.setVisibility(View.VISIBLE);
            } else {
                activityRoomBinding.remotePeers.setVisibility(View.VISIBLE);
                activityRoomBinding.roomState.setVisibility(View.GONE);
            }
            peerAdapter.replacePeers(peerList);
        });

        stateStore.getNotify().observe(this, notify -> {
            SpannableStringBuilder text = SpannableStringBuilder.valueOf(notify.getText());
            if(notify.getType().equals("error"))
                text.setSpan(new ForegroundColorSpan(Color.RED), 0, text.length(), 0);
            Toast.makeText(this, text, notify.getTimeout()).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_SETTING){
            destroyRoom();
            createRoom();
            joinRoom();
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    private void destroyRoom(){
        huddleClient.closeRoom();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyRoom();
    }

    private void checkPermission() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        String rationale = "Please provide permissions";
        Permissions.Options options =
                new Permissions.Options().setRationaleDialogTitle("Info").setSettingsDialogTitle("Warning");
        Permissions.check(this, permissions, rationale, options, permissionHandler);
    }

    private final PermissionHandler permissionHandler =
            new PermissionHandler() {
                @Override
                public void onGranted() {
                    joinRoom();
                }
            };

    private final RoomEventListener listener = new RoomEventListener() {

        @Override
        public void onRoomUrlGenerated(String roomId, String roomUrl) {
            stateStore.setRoomUrl(roomId, roomUrl);
        }

        @Override
        public void onRoomStateChanged(ConnectionState state) {
            stateStore.setRoomState(state);
        }

        @Override
        public void onError(String message) {

        }

        @Override
        public void onPeerChanged(String actionType, String peerId, @Nullable JSONObject info, @Nullable String displayName) {
            switch (actionType){
                case Constants.ACTION_ADDED: stateStore.addPeer(peerId, info);
                    break;
                case Constants.ACTION_DISPLAY_NAME_CHANGED: stateStore.setPeerDisplayName(peerId, displayName);
                    break;
                case Constants.ACTION_REMOVED: stateStore.removePeer(peerId);
                    break;
                default: Toast.makeText(getApplicationContext(), "Invalid Action Type", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onProducersChanged(String type, HuddleProducer producer) {
            switch (type){
                case Constants
                        .ACTION_ADDED: stateStore.addProducer(producer);
                    break;
                case Constants.ACTION_PAUSED: stateStore.setProducerPaused(producer.getId());
                    break;
                case Constants.ACTION_RESUMED: stateStore.setProducerResumed(producer.getId());
                    break;
                case Constants.ACTION_REMOVED: stateStore.removeProducer(producer.getId());
                    break;
                default: Toast.makeText(getApplicationContext(), "Invalid Producer Action Type", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onConsumerAdded(String peerId, String consumerType, HuddleConsumer consumer, boolean remotelyPaused) {
            stateStore.addConsumer(peerId, consumerType, consumer, remotelyPaused);
        }

        @Override
        public void onConsumerRemoved(String peerId, String consumerId) {
            stateStore.removeConsumer(peerId, consumerId);
        }

        @Override
        public void onConsumerStateChanged(String actionType, String consumerId, String originator) {
            switch (actionType){
                case Constants.ACTION_PAUSED: stateStore.setConsumerPaused(consumerId, originator);
                    break;
                case Constants.ACTION_RESUMED: stateStore.setConsumerResumed(consumerId, originator);
                    break;
                default: Toast.makeText(getApplicationContext(), "Invalid Action Type", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDataProducerChanged(String actionType, HuddleDataProducer dataProducer) {
            switch (actionType){
                case Constants.ACTION_ADDED: stateStore.addDataProducer(dataProducer);
                    break;
                case Constants.ACTION_REMOVED: stateStore.removeDataProducer(dataProducer.getId());
                    break;
                default: Toast.makeText(getApplicationContext(), "Invalid Action Type", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDataConsumerChanged(String actionType, String peedId, HuddleDataConsumer dataConsumer) {
            switch (actionType) {
                case Constants.ACTION_ADDED: stateStore.addDataConsumer(peedId, dataConsumer);
                    break;
                case Constants.ACTION_REMOVED: stateStore.removeDataConsumer(peedId, dataConsumer.getId());
                    break;
                default: Toast.makeText(getApplicationContext(), "Invalid Action Type", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onReactionReceived(Peer peer, String reaction) {
            Toast.makeText(getApplicationContext(), peer.getDisplayName()+" reacted: "+reaction, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onChatReceived(Peer peer, String message) {
            Toast.makeText(getApplicationContext(), peer.getDisplayName()+" messaged: "+message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNotification(Notify notify) {
            Toast.makeText(getApplicationContext(), notify.getText(), Toast.LENGTH_SHORT).show();
        }
    };

    private final MeListener meListener = new MeListener() {
        @Override
        public void onMeReceived(String peerId, String displayName, DeviceInfo deviceInfo) {
            stateStore.setMe(peerId, displayName, deviceInfo);
        }

        @Override
        public void onMediaCapabilitiesReceived(boolean canSendMic, boolean canSendCam) {
            stateStore.setMediaCapabilities(canSendMic, canSendCam);
        }

        @Override
        public void onNewDisplayName(String displayName) {
            stateStore.setDisplayName(displayName);
        }
    };
}