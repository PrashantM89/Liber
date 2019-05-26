package org.app.liber;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.app.liber.adapter.SlideAdapter;

public class OnboardingActivity extends AppCompatActivity {


    private ViewPager slideViewPager;
    private LinearLayout dotLayout;
    private SlideAdapter adapter;
    private Button button;
    private TextView[] dots;
    int currentPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        button =(Button)findViewById(R.id.got_it_id);
        slideViewPager = (ViewPager)findViewById(R.id.slideViePagerId);
        dotLayout = (LinearLayout)findViewById(R.id.dotsLayoutId);
        adapter = new SlideAdapter(this);
        slideViewPager.setAdapter(adapter);
        addDotsIndicator(0);
        slideViewPager.addOnPageChangeListener(viewListner);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainLiberActivity.class));
            }
        });
    }

    public void addDotsIndicator(int position){
        dots = new TextView[3];
        dotLayout.removeAllViews();

        for(int i=0; i<dots.length;i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.transparentBlack));
            dotLayout.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.black));
        }
    }

    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
//
//            if(position == 2){
//                button.setEnabled(true);
//                button.setText("Got It");
//                button.setVisibility(View.VISIBLE);
//            }else{
//                button.setEnabled(false);
//                button.setVisibility(View.VISIBLE);
//                button.setText("Got It");
//            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
