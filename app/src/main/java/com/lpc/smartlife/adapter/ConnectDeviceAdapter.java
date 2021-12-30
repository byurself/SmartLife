package com.lpc.smartlife.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.lpc.smartlife.R;
import com.lpc.smartlife.entity.Device;

import java.util.List;

/**
 * @author byu_rself
 * @date 2021/12/30 23:22
 */
public class ConnectDeviceAdapter extends RecyclerView.Adapter<ConnectDeviceAdapter.VH> {
    private List<Device> mDevice;
    private Context context;
    private int layoutId;

    public ConnectDeviceAdapter(Context context, List<Device> mDevice, int layoutId) {
        this.mDevice = mDevice;
        this.context = context;
        this.layoutId = layoutId;
    }

    public static class VH extends RecyclerView.ViewHolder {
        public TextView tv_device_name;
        public ImageView iv_device;
        public TextView tv_mac;
        public CheckBox cb_check;

        public VH(@NonNull View itemView) {
            super(itemView);
            tv_device_name = itemView.findViewById(R.id.tv_device_name);
            iv_device = itemView.findViewById(R.id.iv_device);
            tv_mac = itemView.findViewById(R.id.tv_mac);
            cb_check = itemView.findViewById(R.id.cb_connect);
        }
    }

    @NonNull
    @Override
    public ConnectDeviceAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, parent, false);
        return new ConnectDeviceAdapter.VH(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull ConnectDeviceAdapter.VH holder, int position) {
        int a = position;
        holder.tv_device_name.setText(mDevice.get(position).getDeviceName());
        holder.iv_device.setImageDrawable(this.context.getDrawable(mDevice.get(position).getDeviceImageId()));
        holder.tv_mac.setText(mDevice.get(position).getMacAddress());
        mDevice.get(a).setCheck(holder.cb_check.isChecked());
        holder.cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mDevice.get(a).setCheck(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDevice.size();
    }
}
