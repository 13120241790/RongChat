package com.rongchat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rongchat.MainActivity;
import com.rongchat.R;
import com.rongchat.network.http.HttpException;
import com.rongchat.response.GetTokenResponse;
import com.rongchat.response.LoginResponse;
import com.rongchat.utils.NLog;
import com.rongchat.utils.NToast;
import com.rongchat.widget.LoadDialog;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


/**
 * Created by AMing on 16/3/18.
 * Company RongCloud
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final int LOGIN = 6;
    private EditText et_usertel;
    private EditText et_password;
    private Button btn_login;
    private Button btn_qtlogin;

    private SharedPreferences sp;

    SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_ac_login);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        e = sp.edit();
        et_usertel = (EditText) findViewById(R.id.et_usertel);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_qtlogin = (Button) findViewById(R.id.btn_qtlogin);
        btn_login.setOnClickListener(this);
        et_usertel.addTextChangedListener(new TextChange());
        et_password.addTextChangedListener(new TextChange());
        btn_qtlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,
                        RegisterActivity.class));
            }

        });

        registData();

    }

    private void registData() {
        String phone = getIntent().getStringExtra("phone");
        String password = getIntent().getStringExtra("password");
        String nickname = getIntent().getStringExtra("nickname");
        String id = getIntent().getStringExtra("id");
        headuri = getIntent().getStringExtra("headuri");
        NLog.e("phone:" + phone + "password:" + password + "nickname:" + nickname + "id:" + id + "headuri" + headuri);
        et_usertel.setText(phone);
        et_password.setText(password);
    }

    private String phoneString, passwordString, loginToken, connectResultId, headuri;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                phoneString = et_usertel.getText().toString().trim();
                passwordString = et_password.getText().toString().trim();

                if (TextUtils.isEmpty(phoneString)) {
                    NToast.shortToast(mContext, "手机号不能为空");
                    return;
                }

                if (TextUtils.isEmpty(passwordString)) {
                    NToast.shortToast(mContext, "密码不能为空");
                    return;
                }
                if (passwordString.contains(" ")) {
                    NToast.shortToast(mContext, "密码不能包含空格");
                    return;
                }
                LoadDialog.show(mContext, "正在登录...");
                request(LOGIN);
                break;
        }
    }

    @Override
    public Object doInBackground(int requestCode) throws HttpException {
        switch (requestCode) {
            case LOGIN:
                return action.login("86", phoneString, passwordString);
        }
        return super.doInBackground(requestCode);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case LOGIN:
                    LoginResponse lrres = (LoginResponse) result;
                    if (lrres.getCode() == 200) {
                        loginToken = lrres.getResult().getToken();
                        if (!TextUtils.isEmpty(loginToken)) {
                            RongIM.connect(loginToken, new RongIMClient.ConnectCallback() {
                                @Override
                                public void onTokenIncorrect() {
                                    NLog.e("connect", "onTokenIncorrect");
                                }

                                @Override
                                public void onSuccess(String s) {
                                    connectResultId = s;
                                    NLog.e("connect", "onSuccess userid:" + s);
                                    e.putString("loginid", s);
                                    e.putString("loginToken", loginToken);
                                    e.putString("loginphone", phoneString);
                                    e.putString("loginpassword", passwordString);
                                    if (TextUtils.isEmpty(headuri)) {
                                        e.putString("headuri", headuri);
                                    }
                                    e.commit();

                                    LoadDialog.dismiss(mContext);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    NToast.shortToast(mContext, "login success!");
                                    finish();

                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    NLog.e("connect", "onError errorcode:" + errorCode.getValue());
                                }
                            });
                        }

                    } else if (lrres.getCode() == 100) {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, "手机或者密码错误");
                    } else if (lrres.getCode() == 1000) {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, "手机或者密码错误");
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        super.onFailure(requestCode, state, result);
    }

    class TextChange implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before,
                                  int count) {

            boolean Sign2 = et_usertel.getText().length() == 11;
            boolean Sign3 = et_password.getText().length() > 5;

            if (Sign2 & Sign3) {
                btn_login.setTextColor(0xFFFFFFFF);
                btn_login.setEnabled(true);
            }
            // 在layout文件中，对Button的text属性应预先设置默认值，否则刚打开程序的时候Button是无显示的
            else {
                btn_login.setTextColor(0xFFD0EFC6);
                btn_login.setEnabled(false);
            }
        }

    }


}
