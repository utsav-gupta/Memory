package com.memory.utsav.memory;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ImagesActivity extends AppCompatActivity {

    private RequestQueue queue;
    private ImageLoader imageLoader;
    private GridView gridview;
    String url = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";
    private TextView mTextField;
    private ArrayList <String> imageUrls = new ArrayList<String>();
    private JSONArray items;
    private ImageAdapter imageAdapter;
    private NetworkImageView imageView;
    private int displayPos;
    private ArrayList<Integer> randomNumbers;
    private int ctr=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mTextField = (TextView) findViewById(R.id.textView2);
        imageView = (NetworkImageView) findViewById(R.id.imageView);
        imageView.setDefaultImageResId(R.drawable.def_image);
        gridview = (GridView) findViewById(R.id.gridView);
        imageAdapter = new ImageAdapter(ImagesActivity.this,imageUrls);
        gridview.setAdapter(imageAdapter);

        randomNumbers = new ArrayList<Integer>();
        for (int i = 0; i <9; i++) randomNumbers.add(i);
        Collections.shuffle(randomNumbers);


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                     items = response.getJSONArray("items");
                    for (int i = 0; i < 9; i++) {
                        imageUrls.add(items.getJSONObject(i).getJSONObject("media").getString("m"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                imageAdapter.notifyDataSetChanged();

                new CountDownTimer(15000, 1000) {   // 15 sec timer to remember images

                    public void onTick(long millisUntilFinished) {

                        mTextField.setText(""+millisUntilFinished / 1000);
                    }

                    public void onFinish() {  //15 sec finished

                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                try {
                                    if(displayPos==position) {  // match found
                                        //flip the image up
                                        imageUrls.set(position, items.getJSONObject(position).getJSONObject("media").getString("m"));
                                        imageAdapter.notifyDataSetChanged();
                                        //if this is not the last image, set e new image to look for
                                        if(ctr<9)
                                            setMainImage();
                                        else  //else you won
                                            mTextField.setText("You won!");

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                        String str = "Find this image on grid below ";
                        mTextField.setText(str);
                        setMainImage();
                        for (int i = 0; i < 9; i++) {
                            imageUrls.set(i,"");
                        }
                        imageAdapter.notifyDataSetChanged();

                    }
                }.start();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        MyVolleySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }

    void setMainImage(){

        displayPos = randomNumbers.get(ctr++);

        try {
            imageView.setImageUrl(items.getJSONObject(displayPos).getJSONObject("media").getString("m"),MyVolleySingleton.getInstance(ImagesActivity.this).getImageLoader());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
