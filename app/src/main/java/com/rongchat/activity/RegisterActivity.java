package com.rongchat.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rongchat.R;
import com.rongchat.network.http.HttpException;
import com.rongchat.response.CheckPhoneResponse;
import com.rongchat.response.CommonResponse;
import com.rongchat.utils.NToast;
import com.rongchat.widget.LoadDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AMing on 16/3/21.
 * Company RongCloud
 */
public class RegisterActivity extends BaseActivity {

    private static final int CHECKPHONE = 1;
    private static final int SENDCODE = 2;

    private EditText et_usernick;
    private EditText et_usertel;
    private EditText et_password;
    private Button btn_register;
    private TextView tv_xieyi;
    private ImageView iv_hide;
    private ImageView iv_show;
    private ImageView iv_photo;

    private String imageName, nickName, userTel, password;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private boolean isSetHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_ac_regist);
        initView();
    }

    private void initView() {
        et_usernick = (EditText) findViewById(R.id.et_usernick);

        et_usertel = (EditText) findViewById(R.id.et_usertel);
        et_password = (EditText) findViewById(R.id.et_password);

        // 监听多个输入框
        et_usernick.addTextChangedListener(new TextChange());
        et_usertel.addTextChangedListener(new TextChange());
        et_password.addTextChangedListener(new TextChange());
        btn_register = (Button) findViewById(R.id.btn_register);
        tv_xieyi = (TextView) findViewById(R.id.tv_xieyi);
        iv_hide = (ImageView) findViewById(R.id.iv_hide);

        iv_show = (ImageView) findViewById(R.id.iv_show);
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        String xieyi = "<font color=" + "\"" + "#AAAAAA" + "\">" + "点击上面的"
                + "\"" + "注册" + "\"" + "按钮,即表示你同意" + "</font>" + "<u>"
                + "<font color=" + "\"" + "#576B95" + "\">" + "《融云融信软件许可及服务协议》"
                + "</font>" + "</u>";

        tv_xieyi.setText(Html.fromHtml(xieyi));
        iv_hide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iv_hide.setVisibility(View.GONE);
                iv_show.setVisibility(View.VISIBLE);
                et_password
                        .setTransformationMethod(HideReturnsTransformationMethod
                                .getInstance());
                // 切换后将EditText光标置于末尾
                CharSequence charSequence = et_password.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }

            }

        });
        iv_show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iv_show.setVisibility(View.GONE);
                iv_hide.setVisibility(View.VISIBLE);
                et_password
                        .setTransformationMethod(PasswordTransformationMethod
                                .getInstance());
                // 切换后将EditText光标置于末尾
                CharSequence charSequence = et_password.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }

        });
        iv_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showCamera();
            }

        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDialog.show(mContext, "正在注册...");

                nickName = et_usernick.getText().toString().trim();
                password = et_password.getText().toString().trim();
                userTel = et_usertel.getText().toString().trim();

                if ((new File("/sdcard/rongchat/" + imageName)).exists()) {
                    isSetHead = true;
                }

                if (nickName.contains(" ")) {
                    NToast.shortToast(mContext, "昵称不可包含空格");
                    LoadDialog.dismiss(mContext);
                    return;
                }


                final AlertDialog dlg = new AlertDialog.Builder(mContext).create();
                dlg.show();
                Window window = dlg.getWindow();
                window.setContentView(R.layout.alertdialog2);
                TextView content = (TextView) window.findViewById(R.id.message);
                content.setText("我们将发送验证码短信到这个号码：\n +86 " + userTel);
                TextView ok = (TextView) window.findViewById(R.id.ok);
                TextView cancel = (TextView) window.findViewById(R.id.cancel);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoadDialog.dismiss(mContext);
                        LoadDialog.show(mContext, "验证手机可用...");
                        request(CHECKPHONE);
                        dlg.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoadDialog.dismiss(mContext);
                        dlg.cancel();
                    }
                });


            }
        });
    }

    @Override
    public Object doInBackground(int requestCode) throws HttpException {
        switch (requestCode) {
            case CHECKPHONE:
                return action.checkPhoneAvailable("86", userTel);
            case SENDCODE:
                return action.sendCode("86", userTel);
        }
        return super.doInBackground(requestCode);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        switch (requestCode) {
            case CHECKPHONE:
                CheckPhoneResponse cprres = (CheckPhoneResponse) result;
                if (cprres.getCode() == 200) {
                    if (cprres.isResult() == true) {
                        request(SENDCODE);
                    } else {
                        LoadDialog.dismiss(mContext);
                        Toast.makeText(mContext, "手机号已被注册", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case SENDCODE:
                CommonResponse scrres = (CommonResponse) result;
                if (scrres.getCode() == 200) {
                    Intent intent = new Intent(RegisterActivity.this, RegisterActivity2.class);
                    intent.putExtra("nickName", nickName);
                    intent.putExtra("userTel", userTel);
                    intent.putExtra("password", password);
                    if (isSetHead) {
                        intent.putExtra("imgHead", "/sdcard/rongchat/" + imageName);
                    }
                    startActivity(intent);
                    finish();
                } else if (scrres.getCode() == 5000) {
                    NToast.shortToast(mContext, "短信发送超过频率限制!");
                }
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case CHECKPHONE:
                LoadDialog.dismiss(mContext);
                Toast.makeText(mContext, "手机号可用验证失败", Toast.LENGTH_SHORT).show();
                break;
            case SENDCODE:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "获取验证码请求失败");
                break;
        }
    }

    // EditText监听器
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

            boolean Sign1 = et_usernick.getText().length() > 0;
            boolean Sign2 = et_usertel.getText().length() > 10;
            boolean Sign3 = et_password.getText().length() > 5;

            if (Sign1 & Sign2 & Sign3) {
                btn_register.setTextColor(0xFFFFFFFF);
                btn_register.setEnabled(true);
            }
            // 在layout文件中，对Button的text属性应预先设置默认值，否则刚打开程序的时候Button是无显示的
            else {
                btn_register.setTextColor(0xFFD0EFC6);
                btn_register.setEnabled(false);
            }
        }

    }

    // 拍照部分
    private void showCamera() {

        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.alertdialog);
        // 为确认按钮添加事件,执行退出应用操作
        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
        tv_paizhao.setText("拍照");
        tv_paizhao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            public void onClick(View v) {

                imageName = getNowTime() + ".png";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定调用相机拍照后照片的储存路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File("/sdcard/rongchat/", imageName)));
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                dlg.cancel();
            }
        });
        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("相册");
        tv_xiangce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getNowTime();
                imageName = getNowTime() + ".png";
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

                dlg.cancel();
            }
        });

    }

    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO:

                    startPhotoZoom(
                            Uri.fromFile(new File("/sdcard/rongchat/", imageName)),
                            200);
                    break;

                case PHOTO_REQUEST_GALLERY:
                    if (data != null)
                        startPhotoZoom(data.getData(), 200);
                    break;

                case PHOTO_REQUEST_CUT:
//                BitmapFactory.Options options = new BitmapFactory.Options();
//
//                /**
//                 * 最关键在此，把options.inJustDecodeBounds = true;
//                 * 这里再decodeFile()，返回的bitmap为空
//                 * ，但此时调用options.outHeight时，已经包含了图片的高了
//                 */
//                options.inJustDecodeBounds = true;
                    Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/rongchat/"
                            + imageName);
                    iv_photo.setImageBitmap(bitmap);

                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    @SuppressLint("SdCardPath")
    private void startPhotoZoom(Uri uri1, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri1, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", false);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File("/sdcard/rongchat/", imageName)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    @SuppressLint("SimpleDateFormat")
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
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
