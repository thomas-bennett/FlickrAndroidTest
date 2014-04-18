package com.tombennett.flickrandroidtest;

import java.io.InputStream;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotosInterface;
import com.gmail.yuyang226.flickr.photos.Size;

public class FlickrPreviewImageView extends ImageView {
    private DownloadPhotoPreviewTask mDownloadTask;
    private final int mDefaultImage;

    public FlickrPreviewImageView(Context context, int defaultImage) {
        super(context);
        mDefaultImage = defaultImage;
    }

    public void cancelDownload() {
        mDownloadTask.cancel(true);
    }

    public void setPhoto(Photo photo) {
        setImageResource(mDefaultImage);
        mDownloadTask = new DownloadPhotoPreviewTask();
        mDownloadTask.execute(photo);
    }

    // TODO: Allow to cancel.
    private class DownloadPhotoPreviewTask extends AsyncTask<Photo, Void, Drawable> {

        // TODO: Implement onCanceled.

        @Override
        protected Drawable doInBackground(Photo... params) {
            if (params.length == 0) {
                return null;
            }
            Photo photo = params[0];
            assert (photo != null);

            Flickr flickr = new Flickr(Constants.FLICKR_API_KEY);
            PhotosInterface photosInterface = flickr.getPhotosInterface();
            try {
                InputStream inputStream = photosInterface.getImageAsStream(photo, Size.LARGE);
                return Drawable.createFromStream(inputStream, photo.getId());

            } catch (Exception exception) {
                Log.e(Constants.TAG, exception.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            // TODO: Verify this is the same imageview!
            setImageDrawable(drawable);
        }
    }
}
