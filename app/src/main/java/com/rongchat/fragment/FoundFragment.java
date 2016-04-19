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
import com.rongchat.circle.CircleOfFriendsActivity;
import com.rongchat.scanner.CaptureActivity;
import com.rongchat.utils.NToast;

/**
 * Created by AMing on 16/3/24.
 * Company RongCloud
 */
public class FoundFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout friendship, scanningCode;

    public static FoundFragment instance = null;

    public static FoundFragment getInstance() {
        if (instance == null) {
            instance = new FoundFragment();
        }
        return instance;
    }

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.rs_found_fragment, null);
        friendship = (RelativeLayout) mView.findViewById(R.id.re_friendship);
        scanningCode = (RelativeLayout) mView.findViewById(R.id.rc_scanningcode);
        friendship.setOnClickListener(this);
        scanningCode.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_friendship:
                startActivity(new Intent(getActivity(), CircleOfFriendsActivity.class));
                break;
            case R.id.rc_scanningcode:
                startActivity(new Intent(getActivity(), CaptureActivity.class));
                break;
        }
    }
}
