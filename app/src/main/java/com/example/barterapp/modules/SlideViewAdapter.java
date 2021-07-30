package com.example.barterapp.modules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.barterapp.R;

public class SlideViewAdapter extends PagerAdapter {
    Context ctx;
    public  SlideViewAdapter(Context context) {this.ctx = context;}

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_screen, container, false);


        ImageView mainImage = view.findViewById(R.id.ss_logoImage);
        TextView title = view.findViewById(R.id.ss_titleText);
        TextView body = view.findViewById(R.id.ss_bodyText);


        switch (position){
            case 0:
                mainImage.setImageResource(R.drawable.onlylogo_transparent);
                title.setText("Welcome");
                body.setText("Fair exchange is no robbery.");

                break;

            case 1:
                mainImage.setImageResource(R.drawable.swap_ss);
                title.setText("What is Barter?");
                body.setText("Barter is considered as an act of trading items between 2 parties without the use of money. Therefore, if you have too many items that you're not using anymore? Come barter with us here!");
                break;
            case 2:
                mainImage.setImageResource(R.drawable.marketplace_ss);
                title.setText("What is Marketplace?");
                body.setText("A platform where users can come together to trade their items to a curated customer base. Scroll, View, and Trade here!");
                break;
            case 3:
                mainImage.setImageResource(R.drawable.user_ss);
                title.setText("What is User Profile?");
                body.setText("A visual display of user's personal data. Users able to personalized and view users' information here!");
                break;
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
