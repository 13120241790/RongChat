package com.rongchat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.rongchat.MainActivity;
import com.rongchat.R;
import com.rongchat.network.http.HttpException;
import com.rongchat.response.LoginResponse;
import com.rongchat.utils.NLog;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by AMing on 16/3/24.
 * Company RongCloud
 */
public class WelcomeActivity extends BaseActivity {

    private static final int LOGIN = 7;
    private SharedPreferences sp;

    SharedPreferences.Editor e;

    private String cachePhone, cachePassword, loginToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_ac_welcome);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        e = sp.edit();


        cachePhone = sp.getString("loginphone", "");
        cachePassword = sp.getString("loginpassword", "");
        if (TextUtils.isEmpty(cachePhone) && TextUtils.isEmpty(cachePassword)) {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                }
            }, 1000);

        } else {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    request(LOGIN);
                }
            }, 500);
        }
    }

    @Override
    public Object doInBackground(int requestCode) throws HttpException {
        switch (requestCode) {
            case LOGIN:
                return action.login("86", cachePhone, cachePassword);

        }
        return super.doInBackground(requestCode);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        switch (requestCode) {
            case LOGIN:
                LoginResponse lrres = (LoginResponse) result;
                if (lrres.getCode() == 200) {
                    loginToken = lrres.getResult().getToken();
                    RongIM.connect(loginToken, new RongIMClient.ConnectCallback() {
                        @Override
                        public void onTokenIncorrect() {
                            NLog.e("connect", "onTokenIncorrect");
                        }

                        @Override
                        public void onSuccess(String s) {
                            NLog.e("connect", "onSuccess userid:" + s);
                            e.putString("loginid", s);
                            e.commit();
                            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                            finish();

                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            NLog.e("connect", "onError errorcode:" + errorCode.getValue());
                        }
                    });
                }
                break;
        }
    }


    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case LOGIN:
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
}
