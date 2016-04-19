package com.rongchat.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rongchat.R;
import com.rongchat.network.http.HttpException;
import com.rongchat.response.CommonResponse;
import com.rongchat.response.RegisterResponse;
import com.rongchat.response.VerifyCodeResponse;
import com.rongchat.utils.NToast;
import com.rongchat.utils.downtime.DownTimer;
import com.rongchat.utils.downtime.DownTimerListener;
import com.rongchat.widget.LoadDialog;

/**
 * Created by AMing on 16/3/21.
 * Company RongCloud
 */
public class RegisterActivity2 extends BaseActivity implements DownTimerListener, View.OnClickListener {

    private static final int RESENDCODE = 3;
    private static final int VERIFYCODE = 4;
    private static final int REGISTER = 5;
    private static final int REGIST_BACK = 1001;
    private TextView showPhone, timeState;

    private EditText code;

    private Button next;

    private String nickName, userTel, password, imgHead;

    private DownTimer downTimer;

    private String mCodeToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_ac_regist2);
        nickName = getIntent().getStringExtra("nickName");
        userTel = getIntent().getStringExtra("userTel");
        password = getIntent().getStringExtra("password");
        imgHead = getIntent().getStringExtra("imgHead");

        showPhone = (TextView) findViewById(R.id.show_phone);
        timeState = (TextView) findViewById(R.id.timeState);
        code = (EditText) findViewById(R.id.et_code);
        next = (Button) findViewById(R.id.btn_next);
        next.setOnClickListener(this);
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    next.setTextColor(0xFFFFFFFF);
                    next.setEnabled(true);
                } else {
                    next.setTextColor(0xFFD0EFC6);
                    next.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        showPhone.setText("+86 " + userTel);

        downTimer = new DownTimer();
        downTimer.setListener(this);
        downTimer.startDown(60 * 1000);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timeState.setText("接收短信大约需要" + String.valueOf(millisUntilFinished / 1000) + "秒钟");
        timeState.setTextColor(getResources().getColor(R.color.btn_gray_pressed));
        timeState.setClickable(false);
    }

    @Override
    public void onFinish() {
        timeState.setText("收不到验证码?");
        timeState.setTextColor(getResources().getColor(R.color.text_color));
        timeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dlg = new AlertDialog.Builder(mContext).create();
                dlg.show();
                Window window = dlg.getWindow();
                window.setContentView(R.layout.alertdialog3);
                TextView tv = (TextView) window.findViewById(R.id.get_code);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        request(RESENDCODE);

                        downTimer.startDown(60 * 1000);
                        dlg.cancel();
                    }
                });
            }
        });
    }

    @Override
    public Object doInBackground(int requestCode) throws HttpException {
        switch (requestCode) {
            case RESENDCODE:
                return action.sendCode("86", userTel);
            case VERIFYCODE:
                return action.verifyCode("86", userTel, codeString);
            case REGISTER:
                return action.register(nickName, password, mCodeToken);
        }
        return super.doInBackground(requestCode);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        switch (requestCode) {
            case RESENDCODE:
                CommonResponse scrres = (CommonResponse) result;
                if (scrres.getCode() == 200) {
                    NToast.shortToast(mContext, "已发送验证码");
                } else if (scrres.getCode() == 5000) {
                    NToast.shortToast(mContext, "短信发送超过频率限制!");
                } else if (scrres.getCode() == 2000) {
                    NToast.shortToast(mContext, "验证码已过期");
                    LoadDialog.dismiss(mContext);
                }
                break;
            case VERIFYCODE:
                VerifyCodeResponse vcres = (VerifyCodeResponse) result;
                switch (vcres.getCode()) {
                    case 200:
                        mCodeToken = vcres.getResult().getVerification_token();
                        if (!TextUtils.isEmpty(mCodeToken)) {
                            request(REGISTER);
                        } else {
                            NToast.shortToast(mContext, "code token is null");
                            LoadDialog.dismiss(mContext);
                        }
                        break;
                    case 1000:
                        //验证码错误
                        NToast.shortToast(mContext, "验证码错误");
                        LoadDialog.dismiss(mContext);
                        break;
                    case 2000:
                        //验证码过期
                        NToast.shortToast(mContext, "验证码过期请重新请求");
                        LoadDialog.dismiss(mContext);
                        break;
                }
                break;
            case REGISTER:
                RegisterResponse rres = (RegisterResponse) result;
                switch (rres.getCode()) {
                    case 200:
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, "注册成功!");
                        Intent data = new Intent(RegisterActivity2.this, LoginActivity.class);
                        data.putExtra("phone", userTel);
                        data.putExtra("password", password);
                        data.putExtra("nickname", nickName);
                        data.putExtra("id", rres.getResult().getId());
                        data.putExtra("headuri", imgHead);
                        startActivity(data);
                        this.finish();
                        break;
                    case 400:
                        // 错误的请求
                        break;
                    case 404:
                        //token 不存在
                        break;
                    case 500:
                        //应用服务端内部错误
                        break;
                }
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        super.onFailure(requestCode, state, result);
    }

    private String codeString;

    @Override
    public void onClick(View v) {
        codeString = code.getText().toString().trim();
        //nickName, userTel, password, imgHead;
        if (TextUtils.isEmpty(nickName) && TextUtils.isEmpty(userTel) && TextUtils.isEmpty(password)) {
            NToast.shortToast(mContext, "注册数据昵称，手机或密码为空");
            return;
        }
        if (TextUtils.isEmpty(codeString)) {
            NToast.shortToast(mContext, "验证码不能为空");
            return;
        }
        LoadDialog.show(mContext, "正在注册...");
        request(VERIFYCODE);
    }
}
