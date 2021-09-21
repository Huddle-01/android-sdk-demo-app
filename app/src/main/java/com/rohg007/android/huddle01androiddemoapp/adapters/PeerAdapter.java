package com.rohg007.android.huddle01androiddemoapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohg007.android.huddle01_android_sdk.models.Peer;
import com.rohg007.android.huddle01androiddemoapp.R;
import com.rohg007.android.huddle01androiddemoapp.state.StateStore;
import com.rohg007.android.huddle01androiddemoapp.viewmodels.PeerProps;
import com.rohg007.android.huddle01androiddemoapp.views.PeerView;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

public class PeerAdapter extends RecyclerView.Adapter<PeerAdapter.PeerViewHolder> {

    private static final String TAG = "PeerAdapter";

    @NonNull
    private StateStore mStore;
    @NonNull private LifecycleOwner mLifecycleOwner;

    private List<Peer> mPeers = new LinkedList<>();

    private int containerHeight;

    public PeerAdapter(
            @NonNull StateStore store,
            @NonNull LifecycleOwner lifecycleOwner) {
        mStore = store;
        mLifecycleOwner = lifecycleOwner;
    }

    public void replacePeers(@NonNull List<Peer> peers) {
        mPeers = peers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PeerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        containerHeight = parent.getHeight();
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_remote_peer, parent, false);
        return new PeerViewHolder(
                view, new PeerProps(((AppCompatActivity) context).getApplication(), mStore));
    }

    @Override
    public void onBindViewHolder(@NonNull PeerViewHolder holder, int position) {
        // update height
        ViewGroup.LayoutParams layoutParams = holder.mPeerView.getLayoutParams();
        layoutParams.height = getItemHeight();
        holder.mPeerView.setLayoutParams(layoutParams);
        // bind
        holder.bind(mLifecycleOwner, mPeers.get(position));
    }

    @Override
    public int getItemCount() {
        return mPeers.size();
    }

    private int getItemHeight() {
        int itemCount = getItemCount();
        if (itemCount <= 1) {
            return containerHeight;
        } else if (itemCount <= 3) {
            return containerHeight / itemCount;
        } else {
            return (int) (containerHeight / 3.2);
        }
    }

    static class PeerViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        final PeerView mPeerView;
        @NonNull final PeerProps mPeerProps;

        PeerViewHolder(@NonNull View view, @NonNull PeerProps peerProps) {
            super(view);
            mPeerView = view.findViewById(R.id.remote_peer);
            mPeerProps = peerProps;
        }

        void bind(LifecycleOwner owner, @NonNull Peer peer) {
            Log.d(TAG, "bind() id: " + peer.getId() + ", name: " + peer.getDisplayName());
            mPeerProps.connect(owner, peer.getId());
            mPeerView.setProps(mPeerProps);
        }
    }
}
