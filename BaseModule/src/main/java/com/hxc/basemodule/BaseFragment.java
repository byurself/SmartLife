package com.hxc.basemodule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    protected abstract int getLayoutId();
    protected Toolbar toolbar;
    private View root;


    public View rootView(LayoutInflater inflater, ViewGroup container) {

        root = inflater.inflate(getLayoutId(), container, false);
        return root;
    }

    /**
     * 设置toolbar上的navigation按钮的图标和事件
     * @param icon
     * @param listener
     */
    protected void showNavigationButton(int icon, View.OnClickListener listener){
        if(root != null ) {
            toolbar = root.findViewById(R.id.toolbar);

            toolbar.setNavigationIcon(icon);
            toolbar.setNavigationOnClickListener(listener);
        }
    }

    protected  void setTitle(String title){
        if(root != null) {
            TextView toolbar_title = root.findViewById(R.id.toolbar_title);
            toolbar_title.setText(title);
        }
    }

    protected void showMenu(int menuId , Toolbar.OnMenuItemClickListener listener){
        if(root != null) {
            toolbar = root.findViewById(R.id.toolbar);
            toolbar.inflateMenu(menuId);
            toolbar.setOnMenuItemClickListener(listener);
        }
    }

    public void displayToast(String message) {
        Toast.makeText(root.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showAlertDialog(String title, String msg, int icon) {
        AlertDialog dialog = new AlertDialog.Builder(root.getContext())
                .setTitle(title)
                .setMessage(msg)
                .setIcon(icon)
                .setPositiveButton(getResources().getString(R.string.OK), null)
                .create();
        dialog.show();
    }

    public void showAlertDialog(String title, String msg) {
        AlertDialog dialog = new AlertDialog.Builder(root.getContext())
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(getResources().getString(R.string.OK), null)
                .create();
        dialog.show();
    }

    public void showAlertDialog(String title, String msg , DialogInterface.OnClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(root.getContext())
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(getResources().getString(R.string.OK), listener)
                .create();
        dialog.show();
    }

}
