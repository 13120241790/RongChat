package com.rongchat.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.rongchat.R;

/**
 * Created by AMing on 16/4/19.
 * Company RongCloud
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        logout = (RelativeLayout) findViewById(R.id.logout);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:

                break;
        }
    }
}
