package com.example.financepro;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Dialog;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.Window;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.view.Gravity;
import androidx.fragment.app.DialogFragment;

public class MyDialog extends DialogFragment {

    private int viewValue;

    interface DisplayOptionListener{
        public void onDisplayOptionChanged();
    }
    DisplayOptionListener displayOptionListener;
    public MyDialog(DisplayOptionListener listener){
        this.displayOptionListener = listener;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View subView = layoutInflater.inflate(R.layout.my_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(subView);
        RadioGroup radioGroup = (RadioGroup) subView.findViewById(R.id.view_mode);
        radioGroup.check(MainActivity.viewValue);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                MainActivity.viewValue = i;
                displayOptionListener.onDisplayOptionChanged();
            }
        });

        return builder.create();
    }


    @Override
    public void onResume() {
        // Set the width of the dialog proportional to x% of the screen width
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.80), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

}
