package com.lpc.smartlife.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lpc.smartlife.R;
import com.lpc.smartlife.entity.Device;
import com.lpc.smartlife.entity.DeviceList;
import com.lpc.smartlife.utils.MyHttpConnection;

import java.util.List;

/**
 * @author byu_rself
 * @date 2021/12/26 20:36
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.VH> {
    private List<Device> mDevice;
    private Context context;
    private TextView textViewDeviceCount;

    public DeviceAdapter(Context context, List<Device> mDevice, TextView textViewDeviceCount) {
        this.mDevice = mDevice;
        this.context = context;
        this.textViewDeviceCount = textViewDeviceCount;
    }

    public static class VH extends RecyclerView.ViewHolder {
        public TextView tvDeviceName;
        public ImageView ivDevice;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
            ivDevice = itemView.findViewById(R.id.ivDevice);
        }
    }

    @NonNull
    @Override
    public DeviceAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.device, parent, false);
        return new DeviceAdapter.VH(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull DeviceAdapter.VH holder, int position) {
        int a = position;

        if (holder == null)
            return;

        holder.tvDeviceName.setText(mDevice.get(position).getDeviceName());
        holder.ivDevice.setImageDrawable(this.context.getDrawable(mDevice.get(position).getDeviceImageId()));
        textViewDeviceCount.setText(DeviceList.deviceList.getCount() + "个设备");
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("是否删除？");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDevice.remove(mDevice.get(a));

                        // 删除设备
                        deleteDevice(mDevice.get(a).getDeviceId());

                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDevice.size();
    }

    public void deleteDevice(Integer deviceId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection conn = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("deviceId", deviceId);
                String response = conn.myPost("/deleteDevice", json);
            }
        }).start();
    }
}
