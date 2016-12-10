package cs.petrsu.ru.imitnews;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import cs.petrsu.ru.imitnews.remote.ImageDrawableLoader;

/**
 * Created by Kovalchuk Denis on 10.12.16.
 * Email: deniskk25@gmail.com
 */

public class ImageFragment extends DialogFragment {
    private static final String TAG = "DialogFragment";
    private static String ARG_PHOTO_URL = "photo_url";
    private static int IMAGE_LOADER = 0;
    private String url;

    private ImageView imageView;

    public static ImageFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_URL, url);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_image, null);

        imageView = (ImageView) v.findViewById(R.id.dialog_image_view);
        url = getArguments().getString(ARG_PHOTO_URL);

//        try {
//            //imageView.setImageDrawable(drawableFromUrl(url));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("123")
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
