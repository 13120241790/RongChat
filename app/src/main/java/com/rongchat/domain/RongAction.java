package com.rongchat.domain;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.rongchat.network.http.HttpException;
import com.rongchat.request.CheckPhoneRequest;
import com.rongchat.request.LoginRequest;
import com.rongchat.request.RegisterRequest;
import com.rongchat.request.SendCodeRequest;
import com.rongchat.request.VerifyCodeRequest;
import com.rongchat.response.CheckPhoneResponse;
import com.rongchat.response.CommonResponse;
import com.rongchat.response.GetTokenResponse;
import com.rongchat.response.LoginResponse;
import com.rongchat.response.RegisterResponse;
import com.rongchat.response.VerifyCodeResponse;
import com.rongchat.utils.NLog;
import com.rongchat.utils.json.JsonMananger;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by AMing on 16/3/18.
 * Company RongCloud
 */
public class RongAction extends BaseAction {

    private final String CONTENTTYPE = "application/json";
    private final String ENCODING = "utf-8";

    /**
     * 构造方法
     *
     * @param context
     */
    public RongAction(Context context) {
        super(context);
    }

    /**
     * 检查手机是否被注册
     *
     * @param region
     * @param phone
     * @return
     * @throws HttpException
     */
    public CheckPhoneResponse checkPhoneAvailable(String region, String phone) throws HttpException {
        String url = getURL("user/check_phone_available");
        String json = JsonMananger.beanToJson(new CheckPhoneRequest(phone, region));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENTTYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CheckPhoneResponse response = null;
        String result = httpManager.post(mContext, url, entity, CONTENTTYPE);
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, CheckPhoneResponse.class);
        }
        return response;
    }


    /**
     * 发送验证码
     *
     * @param region
     * @param phone
     * @return
     * @throws HttpException
     */
    public CommonResponse sendCode(String region, String phone) throws HttpException {
        String url = getURL("user/send_code");
        String json = JsonMananger.beanToJson(new SendCodeRequest(region, phone));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENTTYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CommonResponse response = null;
        String result = httpManager.post(mContext, url, entity, CONTENTTYPE);
        if (!TextUtils.isEmpty(result)) {
            response = JsonMananger.jsonToBean(result, CommonResponse.class);
        }
        return response;
    }


          /*
    * 200: 验证成功
    1000: 验证码错误
    2000: 验证码过期
    异常返回，返回的 HTTP Status Code 如下：

    400: 错误的请求
    500: 应用服务器内部错误
    * */

    /**
     * 验证验证码是否正确(必选先用手机号码调sendcode)
     *
     * @param region
     * @param phone
     * @return
     * @throws HttpException
     */
    public VerifyCodeResponse verifyCode(String region, String phone, String code) throws HttpException {
        String url = getURL("user/verify_code");
        String josn = JsonMananger.beanToJson(new VerifyCodeRequest(region, phone, code));
        VerifyCodeResponse response = null;
        StringEntity entity = null;
        try {
            entity = new StringEntity(josn, ENCODING);
            entity.setContentType(CONTENTTYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENTTYPE);
        if (!TextUtils.isEmpty(result)) {
            Log.e("VerifyCodeResponse", result);
            response = jsonToBean(result, VerifyCodeResponse.class);
        }
        return response;
    }

    /**
     * 注册
     *
     * @param nickname
     * @param password
     * @param
     * @return
     * @throws HttpException
     */
    public RegisterResponse register(String nickname, String password, String verification_token) throws HttpException {
        String url = getURL("user/register");
        StringEntity entity = null;
        try {
            entity = new StringEntity(JsonMananger.beanToJson(new RegisterRequest(nickname, password, verification_token)), ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RegisterResponse response = null;
        String result = httpManager.post(mContext, url, entity, CONTENTTYPE);
        if (!TextUtils.isEmpty(result)) {
            NLog.e("RegisterResponse", result);
            response = jsonToBean(result, RegisterResponse.class);
        }
        return response;
    }


    /**
     * 登录: 登录成功后，会设置 Cookie，后续接口调用需要登录的权限都依赖于 Cookie。
     *
     * @param region
     * @param phone
     * @param password
     * @return
     * @throws HttpException
     */
    public LoginResponse login(String region, String phone, String password) throws HttpException {
        String uri = getURL("user/login");
        String json = JsonMananger.beanToJson(new LoginRequest(region, phone, password));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENTTYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, uri, entity, CONTENTTYPE);
        LoginResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("LoginResponse", result);
            response = JsonMananger.jsonToBean(result, LoginResponse.class);
        }
        return response;
    }

    /**
     * 获取 token 前置条件需要登录   502 坏的网关 测试环境用户已达上限
     *
     * @return
     * @throws HttpException
     */
    public GetTokenResponse getToken() throws HttpException {
        String url = getURL("user/get_token");
        String result = httpManager.get(url);
        GetTokenResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("GetTokenResponse", result+"-----------");
            response = jsonToBean(result, GetTokenResponse.class);
        }
        return response;
    }
}
