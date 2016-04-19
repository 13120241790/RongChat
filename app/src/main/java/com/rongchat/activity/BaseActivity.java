package com.rongchat.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.rongchat.domain.RongAction;
import com.rongchat.network.async.AsyncTaskManager;
import com.rongchat.network.async.OnDataListener;
import com.rongchat.network.http.HttpException;

/**
 * Created by AMing on 16/3/18.
 * Company RongCloud
 */
public class BaseActivity extends FragmentActivity implements OnDataListener{

    protected Context mContext;
    private AsyncTaskManager mAsyncTaskManager;
    protected RongAction action;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        mAsyncTaskManager = AsyncTaskManager.getInstance(mContext);
        // Activity管理
        action = new RongAction(mContext);
    }


    /**
     * 发送请求（需要检查网络）
     *
     * @param requsetCode 请求码
     */
    public void request(int requsetCode) {
        if (mAsyncTaskManager != null) {
            mAsyncTaskManager.request(requsetCode, this);
        }
    }

    /**
     * 发送请求
     *
     * @param requsetCode    请求码
     * @param isCheckNetwork 是否需检查网络，true检查，false不检查
     */
    public void request(int requsetCode, boolean isCheckNetwork) {
        if (mAsyncTaskManager != null) {
            mAsyncTaskManager.request(requsetCode, isCheckNetwork, this);
        }
    }

    /**
     * 取消所有请求
     */
    public void cancelRequest() {
        if (mAsyncTaskManager != null) {
            mAsyncTaskManager.cancelRequest();
        }
    }

    @Override
    public Object doInBackground(int requestCode) throws HttpException {
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {

    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {

    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }
}
