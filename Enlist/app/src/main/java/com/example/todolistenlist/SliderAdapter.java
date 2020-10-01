package com.example.todolistenlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] images_slider = {
            R.mipmap.publish_post_img,
            R.mipmap.shared_workspace,
            R.mipmap.reviewed_docs
    };

    public String[] title_slider = {
            "Create",
            "Sync",
            "Execute"
    };

    public String[] description_slider = {
            "Add the assigned tasks to stay focused & achieve more",
            "Share the assigned task within the batch to keep everyone going",
            "Complete the assigned task to stay on top of the line"
    };

    @Override
    public int getCount() {
        return title_slider.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_slider, container, false);

        ImageView ImageView_slider = (ImageView) view.findViewById(R.id.slider_image);
        TextView Title_slider = (TextView) view.findViewById(R.id.slider_title);
        TextView Description_slider = (TextView) view.findViewById(R.id.slider_description);

        ImageView_slider.setImageResource(images_slider[position]);
        Title_slider.setText(title_slider[position]);
        Description_slider.setText(description_slider[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
