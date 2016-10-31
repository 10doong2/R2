package com.doongis.r2.model;

import android.app.Activity;
import android.widget.Toast;

/**
 * 휴대폰의 취소버튼을 누르면 종료가 되도록. (2초안에 두번 누르기)
 */
public class BackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime + 1000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 1000) {
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity,
                "\'뒤로\' 버튼을 한번 더 누르면 종료가 됩니다.",
                Toast.LENGTH_SHORT);
        toast.show();
    }


}

