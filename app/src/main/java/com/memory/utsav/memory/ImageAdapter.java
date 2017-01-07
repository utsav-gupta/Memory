package com.memory.utsav.memory;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by utsav on 05-01-2017.
 */

public class ImageAdapter extends BaseAdapter {
    private final ImageLoader imageLoader;
    private final ArrayList<String> imageUrls;
    private Context mContext;



    public ImageAdapter(Context c, ArrayList<String> imageUrls) {
        mContext = c;
        imageLoader = MyVolleySingleton.getInstance(mContext.getApplicationContext()).getImageLoader();
        this.imageUrls=imageUrls;

    }

    public int getCount() {
        return 9;
    }

    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        NetworkImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new NetworkImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(270, 270));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(4, 4, 4, 4);
            imageView.setDefaultImageResId(R.drawable.def_image);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (NetworkImageView) convertView;
        }
        if(imageUrls.size()>0) {
            imageLoader.get(imageUrls.get(position), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Log.i("image_cached:", String.valueOf(isImmediate));

                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }


            });
            imageView.setImageUrl(imageUrls.get(position), imageLoader);
        }

        return imageView;
    }


}