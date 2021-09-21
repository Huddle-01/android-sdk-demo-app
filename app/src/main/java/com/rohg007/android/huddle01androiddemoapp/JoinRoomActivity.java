package com.rohg007.android.huddle01androiddemoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.rohg007.android.huddle01androiddemoapp.databinding.ActivityJoinRoomBinding;

public class JoinRoomActivity extends AppCompatActivity {

    private ActivityJoinRoomBinding joinRoomBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        joinRoomBinding = DataBindingUtil.setContentView(this, com.rohg007.android.huddle01androiddemoapp.R.layout.activity_join_room);
        joinRoomBinding.startMeetingBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, RoomActivity.class));
        });

        joinRoomBinding.joinMeetingBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, RoomActivity.class);
            intent.putExtra("roomId", joinRoomBinding.roomIdEdt.getText().toString());
            startActivity(intent);
        });
    }
}