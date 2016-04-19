package com.rongchat.circle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.rongchat.R;
import com.rongchat.activity.BaseActivity;
import com.rongchat.utils.picture.MultiImageSelectorActivity;
import com.rongchat.widget.HorizontalListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by AMing on 16/3/29.
 * Company RongCloud
 */
public class PublishFriendshipActivity extends BaseActivity {

    private HorizontalListView publishPhoto;

    private ArrayList<String> fromPhotoData;

    private ArrayList<String> fromPerview;

    private PublishPhotoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_ac_publish);
        publishPhoto = (HorizontalListView) findViewById(R.id.publish_photo);
        fromPhotoData = getIntent().getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
        fromPerview = getIntent().getStringArrayListExtra("PREVIEW_RESULT");

        if (fromPhotoData != null) {
            adapter = new PublishPhotoAdapter(fromPhotoData);
            publishPhoto.setAdapter(adapter);
        } else if (fromPerview != null) {
            adapter = new PublishPhotoAdapter(fromPerview);
            publishPhoto.setAdapter(adapter);
        } else {
            publishPhoto.setVisibility(View.GONE);
        }
    }

    public class PublishPhotoAdapter extends BaseAdapter {

        private ArrayList<String> photoList;

        public PublishPhotoAdapter(ArrayList<String> photoList) {
            this.photoList = photoList;
        }

        @Override
        public int getCount() {
            return photoList.size();
        }

        @Override
        public Object getItem(int position) {
            return photoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            String path = photoList.get(position);
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.rc_item_publishphoto, null);
                holder.imageView = (ImageView) convertView.findViewById(R.id.item_photo_publish);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            Picasso.with(mContext)
                    .load("file://"+path)
                    .placeholder(R.drawable.de_pic_default_error)
                    .resize(80,80)
                    .centerCrop()
                    .into(holder.imageView);
            return convertView;
        }

        class Holder {
            ImageView imageView;
        }
    }
}
