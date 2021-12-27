package com.lpc.smartlife.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.lpc.smartlife.R;
import com.lpc.smartlife.entity.Room;

import java.util.List;

/**
 * @author byu_rself
 * @date 2021/12/27 21:41
 */
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.VH> {
    private List<Room> mRoom;
    private Context context;

    public RoomAdapter(Context context, List<Room> mRoom) {
        this.mRoom = mRoom;
        this.context = context;
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
        if (holder == null)
            return;

        holder.textViewRoomName.setText(mRoom.get(position).getRoomName());
        holder.textViewRoomDeviceCount.setText(mRoom.get(position).getDeviceCount() + "个设备");
    }

    @Override
    public int getItemCount() {
        return mRoom.size();
    }

}
