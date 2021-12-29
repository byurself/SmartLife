package com.lpc.smartlife.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.lpc.smartlife.R;
import com.lpc.smartlife.entity.DeviceList;
import com.lpc.smartlife.entity.Room;
import com.lpc.smartlife.entity.RoomList;
import com.lpc.smartlife.utils.MyHttpConnection;
import com.lpc.smartlife.views.smartlife.RoomDeviceActivity;

import java.util.List;

/**
 * @author byu_rself
 * @date 2021/12/27 21:41
 */
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.VH> {
    private List<Room> mRoom;
    private Context context;
    private int layoutId;

    public RoomAdapter(Context context, List<Room> mRoom, int layoutId) {
        this.mRoom = mRoom;
        this.context = context;
        this.layoutId = layoutId;
    }

    public static class VH extends RecyclerView.ViewHolder {
        public TextView textViewRoomName;
        public TextView textViewRoomDeviceCount;

        public VH(@NonNull View itemView) {
            super(itemView);
            textViewRoomName = itemView.findViewById(R.id.textViewRoomName);
            textViewRoomDeviceCount = itemView.findViewById(R.id.textViewRoomDeviceCount);
        }
    }

    @NonNull
    @Override
    public RoomAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.room, parent, false);
        return new RoomAdapter.VH(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.VH holder, int position) {
        int a = position;

        if (holder == null)
            return;

        holder.textViewRoomName.setText(mRoom.get(position).getRoomName());
        holder.textViewRoomDeviceCount.setText(mRoom.get(position).getDeviceCount() + "个设备");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RoomDeviceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("roomId", mRoom.get(a).getRoomId());
                bundle.putString("roomName", mRoom.get(a).getRoomName());
                bundle.putString("deviceCount", String.valueOf(mRoom.get(a).getDeviceCount()));
                bundle.putString("userId", mRoom.get(a).getUserId());
                bundle.putInt("index", a);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });

        if (layoutId == 1) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("是否删除？");
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 删除房间
                            deleteRoom(mRoom.get(a).getRoomId());

                            mRoom.remove(mRoom.get(a));

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
    }

    public void deleteRoom(Integer roomId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpConnection conn = new MyHttpConnection();
                JSONObject json = new JSONObject();
                json.put("roomId", roomId);
                DeviceList.deviceList.httpGetDeviceList();
                String response = conn.myPost("/deleteRoom", json);
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return mRoom.size();
    }

}
