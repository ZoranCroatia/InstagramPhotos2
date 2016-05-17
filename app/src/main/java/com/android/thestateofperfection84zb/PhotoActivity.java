package com.android.thestateofperfection84zb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * This class shows ListView populated with the data fetched from the Instagram.
 *
 * @author       Zoran Bosanac
 * @version      1.0, 17. May 2016.
 */
public class PhotoActivity extends AppCompatActivity {

    /**
     * Instructions on how to choose endpoint and how to get ACCESS-TOKEN from Instagram:
     * 1. You need to open account on the: https://www.instagram.com/
     * 2. Open https://apigee.com/console/instagram and open "Authentication" and give authentication
     * 3. I used the following endpoint media search (with different latitude and longitude) from
     * the Instagram API:
     * https://api.instagram.com/v1/media/search?lat=48.858844&lng=2.294351&access_token=ACCESS-TOKEN
     * 4. The latitude of Empire State Building, New York, NY, USA is 40.748817, and the longitude
     * is -73.985428. Search distance is 5000 meters
     * 5. So here is the full endpoint media search that I used:
     * https://api.instagram.com/v1/media/search?access_token=3157986452.1fb234f.6e5fbb8cab404f24b9e689b0c67e44a3&lat=40.748817&lng=73.985428&distance=5000
     * It is copy-pasted, the principle is: https://api.instagram.com PLUS
     * /v1/media/search?access_token=3157986452.1fb234f.6e5fbb8cab404f24b9e689b0c67e44a3&lat=40.748817&lng=-73.985428&distance=5000
     * HTTP/1.1 must be omitted
     */
    final static String URL =
            "https://api.instagram.com/v1/media/search?access_token=3157986452.1fb234f.6e5fbb8cab404f24b9e689b0c67e44a3&lat=40.748817&lng=73.985428&distance=5000";
    List<PhotoResource> photos;
    PhotoAdapter photoAdapter;
    @Bind(R.id.lvPhotos)
    ListView listView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        photos = new ArrayList<>();

        // Setup refresh listener which triggers new data loading
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPhotos(true);
            }
        });
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fetchPhotos(false);

        photoAdapter = new PhotoAdapter(this, photos);
        listView.setAdapter(photoAdapter);

    }

    public void fetchPhotos(final boolean isRefreshed) {
        if (isRefreshed) {
            photoAdapter.clear();
        }

        // Create the network client
        AsyncHttpClient httpClient = new AsyncHttpClient();

        // Trigger the GET request
        httpClient.get(URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    JSONArray dataArray = response.getJSONArray("data"); // array of posts
                    // iterate array of posts
                    for (int i = 0; i < dataArray.length(); i++) {

                        // get the JSON object at that position
                        JSONObject obj = dataArray.getJSONObject(i);

                        // We need this object because we will decode the attributes of the JSON
                        // into a Java object
                        PhotoResource photo = new PhotoResource();

                        // Created time: { "data" => [array of items] => "created_time" }
                        photo.setCreatedTime(Long.parseLong(obj.optString("created_time")));

                        // URL: { "data" => [array of items] => "images" => "standard_resolution" => "url" }
                        JSONObject image = obj.optJSONObject("images");
                        JSONObject resolution = image.optJSONObject("standard_resolution");
                        photo.setImages(resolution.optString("url"));

                        // Caption: { "data" => [array of items] => "caption" => "text" }
                        JSONObject caption = obj.optJSONObject("caption");
                        // Caption is not mandatory, that is the reason why we need to check if
                        // the object exists first
                        if (caption != null) {
                            photo.setCaption(caption.optString("text"));
                        }

                        // Author Name: { "data" => [array of items] => "user" => "username" }
                        // Author Profile URL: { "data" => [array of items] => "user" => "profile_picture" }
                        JSONObject user = obj.optJSONObject("user");
                        photo.setUser(user.optString("username"));
                        photo.setUserURL(user.optString("profile_picture"));

                        // Likes: { “data => [array of items] => "likes" => "count" }
                        JSONObject likes = obj.optJSONObject("likes");
                        photo.setLikeCount(likes.optLong("count"));

                        // Add PhotoResource object to the List<PhotoResource>
                        photos.add(photo);

                    }
                } catch (JSONException e) {
                    Log.i("Json exception", "In exception: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Something went wrong with getting photos",
                            Toast.LENGTH_SHORT).show();
                }
                if (isRefreshed) {
                    photoAdapter.addAll(photos);
                    swipeRefreshLayout.setRefreshing(false);
                } else
                    // This is callback
                    photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("Async", "In onFailure");
                Toast.makeText(getApplicationContext(), "Something went wrong with getting photos",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // This method is called when user clicks on the R.id.ivPhoto
    public void viewPhotoZoomActivity(String o) {
        Intent i = new Intent(this, PhotoZoomActivity.class);
        i.putExtra("Open", o);
        startActivity(i);
    }


}
