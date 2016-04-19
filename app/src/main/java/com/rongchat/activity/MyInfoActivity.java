package com.rongchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.rongchat.R;

/**
 * Created by AMing on 16/3/25.
 * Company RongCloud
 */
public class MyInfoActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout portraitRL, nameRL, numberRL, scannerCodeRL, sexRL, regionRL, myAddressRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_ac_myinfo);
        initViews();

    }

    private void initViews() {
        portraitRL = (RelativeLayout) findViewById(R.id.re_avatar);
        nameRL = (RelativeLayout) findViewById(R.id.re_name);
        numberRL = (RelativeLayout) findViewById(R.id.re_rcid);
        scannerCodeRL = (RelativeLayout) findViewById(R.id.re_scanner_code);
        sexRL = (RelativeLayout) findViewById(R.id.re_sex);
        regionRL = (RelativeLayout) findViewById(R.id.re_region);
        myAddressRL = (RelativeLayout) findViewById(R.id.re_address);
        portraitRL.setOnClickListener(this);
        nameRL.setOnClickListener(this);
        numberRL.setOnClickListener(this);
        scannerCodeRL.setOnClickListener(this);
        sexRL.setOnClickListener(this);
        regionRL.setOnClickListener(this);
        myAddressRL.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_avatar:
                break;
            case R.id.re_name:
                break;
            case R.id.re_rcid:
                break;
            case R.id.re_scanner_code:
                startActivity(new Intent(MyInfoActivity.this,GenerateCodeActivity.class));
                break;
            case R.id.re_sex:
                break;
            case R.id.re_region:
                break;
            case R.id.re_address:
                break;
        }
    }
}
