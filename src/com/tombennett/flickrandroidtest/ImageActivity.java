package com.tombennett.flickrandroidtest;

import java.io.InputStream;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotosInterface;
import com.gmail.yuyang226.flickr.photos.Size;

public class ImageActivity extends Activity {

    public static final String INTENT_KEY_PHOTO_ID = "com.tombennett.flickrandroidtest.ImageActivity.photo_id";
    public static final String INTENT_KEY_PHOTO_SECRET = "com.tombennett.flickrandroidtest.ImageActivity.photo_secret";
    public static final String INTENT_KEY_PHOTO_TITLE = "com.tombennett.flickrandroidtest.ImageActivity.photo_title";

    private Photo mPhoto;
    private DownloadPhotoTask mDownloadPhotoTask;
    private String mPhotoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();
        String photoId = intent.getStringExtra(INTENT_KEY_PHOTO_ID);
        assert (photoId != null);
        // Optional
        String photoSecret = intent.getStringExtra(INTENT_KEY_PHOTO_SECRET);
        mPhotoTitle = intent.getStringExtra(INTENT_KEY_PHOTO_TITLE);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (!Utils.isEmpty(mPhotoTitle)) {
            actionBar.setTitle(mPhotoTitle);
        }

        mDownloadPhotoTask = new DownloadPhotoTask(this);
        mDownloadPhotoTask.execute(photoId, photoSecret);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDownloadPhotoTask.cancel(true);
    }

    private class DownloadPhotoTask extends AsyncTask<String, Void, Drawable> {
        private ProgressDialog mProgressDialog;
        private final Activity mActivity;

        public DownloadPhotoTask(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setTitle("Loading");
            String imageTitle = (Utils.isEmpty(mPhotoTitle)) ? "image" : mPhotoTitle;
            mProgressDialog.setMessage(String.format("Loading %s ...", imageTitle));
            mProgressDialog.show();
        }

        @Override
        protected Drawable doInBackground(String... params) {
            assert (params != null);
            assert (params.length > 0);

            try {
                String photoId = params[0];
                String photoSecret = params[1];
                Flickr flickr = new Flickr(Constants.FLICKR_API_KEY);
                PhotosInterface photosInterface = flickr.getPhotosInterface();

                mPhoto = photosInterface.getPhoto(photoId, photoSecret);
                mPhotoTitle = mPhoto.getTitle();

                Log.d(Constants.TAG, "Fetching data for " + mPhotoTitle);
                photosInterface.getImageAsStream(mPhoto, Size.ORIGINAL);
                InputStream inputStream = photosInterface.getImageAsStream(mPhoto, Size.LARGE);
                return Drawable.createFromStream(inputStream, photoId);

            } catch (Exception exception) {
                Log.e(Constants.TAG, exception.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            mProgressDialog.dismiss();

            if (drawable != null) {
                ImageView imageView = (ImageView) mActivity.findViewById(R.id.image);
                imageView.setImageDrawable(drawable);
                getActionBar().setTitle(mPhotoTitle);
                imageView.setContentDescription(mPhotoTitle);
            }
        }
    }

}
