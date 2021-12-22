package com.hxc.basemodule;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public abstract class BaseActivity extends AppCompatActivity {
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_PHONE_STATE
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码

    protected Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

    }

    protected abstract int getLayoutId();

    protected void showBackButton(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.icon_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    protected  void setTitle(String title){
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    protected void showMenu(int menuId , Toolbar.OnMenuItemClickListener listener){
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(menuId);
        toolbar.setOnMenuItemClickListener(listener);
    }

    //进行权限检查
    protected void getPersimmions() {
        mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            // 启动当前权限页面的公开接口
            requestPermissions(PERMISSIONS); // 请求权限

        } else {
            allPermissionsGranted();
        }
    }

    // 全部权限均已获取,需要子类具体实现
    public void allPermissionsGranted() {

    }


    // 请求权限兼容低版本
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CODE ) {
            if (!mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                allPermissionsGranted();
            } else {
                for(String perm :PERMISSIONS){
                    if(mPermissionsChecker.lacksPermission(perm)) {
                        showMissingPermissionDialog(perm);
                        break;
                    }
                }

            }
        }
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode ==REQUEST_CODE){
            for(int i=0;i<permissions.length;i++){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    showMissingPermissionDialog(permissions[i]);
                    break;
                }
            }
        }
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog(String lackPermission) {
        String dialogMessage="";
        if(lackPermission.equals( Manifest.permission.CAMERA)){
            dialogMessage = getString(R.string.LackCamera);
        }else if(lackPermission.equals( Manifest.permission.READ_CALENDAR) || lackPermission.equals( Manifest.permission.WRITE_CALENDAR)){
            dialogMessage = getString(R.string.LackCalendar);
        }else if(lackPermission.equals( Manifest.permission.READ_CONTACTS) || lackPermission.equals( Manifest.permission.WRITE_CONTACTS)){
            dialogMessage = getString(R.string.LackCantacts);
        }else if(lackPermission.equals( Manifest.permission.ACCESS_COARSE_LOCATION) || lackPermission.equals( Manifest.permission.ACCESS_FINE_LOCATION)){
            dialogMessage = getString(R.string.LackLocation);
        }else if(lackPermission.equals( Manifest.permission.RECORD_AUDIO)){
            dialogMessage = getString(R.string.LackMicrophone);
        }else if(lackPermission.equals( Manifest.permission.CALL_PHONE) || lackPermission.equals( Manifest.permission.READ_PHONE_STATE)){
            dialogMessage = getString(R.string.LackPhone);
        }else if(lackPermission.equals( Manifest.permission.BODY_SENSORS)){
            dialogMessage = getString(R.string.LackSensors);
        }else if(lackPermission.equals( Manifest.permission.SEND_SMS) || lackPermission.equals( Manifest.permission.READ_SMS )){
            dialogMessage = getString(R.string.LackSms);
        }else if(lackPermission.equals( Manifest.permission.READ_EXTERNAL_STORAGE) || lackPermission.equals( Manifest.permission.WRITE_EXTERNAL_STORAGE )){
            dialogMessage = getString(R.string.LackExternalStorage);
        }else{
            dialogMessage = "缺少"+lackPermission+"权限";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.PermissionWarning);
        builder.setMessage(dialogMessage);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.Quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        builder.setPositiveButton(R.string.Settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        builder.show();
    }
    public boolean checkGPSIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    /**
     * 跳转GPS设置
     */
    public void openGPSSettings() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    private ProgressDialog mProgressDialog;

    public void showProgressDialog(String title, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setTitle("");
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showAlertDialog(String title, String msg, int icon) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setIcon(icon)
                .setPositiveButton(getResources().getString(R.string.OK), null)
                .create();
        dialog.show();
    }

    public void showAlertDialog(String title, String msg) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(getResources().getString(R.string.OK), null)
                .create();
        dialog.show();
    }

    public void showAlertDialog(String title, String msg ,DialogInterface.OnClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(getResources().getString(R.string.OK), listener)
                .create();
        dialog.show();
    }

    class PermissionsChecker {
        private final Context mContext;

        public PermissionsChecker(Context context) {
            mContext = context.getApplicationContext();
        }

        // 判断权限集合
        public boolean lacksPermissions(String... permissions) {
            for (String permission : permissions) {
                if (lacksPermission(permission)) {
                    return true;
                }
            }
            return false;
        }

        // 判断是否缺少权限
        private boolean lacksPermission(String permission) {
            return ContextCompat.checkSelfPermission(mContext, permission) ==
                    PackageManager.PERMISSION_DENIED;
        }
    }

    /**
     * 获取系统状态栏的高度
     * @return
     */
    protected int  getStateBarHeight(){
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        };
        return result;
    }

    /**
     * 设置沉浸式窗口
     */
    protected  void setImmersiveWindows(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            View root = ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
            root.setPadding(0,getStateBarHeight(),0,0);
        }
    }
}
