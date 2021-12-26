package com.lpc.smartlife.views.smartlife;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.hxc.basemodule.BaseActivity;
import com.lpc.smartlife.R;
import com.lpc.smartlife.utils.Tools;
import com.lpc.smartlife.views.fragment.HomeFragment;
import com.lpc.smartlife.views.fragment.UserCenterFragment;

public class IndexActivity extends BaseActivity {

    Fragment homeFragment;
    Fragment userCenterFragment;
    FragmentManager fragmentManager;
    RadioGroup radioGroup;
    RadioButton radioButtonHome;
    RadioButton radioButtonMy;
    ConstraintLayout conIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        setImmersiveWindows();
        init();

    }

    protected void init() {
        homeFragment = new HomeFragment();
        userCenterFragment = new UserCenterFragment();
        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.indexFrameLayout, homeFragment, "home").commit();
        fragmentManager.beginTransaction().add(R.id.indexFrameLayout, userCenterFragment, "userCenter").commit();

        fragmentManager.beginTransaction().hide(userCenterFragment).commit();

        radioGroup = findViewById(R.id.indexRadioGroup);
        radioButtonHome = findViewById(R.id.radioButtonHome);
        radioButtonMy = findViewById(R.id.radioButtonMy);
        conIndex = findViewById(R.id.con_index);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonHome:
                        conIndex.setBackground(getDrawable(R.mipmap.background));
                        radioButtonHome.setCompoundDrawablesRelative(null, Tools.makeDrawable(R.mipmap.home_check, IndexActivity.this), null, null);
                        radioButtonMy.setCompoundDrawablesRelative(null, Tools.makeDrawable(R.mipmap.my, IndexActivity.this), null, null);
                        radioButtonHome.setTextColor(getResources().getColor(R.color.checked));
                        radioButtonMy.setTextColor(getResources().getColor(R.color.black));
                        showFragment(homeFragment);
                        break;
                    case R.id.radioButtonMy:
                        conIndex.setBackground(getDrawable(R.drawable.background_white));
                        radioButtonHome.setCompoundDrawablesRelative(null, Tools.makeDrawable(R.mipmap.home, IndexActivity.this), null, null);
                        radioButtonMy.setCompoundDrawablesRelative(null, Tools.makeDrawable(R.mipmap.my_check, IndexActivity.this), null, null);
                        radioButtonHome.setTextColor(getResources().getColor(R.color.black));
                        radioButtonMy.setTextColor(getResources().getColor(R.color.checked));
                        showFragment(userCenterFragment);
                        break;
                }
            }
        });
    }

    public void showFragment(Fragment target) {
        for (Fragment item : fragmentManager.getFragments()) {
            fragmentManager.beginTransaction().hide(item).commit();
        }
        fragmentManager.beginTransaction().show(target).commit();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_index;
    }
}