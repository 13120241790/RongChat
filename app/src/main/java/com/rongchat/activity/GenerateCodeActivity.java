package com.rongchat.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.rongchat.R;
import com.rongchat.scanner.encoding.EncodingHandler;

/**
 * Created by AMing on 16/3/25.
 * Company RongCloud
 */
public class GenerateCodeActivity extends BaseActivity {

    private TextView codeName;

    private ImageView codeHead, codeImage;

    private SharedPreferences sp;

    private String myID;

    private static final String friendLogo = "RCfriend@";

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_ac_generate);
        initViews();
        myID = sp.getString("loginid", "");
        String str = friendLogo + myID;
        try {
            bitmap = EncodingHandler.createQRCode(str, 240);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            codeImage.setImageBitmap(bitmap);
        }
    }

    private void initViews() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        codeName = (TextView) findViewById(R.id.code_name);
        codeHead = (ImageView) findViewById(R.id.code_head);
        codeImage = (ImageView) findViewById(R.id.iv_scanner_code);
    }
}
