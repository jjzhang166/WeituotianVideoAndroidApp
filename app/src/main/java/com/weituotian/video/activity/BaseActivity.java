package com.weituotian.video.activity;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;

import com.weituotian.video.R;

/**
 * Created by ange on 2017/3/12.
 */

public class BaseActivity extends AppCompatActivity {

    public Toolbar mToolbar;

    protected <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected int[] getScreenSize() {
        int screenSize[] = new int[2];
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenSize[0] = displayMetrics.widthPixels;
        screenSize[1] = displayMetrics.heightPixels;
        return screenSize;
    }

}
