package org.app.liber;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class OnboardingActivity extends AppCompatActivity {


    private ViewPager slideViewPager;
    private LinearLayout dotLayout;
    private SlideAdapter adapter;
    private Button button;
    int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        button =(Button)findViewById(R.id.got_it_id);
        slideViewPager = (ViewPager)findViewById(R.id.slideViePagerId);
       // dotLayout = (LinearLayout)findViewById(R.id.dotsLayoutId);
        adapter = new SlideAdapter(this);
        slideViewPager.setAdapter(adapter);

        slideViewPager.addOnPageChangeListener(viewListner);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainLiberActivity.class));
            }
        });
    }

    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPage = position;
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
