package com.rohg007.android.huddle01androiddemoapp.state;

import android.text.TextUtils;

import com.rohg007.android.huddle01_android_sdk.ConnectionState;
import com.rohg007.android.huddle01_android_sdk.models.Consumers;
import com.rohg007.android.huddle01_android_sdk.models.DataConsumers;
import com.rohg007.android.huddle01_android_sdk.models.DataProducers;
import com.rohg007.android.huddle01_android_sdk.models.DeviceInfo;
import com.rohg007.android.huddle01_android_sdk.models.HuddleConsumer;
import com.rohg007.android.huddle01_android_sdk.models.HuddleDataConsumer;
import com.rohg007.android.huddle01_android_sdk.models.HuddleDataProducer;
import com.rohg007.android.huddle01_android_sdk.models.HuddleProducer;
import com.rohg007.android.huddle01_android_sdk.models.Me;
import com.rohg007.android.huddle01_android_sdk.models.Notify;
import com.rohg007.android.huddle01_android_sdk.models.Peers;
import com.rohg007.android.huddle01_android_sdk.models.Producers;
import com.rohg007.android.huddle01_android_sdk.models.RoomInfo;

import org.json.JSONObject;

import androidx.lifecycle.MutableLiveData;

public class StateStore {

    CustomTypeMutableLiveData<RoomInfo> roomInfo = new CustomTypeMutableLiveData<>(RoomInfo::new);
    CustomTypeMutableLiveData<Me> me = new CustomTypeMutableLiveData<>(Me::new);
    CustomTypeMutableLiveData<Producers> producers = new CustomTypeMutableLiveData<>(Producers::new);
    CustomTypeMutableLiveData<DataProducers> dataProducers = new CustomTypeMutableLiveData<>(DataProducers::new);
    CustomTypeMutableLiveData<Peers> peers = new CustomTypeMutableLiveData<>(Peers::new);
    CustomTypeMutableLiveData<Consumers> consumers = new CustomTypeMutableLiveData<>(Consumers::new);
    CustomTypeMutableLiveData<DataConsumers> dataConsumers = new CustomTypeMutableLiveData<>(DataConsumers::new);
    MutableLiveData<Notify> notify = new MutableLiveData<>();

    public void setRoomUrl(String roomId, String url){
        roomInfo.postValue(roomInfo->{
            roomInfo.setRoomId(roomId);
            roomInfo.setUrl(url);
        });
    }

    public void setRoomState(ConnectionState state){
        roomInfo.postValue(roomInfo->{
            roomInfo.setConnectionState(state);
        });
        if(state == ConnectionState.CLOSED){
            peers.postValue(Peers::clear);
            me.postValue(Me::clear);
            producers.postValue(Producers::clear);
            consumers.postValue(Consumers::clear);
        }
    }

    public void setMe(String peerId, String displayName, DeviceInfo device){
        me.postValue(me->{
            me.setId(peerId);
            me.setDisplayName(displayName);
            me.setDeviceInfo(device);
        });
    }

    public void setMediaCapabilities(boolean canSendMic, boolean canSendCam){
        me.postValue(me-> {
            me.setCanSendMic(canSendMic);
            me.setCanSendCam(canSendCam);
        });
    }

    public void setDisplayName(String displayName){
        me.postValue(me->me.setDisplayName(displayName));
    }

    public void addProducer(HuddleProducer producer){
        producers.postValue(producers->producers.addProducer(producer));
    }

    public void setProducerPaused(String producerId){
        producers.postValue(producers->producers.setProducerPaused(producerId));
    }

    public void setProducerResumed(String producerId){
        producers.postValue(producers->producers.setProducerResumed(producerId));
    }

    public void removeProducer(String producerId){
        producers.postValue(producers->producers.removeProducer(producerId));
    }

    public void addDataProducer(HuddleDataProducer dataProducer){
        dataProducers.postValue(dataProducers->dataProducers.addDataProducer(dataProducer));
    }

    public void removeDataProducer(String dataProducerId){
        dataProducers.postValue(dataProducers->dataProducers.removeDataProducer(dataProducerId));
    }

    public void addPeer(String peerId, JSONObject info){
        peers.postValue(peers-> {
            peers.addPeer(peerId, info);
        });
    }

    public void setPeerDisplayName(String peerId, String displayName){
        peers.postValue(peers->peers.setPeerDisplayName(peerId, displayName));
    }

    public void removePeer(String peerId){
        roomInfo.postValue(roomInfo->{
            if(!TextUtils.isEmpty(peerId) && roomInfo.getActiveSpeakerId().equals(peerId))
                roomInfo.setActiveSpeakerId(null);
            if(!TextUtils.isEmpty(peerId) && roomInfo.getStatsPeerId().equals(peerId))
                roomInfo.setStatsPeerId(null);
        });
        peers.postValue(peers->peers.removePeer(peerId));
    }

    public void addConsumer(String peerId, String type, HuddleConsumer consumer, boolean remotelyPaused){
        consumers.postValue(consumers->consumers.addConsumer(type, consumer, remotelyPaused));
        peers.postValue(peers->peers.addConsumer(peerId, consumer));
    }

    public void removeConsumer(String peerId, String consumerId){
        consumers.postValue(consumers->consumers.removeConsumer(consumerId));
        peers.postValue(peers->peers.removeConsumer(peerId, consumerId));
    }

    public void setConsumerPaused(String consumerId, String originator){
        consumers.postValue(consumers->consumers.setConsumerPaused(consumerId, originator));
    }

    public void setConsumerResumed(String consumerId, String originator){
        consumers.postValue(consumers->consumers.setConsumerResumed(consumerId, originator));
    }

    public void addDataConsumer(String peerId, HuddleDataConsumer dataConsumer){
        dataConsumers.postValue(dataConsumers->dataConsumers.addDataConsumer(dataConsumer));
        peers.postValue(peers->peers.addDataConsumer(peerId, dataConsumer));
    }

    public void removeDataConsumer(String peerId, String dataConsumerId){
        dataConsumers.postValue(dataConsumers->dataConsumers.removeDataConsumer(dataConsumerId));
        peers.postValue(peers->peers.removeDataConsumer(peerId, dataConsumerId));
    }

    public CustomTypeMutableLiveData<RoomInfo> getRoomInfo() {
        return roomInfo;
    }

    public CustomTypeMutableLiveData<Me> getMe() {
        return me;
    }

    public CustomTypeMutableLiveData<Producers> getProducers() {
        return producers;
    }

    public CustomTypeMutableLiveData<Peers> getPeers() {
        return peers;
    }

    public CustomTypeMutableLiveData<Consumers> getConsumers() {
        return consumers;
    }

    public MutableLiveData<Notify> getNotify() {
        return notify;
    }
}
