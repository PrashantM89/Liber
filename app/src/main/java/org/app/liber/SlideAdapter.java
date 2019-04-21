package org.app.liber;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;


    public SlideAdapter(Context context) {
        this.context = context;
    }

    public String[] slide_text ={
            "Liber is a global library, for the people and by the people.",
            "Find your favourite books in Library and rent it. We will deliver it to your doorstep.",
            "Upload your books, let others rent it from you. You earn from rental."
    };

    public int[] slide_img = {R.drawable.global_lib,R.drawable.import_book,R.drawable.rent_earn};

    public String[] slite_title ={
            "Liber",
            "Rent a book to read",
            "Create your library. Earn"
    };

    @Override
    public int getCount() {
        return slide_text.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater =(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.slide_item_1,container,false);

        TextView text = (TextView)v.findViewById(R.id.onboarding_desc_id);
        TextView textTitle = (TextView)v.findViewById(R.id.onboarding_title_id);
        ImageView slideImg = (ImageView)v.findViewById(R.id.slide_img);

        text.setText(slide_text[position]);
        textTitle.setText(slite_title[position]);
        slideImg.setBackgroundResource(slide_img[position]);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
