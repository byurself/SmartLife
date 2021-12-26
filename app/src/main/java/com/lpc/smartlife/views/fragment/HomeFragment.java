package com.lpc.smartlife.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lpc.smartlife.R;
import com.lpc.smartlife.adapter.DeviceAdapter;
import com.lpc.smartlife.entity.Device;
import com.lpc.smartlife.entity.DeviceList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author byu_rself
 * @date 2021/12/24 21:51
 */
public class HomeFragment extends Fragment {

    RadioGroup indexRadioGroup;
    RadioButton radioButtonDevices;
    RadioButton radioButtonType;
    ViewPager viewPager;
    ArrayList<View> viewList;
    TextView textViewDeviceCount;

    RecyclerView recyclerView;
    DeviceAdapter deviceAdapter;
    List<Device> myDeviceList;

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

    public void init(View v) {
        indexRadioGroup = v.findViewById(R.id.indexRadioGroup);
        radioButtonDevices = v.findViewById(R.id.radioButtonDevices);
        radioButtonType = v.findViewById(R.id.radioButtonType);
        viewPager = v.findViewById(R.id.viewPager);
        textViewDeviceCount = v.findViewById(R.id.textViewDeviceCount);

        LayoutInflater inflater = getLayoutInflater().from(this.getContext());
        View devices = inflater.inflate(R.layout.devices_layout, null);
        View type = inflater.inflate(R.layout.type_layout, null);

        viewList = new ArrayList<>();
        viewList.add(devices);
        viewList.add(type);

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

        initRecyclerView(devices);

        indexRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonDevices:
                        viewPager.setCurrentItem(0);
                        radioButtonDevices.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                        radioButtonType.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                        break;
                    case R.id.radioButtonType:
                        viewPager.setCurrentItem(1);
                        radioButtonDevices.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                        radioButtonType.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                        break;
                }
            }
        });
    }

    public void initRecyclerView(View root) {
        recyclerView = root.findViewById(R.id.recyclerView);

        myDeviceList = new ArrayList<Device>();
        deviceAdapter = new DeviceAdapter(root.getContext(), DeviceList.deviceList.getDeviceList());
        DeviceList.deviceList.getDeviceList().add(new Device("FreeBuds3",R.mipmap.freebuds3,1,"123456"));
        DeviceList.deviceList.getDeviceList().add(new Device("FreeBuds3",R.mipmap.huaweifreebuds4,1,"123456"));
        DeviceList.deviceList.getDeviceList().add(new Device("FreeBuds3",R.mipmap.huaweifreebuds4,1,"123456"));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(root.getContext(), 2, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(deviceAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        textViewDeviceCount.setText(DeviceList.deviceList.getCount() + "个设备");
    }

}
