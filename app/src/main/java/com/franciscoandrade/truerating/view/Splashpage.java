package com.franciscoandrade.truerating.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.franciscoandrade.truerating.R;

/**
 * Created by melg on 3/4/18.
 */

public class Splashpage extends AppCompatActivity {
    private VideoView videoView;
    private String TAG;
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_page);

        videoView = findViewById(R.id.video_view);

        String uriPath = "android.resource://" + getPackageName() + "/"
                + R.raw.trurating_animatedd;

        Uri uri = Uri.parse(uriPath);
        videoView.seekTo(50);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.setZOrderOnTop(true);
        videoView.start();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Splashpage.this, MapsActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
