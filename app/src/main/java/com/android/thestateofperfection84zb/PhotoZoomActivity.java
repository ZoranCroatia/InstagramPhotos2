package com.android.thestateofperfection84zb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

/**
 * This activity will show only 1 photo that can be zoomed
 */
public class PhotoZoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("Open");

        PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);

        Picasso.with(this)
                .load(url)
                .fit()              // Image is stretched fullscreen
                .into(photoView);

        Toast.makeText(this, "This photo can be zoomed", Toast.LENGTH_SHORT).show();

    }


}