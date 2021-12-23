package com.lpc.smartlife.views.smartlife;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.hxc.basemodule.BaseActivity;
import com.lpc.smartlife.MainActivity;
import com.lpc.smartlife.R;

public class IndexActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        setImmersiveWindows();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_index;
    }
}