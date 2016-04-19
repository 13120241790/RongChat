package com.rongchat.circle;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rongchat.R;
import com.rongchat.activity.BaseActivity;
import com.rongchat.circle.pull.NoScrollerListView;
import com.rongchat.circle.pull.PullToRefreshBase;
import com.rongchat.circle.pull.PullToRefreshScrollView;
import com.rongchat.utils.picture.MultiImageSelectorActivity;


/**
 * Created by AMing on 16/3/28.
 * Company RongCloud
 */
public class CircleOfFriendsActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ScrollView>, View.OnClickListener {

    private NoScrollerListView ListView;

    private FriendAdapter adapter;

    private PullToRefreshScrollView refreshScrollView;

    private ImageView circle, camera, photobg;

    private RelativeLayout headView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_ac_circle);

        photobg = (ImageView) findViewById(R.id.photo_bg);
        camera = (ImageView) findViewById(R.id.iv_camera);
        headView = (RelativeLayout) findViewById(R.id.headview);
        circle = (ImageView) headView.findViewById(R.id.circleprogress);
        ListView = (NoScrollerListView) findViewById(R.id.pulllistview);
        refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshScrollView);
        camera.setOnClickListener(this);
        photobg.setOnClickListener(this);
        refreshScrollView.setOnRefreshListener(this);
        refreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        adapter = new FriendAdapter();
        ListView.setAdapter(adapter);


    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.criclefriend);
        operatingAnim.setInterpolator(new LinearInterpolator());
        operatingAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                circle.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        circle.setVisibility(View.VISIBLE);
        circle.startAnimation(operatingAnim);


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        refreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera:
                final AlertDialog dlg = new AlertDialog.Builder(this).create();
                dlg.show();
                Window window = dlg.getWindow();
                window.setContentView(R.layout.alertdialog4);
                TextView text = (TextView) window.findViewById(R.id.friendship_content1);
                TextView photo = (TextView) window.findViewById(R.id.friendship_content2);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CircleOfFriendsActivity.this, PublishFriendshipActivity.class));
                        dlg.cancel();
                    }
                });
                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CircleOfFriendsActivity.this, MultiImageSelectorActivity.class));
                        dlg.cancel();
                    }
                });
                break;
            case R.id.photo_bg:
                final AlertDialog dlg1 = new AlertDialog.Builder(this).create();
                dlg1.show();
                Window window1 = dlg1.getWindow();
                window1.setContentView(R.layout.alertdialog5);
                TextView changebg = (TextView) window1.findViewById(R.id.changebg);
                changebg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg1.cancel();
                    }
                });
                break;
        }

    }

    class FriendAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
