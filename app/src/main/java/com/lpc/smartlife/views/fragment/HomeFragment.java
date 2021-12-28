package com.lpc.smartlife.views.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lpc.smartlife.R;
import com.lpc.smartlife.adapter.DeviceAdapter;
import com.lpc.smartlife.adapter.RoomAdapter;
import com.lpc.smartlife.entity.Device;
import com.lpc.smartlife.entity.DeviceList;
import com.lpc.smartlife.entity.Room;
import com.lpc.smartlife.entity.RoomList;
import com.lpc.smartlife.entity.User;
import com.lpc.smartlife.utils.MyHttpConnection;
import com.lpc.smartlife.views.smartlife.RoomInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author byu_rself
 * @date 2021/12/24 21:51
 */
public class HomeFragment extends Fragment {

    RadioGroup indexRadioGroup;
    RadioButton radioButtonDevices;
    RadioButton radioButtonRoom;
    ViewPager viewPager;
    ArrayList<View> viewList;
    TextView textViewDeviceCount;
    TextView textViewUserHome;
    ImageView imageView;

    RecyclerView rvDevice;
    RecyclerView rvRoom;
    DeviceAdapter deviceAdapter;
    RoomAdapter roomAdapter;

    View devices;
    View room;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_layout, container, false);
        init(v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        textViewUserHome.setText(User.user.getUserName());
        deviceAdapter.notifyDataSetChanged();
        roomAdapter.notifyDataSetChanged();
    }

    public void init(View v) {
        indexRadioGroup = v.findViewById(R.id.indexRadioGroup);
        radioButtonDevices = v.findViewById(R.id.radioButtonDevices);
        radioButtonRoom = v.findViewById(R.id.radioButtonRoom);
        viewPager = v.findViewById(R.id.viewPager);
        textViewDeviceCount = v.findViewById(R.id.textViewDeviceCount);
        textViewUserHome = v.findViewById(R.id.textViewUserHome);
        imageView = v.findViewById(R.id.imageView8);

        textViewUserHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RoomInfoActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RoomInfoActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        LayoutInflater inflater = getLayoutInflater().from(this.getContext());
        devices = inflater.inflate(R.layout.devices_layout, null);
        room = inflater.inflate(R.layout.room_layout, null);

        viewList = new ArrayList<>();
        viewList.add(devices);
        viewList.add(room);

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(viewList.get(position));
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };

        initDeviceRecyclerView(devices);
        initRoomRecyclerView(room);

        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indexRadioGroup.check(indexRadioGroup.getChildAt(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        indexRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonDevices:
                        viewPager.setCurrentItem(0);
                        radioButtonDevices.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                        radioButtonRoom.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        break;
                    case R.id.radioButtonRoom:
                        viewPager.setCurrentItem(1);
                        radioButtonDevices.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        radioButtonRoom.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                        break;
                }
            }
        });
    }

    public void initDeviceRecyclerView(View root) {
        rvDevice = root.findViewById(R.id.rvDevice);

        deviceAdapter = new DeviceAdapter(root.getContext(), DeviceList.deviceList.getDeviceList(), textViewDeviceCount);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(root.getContext(), 2, GridLayoutManager.VERTICAL, false);

        rvDevice.setLayoutManager(gridLayoutManager);
        rvDevice.setAdapter(deviceAdapter);
        rvDevice.setItemAnimator(new DefaultItemAnimator());
    }

    public void initRoomRecyclerView(View root) {
        rvRoom = root.findViewById(R.id.rvRoom);
        // 布局设置
        LinearLayoutManager manager = new LinearLayoutManager(root.getContext());
        rvRoom.setLayoutManager(manager);
        // 设置适配器
        RoomList.roomList.getRoomList();
        roomAdapter = new RoomAdapter(root.getContext(), RoomList.roomList.getRooms());
        rvRoom.setAdapter(roomAdapter);
        // 设置分割线
        rvRoom.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL));
        // 设置增加或删除条目的动画
        rvRoom.setItemAnimator(new DefaultItemAnimator());
    }

}
