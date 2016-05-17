package com.android.thestateofperfection84zb;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * I created this custom ArrayAdapter because I will need it to populate List<PhotoResource> photos
 * in PhotoActivity class
 */
public class PhotoAdapter extends ArrayAdapter<PhotoResource> {

    PhotoActivity photoActivity;
    int numberOfLikes;

    // View lookup cache
    static class ViewHolder {
        @Bind(R.id.ivPhoto)
        ImageView imageView;
        @Bind(R.id.ivUser)
        ImageView ivUser;
        @Bind(R.id.tvUser)
        TextView tvUser;
        @Bind(R.id.tvCaption)
        TextView tvCaption;
        @Bind(R.id.tvLikes)
        TextView tvLikes;
        @Bind(R.id.tvDateCreated)
        TextView tvDateCreated;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public PhotoAdapter(Context context, List<PhotoResource> photos) {
        super(context, 0, photos);
        photoActivity = (PhotoActivity) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final PhotoResource photo = getItem(position);

        ViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.photo_item_layout,
                    parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.imageView.setImageResource(0); //clear off
        // This Picasso is main photo in the PhotoActivity class
        Picasso.with(getContext()).load(photo.getImages()).placeholder(R.drawable.loading)
                .into(viewHolder.imageView);
        viewHolder.ivUser.setImageResource(0); //clear off
        // This Picasso is user photo in the PhotoActivity class
        Picasso.with(getContext()).load(photo.getUserURL()).fit().centerCrop().
                into(viewHolder.ivUser);
        viewHolder.tvCaption.setText(photo.getCaption());
        viewHolder.tvUser.setText(photo.getUser());


        numberOfLikes = (int) photo.getLikeCount();

        if (numberOfLikes == 0)
            viewHolder.tvLikes.setText("No likes");
        else if (numberOfLikes == 1)
            viewHolder.tvLikes.setText("Only 1 like");
        else
            viewHolder.tvLikes.setText(numberOfLikes + " likes");

        // This is for the time showed in PhotoActivity class
        CharSequence displayCreated = DateUtils.getRelativeTimeSpanString(
                photo.getCreatedTime() * 1000,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL);
        viewHolder.tvDateCreated.setText(displayCreated);

        // We need this listener for opening the PhotoZoomActivity
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoActivity.viewPhotoZoomActivity(photo.getImages());
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

}
