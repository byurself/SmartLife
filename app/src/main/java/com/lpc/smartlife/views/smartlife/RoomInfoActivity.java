package com.lpc.smartlife.views.smartlife;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxc.basemodule.BaseActivity;
import com.lpc.smartlife.R;
import com.lpc.smartlife.entity.DeviceList;
import com.lpc.smartlife.entity.User;
import com.lpc.smartlife.utils.CreateDialog;
import com.lpc.smartlife.utils.Tools;

public class RoomInfoActivity extends BaseActivity {

    ImageButton imageButtonBack;
    TextView tvUsersHome;
    TextView tvEditUser;
    TextView tvEditDeviceCount;
    ImageView ivEditUser;
    ImageView ivEditDeviceCount;
    LinearLayout homeName;
    LinearLayout deviceManager;
    CreateDialog createDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_info);

        setImmersiveWindows();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tvUsersHome.setText(User.user.getUserName());
        tvEditUser.setText(User.user.getUserName());
        tvEditDeviceCount.setText(DeviceList.deviceList.getCount() + "个设备");
    }

    public void init() {
        imageButtonBack = findViewById(R.id.imageButtonBack);
        tvUsersHome = findViewById(R.id.tvUsersHome);
        tvEditUser = findViewById(R.id.tvEditUser);
        tvEditDeviceCount = findViewById(R.id.tvEditDeviceCount);
        ivEditUser = findViewById(R.id.ivEditUser);
        ivEditDeviceCount = findViewById(R.id.ivEditDeviceCount);
        homeName = findViewById(R.id.homeName);
        deviceManager = findViewById(R.id.deviceManager);

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        homeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);
            }
        });

    }

    public void showDialog(View view) {
        createDialog = new CreateDialog(this, R.layout.edit_room_name_dialog, R.style.dialog, onClickListenerCancel, onClickListenerConfirm, tvEditUser.getText().toString().trim());
        createDialog.show();
    }

    private View.OnClickListener onClickListenerCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            createDialog.cancel();
        }
    };

    private View.OnClickListener onClickListenerConfirm = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (createDialog.et_home_name.getText().toString().trim().length() == 0)
                Tools.displayToast(view.getContext(), "名称不能为空");
            else {
                User.user.setUserName(createDialog.et_home_name.getText().toString().trim());
                tvUsersHome.setText(User.user.getUserName());
                tvEditUser.setText(User.user.getUserName());
                createDialog.cancel();
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_info;
    }
}