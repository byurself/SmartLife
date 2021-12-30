package com.lpc.smartlife.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lpc.smartlife.R;
import com.lpc.smartlife.adapter.ConnectDeviceAdapter;
import com.lpc.smartlife.entity.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * @author byu_rself
 * @date 2021/12/28 17:28
 */
public class CreateDialog extends Dialog {
    /**
     * 上下文对象 *
     */
    Activity context;

    public Button bt_cancel;
    public Button bt_confirm;
    public EditText et_home_name;
    public RecyclerView recyclerView;
    public List<Device> devices;
    public ConnectDeviceAdapter connectDeviceAdapter;
    public String deviceName;

    private String roomName;
    private int layout_id;
    private View.OnClickListener mClickListener_cancel;
    private View.OnClickListener mClickListener_confirm;


    public CreateDialog(Activity context, int layout_id, int theme, View.OnClickListener clickListener_cancel, View.OnClickListener clickListener_confirm, String roonName) {
        super(context, theme);
        this.context = context;
        this.mClickListener_cancel = clickListener_cancel;
        this.mClickListener_confirm = clickListener_confirm;
        this.roomName = roonName;
        this.layout_id = layout_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(layout_id);
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

        // 根据id在布局中找到控件对象
        bt_cancel = findViewById(R.id.btCancel);
        bt_confirm = findViewById(R.id.btConfirm);

        if (layout_id == R.layout.connect_dialog) {
            initRecyclerView();
        } else {
            et_home_name = findViewById(R.id.et_home_name);
            this.et_home_name.setText(roomName);
        }
        // 为按钮绑定点击事件监听器
        bt_cancel.setOnClickListener(mClickListener_cancel);
        bt_confirm.setOnClickListener(mClickListener_confirm);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.rvConnect);

        devices = new ArrayList<>();

        connectDeviceAdapter = new ConnectDeviceAdapter(context, devices, R.layout.connect_device);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(connectDeviceAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
