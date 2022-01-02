package com.lpc.smartlife.views.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.lpc.lpcmqttmodule.MyMqttService;
import com.lpc.smartlife.R;
import com.lpc.smartlife.views.smartlife.DeviceInfoActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * @author byu_rself
 * @date 2021/12/31 16:47
 */
public class ESP32Fragment extends Fragment {
    ViewPager viewPager;
    List<View> viewList;
    RadioGroup radioGroup;
    ImageButton ib_common_open;
    ImageButton ib_common_close;
    TimePicker timePicker;
    Button bt_start_timing;
    Button bt_count_down;
    NumberPicker np_hour;
    NumberPicker np_minute;
    NumberPicker np_second;

    private int timingTotalSeconds;
    private Calendar date;
    private int timingHour;
    private int timingMinute;
    private int countDownHour = 0;
    private int countDownMinute = 0;
    private int countDownSecond = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.esp32_info, container, false);
        MyMqttService.startService(v.getContext());
        init(v);
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init(View v) {
        viewPager = v.findViewById(R.id.viewPaper);
        radioGroup = v.findViewById(R.id.radioGroup);

        LayoutInflater inflater = getLayoutInflater().from(this.getContext());
        View view_common = inflater.inflate(R.layout.esp32_common_pattern, null);
        View view_timing = inflater.inflate(R.layout.esp32_timing_pattern, null);
        View view_count_down = inflater.inflate(R.layout.esp32_count_down_pattern, null);

        viewList = new ArrayList<>();
        viewList.add(view_common);
        viewList.add(view_timing);
        viewList.add(view_count_down);

        initCommonView(view_common);
        initTimingView(view_timing);
        initCountDown(view_count_down);

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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                for (int j = 0; j < radioGroup.getChildCount(); j++) {
                    if (radioGroup.getCheckedRadioButtonId() == radioGroup.getChildAt(j).getId()) {
                        viewPager.setCurrentItem(j);
                        break;
                    }
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(position);
                radioButton.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initCommonView(View root) {
        ib_common_open = root.findViewById(R.id.ib_common_open);
        ib_common_close = root.findViewById(R.id.ib_common_close);

        ib_common_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cmd", "set" + DeviceInfoActivity.macAddress);
                jsonObject.put("status", 1);
                jsonObject.put("second", 0);
                jsonObject.put("command", 1);
                MyMqttService.publish(jsonObject.toJSONString());
            }
        });

        ib_common_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cmd", "set" + DeviceInfoActivity.macAddress);
                jsonObject.put("status", 0);
                jsonObject.put("second", 0);
                jsonObject.put("command", 1);
                MyMqttService.publish(jsonObject.toJSONString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initTimingView(View root) {
        timePicker = root.findViewById(R.id.timepicker);
        bt_start_timing = root.findViewById(R.id.bt_start_timing);

        date = Calendar.getInstance();
        date.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        timePicker.setIs24HourView(true);
        timingHour = timePicker.getHour();
        timingMinute = timePicker.getMinute();

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                timingHour = timePicker.getHour();
                timingMinute = timePicker.getMinute();
            }
        });
        bt_start_timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = Calendar.getInstance();
                date.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                int am_pm = date.get(Calendar.AM_PM);
                int time = date.get(Calendar.HOUR) + ((am_pm == Calendar.AM) ? 0 : 12);
                int nowSeconds = time * 3600 + date.get(Calendar.MINUTE) * 60 + date.get(Calendar.SECOND);
                timingTotalSeconds = timingHour * 3600 + timingMinute * 60 - nowSeconds;
                if (timingTotalSeconds < 0)
                    timingTotalSeconds += 24 * 3600;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cmd", "countDown" + DeviceInfoActivity.macAddress);
                jsonObject.put("status", 1);
                jsonObject.put("second", timingTotalSeconds);
                jsonObject.put("command", 2);
                MyMqttService.publish(jsonObject.toJSONString());
            }
        });
    }

    private void initCountDown(View root) {

        np_hour = root.findViewById(R.id.np_hour);
        np_minute = root.findViewById(R.id.np_minute);
        np_second = root.findViewById(R.id.np_second);
        bt_count_down = root.findViewById(R.id.bt_count_down);

        np_hour.setMaxValue(23);
        np_minute.setMaxValue(59);
        np_second.setMaxValue(59);

        np_hour.setMinValue(0);
        np_minute.setMinValue(0);
        np_second.setMinValue(0);

        np_hour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                countDownHour = numberPicker.getValue();
            }
        });
        np_second.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                countDownSecond = numberPicker.getValue();
            }
        });
        np_minute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                countDownMinute = numberPicker.getValue();
            }
        });

        bt_count_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownSecond = countDownHour * 3600 + countDownMinute * 60 + countDownSecond;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cmd", "countDown" + DeviceInfoActivity.macAddress);
                jsonObject.put("status", 1);
                jsonObject.put("second", countDownSecond);
                jsonObject.put("command", 1);
                MyMqttService.publish(jsonObject.toJSONString());
            }
        });
    }
}
