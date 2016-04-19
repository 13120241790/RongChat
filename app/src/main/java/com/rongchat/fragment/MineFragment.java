package com.rongchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rongchat.R;
import com.rongchat.activity.MyInfoActivity;

/**
 * Created by AMing on 16/3/24.
 * Company RongCloud
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    public static MineFragment instance = null;

    public static MineFragment getInstance() {
        if (instance == null) {
            instance = new MineFragment();
        }
        return instance;
    }

    private RelativeLayout myInfo;

    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.rs_mine_fragment, null);
        myInfo = (RelativeLayout) mView.findViewById(R.id.re_myinfo);
        myInfo.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_myinfo:
                startActivity(new Intent(getActivity(),MyInfoActivity.class));
                break;
        }
    }
}
