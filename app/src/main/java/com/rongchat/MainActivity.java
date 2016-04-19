package com.rongchat;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rongchat.activity.BaseActivity;
import com.rongchat.fragment.ContactFragment;
import com.rongchat.fragment.FoundFragment;
import com.rongchat.fragment.MineFragment;
import com.rongchat.widget.AddPopWindow;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private FragmentPagerAdapter mFragmentPagerAdapter; //将 tab  页面持久在内存中

    private ViewPager mViewPager;

    private Fragment mConversationList;

    private List<Fragment> mFragment = new ArrayList<>();

    private RelativeLayout chatRLayout, contactRLayout, foundRLayout, mineRLayout;

    private ImageView moreImage , searchImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initMianViewPager();
    }

    private void initViews() {
        chatRLayout = (RelativeLayout) findViewById(R.id.re_chat);
        contactRLayout = (RelativeLayout) findViewById(R.id.re_contact_list);
        foundRLayout = (RelativeLayout) findViewById(R.id.re_find);
        mineRLayout = (RelativeLayout) findViewById(R.id.re_profile);
        moreImage = (ImageView) findViewById(R.id.rc_more);
        searchImage = (ImageView) findViewById(R.id.rc_search);
        chatRLayout.setOnClickListener(this);
        contactRLayout.setOnClickListener(this);
        foundRLayout.setOnClickListener(this);
        mineRLayout.setOnClickListener(this);
        moreImage.setOnClickListener(this);
        searchImage.setOnClickListener(this);
    }


    private void initMianViewPager() {
        mConversationList = initConversationList();
        mViewPager = (ViewPager) findViewById(R.id.rc_viewpager);
        mFragment.add(mConversationList);
        mFragment.add(ContactFragment.getInstance());
        mFragment.add(FoundFragment.getInstance());
        mFragment.add(MineFragment.getInstance());
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(this);
    }


    private Fragment initConversationList() {
        if (mConversationList == null) {
            ConversationListFragment listFragment = ConversationListFragment.getInstance();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false") //设置私聊会话是否聚合显示
                    .build();
            listFragment.setUri(uri);
            return listFragment;
        } else {
            return mConversationList;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_chat:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.re_contact_list:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.re_find:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.re_profile:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.rc_more:
                AddPopWindow addPopWindow = new AddPopWindow(MainActivity.this);
                addPopWindow.showPopupWindow(moreImage);
                break;

            case R.id.rc_search:

                break;
        }
    }
}
