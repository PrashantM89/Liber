package org.app.liber.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.app.liber.R;

public class MeActivity extends AppCompatActivity {

    private LinearLayout mMyProfileView;
    private CardView mAvatarCard;
    private ProgressBar mProgressBar;
    private FrameLayout mAvatarContainer;
    private ImageView mAvatarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        mMyProfileView = (LinearLayout)findViewById(R.id.row_my_profile);
        mAvatarCard = (CardView)findViewById(R.id.card_avatar);
        mAvatarContainer = (FrameLayout)findViewById(R.id.avatar_container);
        mAvatarImageView = (ImageView)findViewById(R.id.me_avatar);

        mMyProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }
}
