package com.rongchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rongchat.R;

/**
 * Created by AMing on 16/3/24.
 * Company RongCloud
 */
public class ContactFragment extends Fragment {

    public static ContactFragment instance = null;

    public static ContactFragment getInstance() {
        if (instance == null) {
            instance = new ContactFragment();
        }
        return instance;
    }

    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.rs_contact_fragment, null);

        return mView;
    }
}
