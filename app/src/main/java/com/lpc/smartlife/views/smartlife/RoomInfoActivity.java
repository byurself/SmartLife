package com.lpc.smartlife.views.smartlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hxc.basemodule.BaseActivity;
import com.lpc.smartlife.R;

public class RoomInfoActivity extends BaseActivity {

    ImageButton imageButtonBack;
    TextView tvUsersHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_info);

        init();
    }

    public void init(){
        imageButtonBack = findViewById(R.id.imageButtonBack);
        tvUsersHome = findViewById(R.id.tvUsersHome);

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_info;
    }
}