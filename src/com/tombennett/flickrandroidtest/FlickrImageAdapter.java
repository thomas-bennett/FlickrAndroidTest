package com.tombennett.flickrandroidtest;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;

public class FlickrImageAdapter extends BaseAdapter {

    private final PhotoList mPhotoList;
    private final Context mContext;

    public FlickrImageAdapter(Context context, PhotoList photoList) {
        mContext = context;
        mPhotoList = photoList;
    }

    @Override
    public int getCount() {
        return mPhotoList.getTotal();
    }

    @Override
    public Object getItem(int position) {
        return mPhotoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FlickrPreviewImageView imageView;
        if (convertView == null) {
            imageView = new FlickrPreviewImageView(mContext, R.drawable.gallery_standin_image);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            // TODO: Don't hardcode these!
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (FlickrPreviewImageView) convertView;
            imageView.cancelDownload();
        }

        final Photo photo = mPhotoList.get(position);
        imageView.setPhoto(photo);
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ImageActivity.class);
                intent.putExtra(ImageActivity.INTENT_KEY_PHOTO_ID, photo.getId());
                intent.putExtra(ImageActivity.INTENT_KEY_PHOTO_SECRET, photo.getSecret());
                intent.putExtra(ImageActivity.INTENT_KEY_PHOTO_TITLE, photo.getTitle());
                mContext.startActivity(intent);
            }

        });

        return imageView;
    }
}
