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
    private Photo mPhoto;
    private DownloadPhotoTask mDownloadPhotoTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String photoId = intent.getStringExtra(INTENT_KEY_PHOTO_ID);
        assert (photoId != null);
        mDownloadPhotoTask = new DownloadPhotoTask(this);
        mDownloadPhotoTask.execute(photoId);
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
            mProgressDialog.setMessage("Wait while loading...");
            mProgressDialog.show();
        }

        @Override
        protected Drawable doInBackground(String... params) {
            assert (params != null);
            assert (params.length > 0);

            try {
                String photoId = params[0];
                Flickr flickr = new Flickr(Constants.FLICKR_API_KEY);
                PhotosInterface photosInterface = flickr.getPhotosInterface();
                mPhoto = photosInterface.getPhoto(photoId);

                Log.d(Constants.TAG, "Need to get data for " + mPhoto.getTitle());
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

            getActionBar().setTitle(mPhoto.getTitle());

            if (drawable != null) {
                ImageView imageView = (ImageView) mActivity.findViewById(R.id.image);
                imageView.setImageDrawable(drawable);
            }
        }
    }

}
