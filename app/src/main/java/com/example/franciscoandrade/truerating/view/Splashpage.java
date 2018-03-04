package com.example.franciscoandrade.truerating.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.example.franciscoandrade.truerating.R;

/**
 * Created by melg on 3/4/18.
 */

public class Splashpage extends AppCompatActivity {
    private VideoView videoView;
    private String TAG;
    private Button button;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_page);

        videoView = findViewById(R.id.video_view);
        button = findViewById(R.id.next_page_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Splashpage.this,MapsActivity.class);
                startActivity(intent);
            }
        });

        String uriPath = "android.resource://" + getPackageName() + "/"
                + R.raw.trurating_animatedd;

        Uri uri = Uri.parse(uriPath);
        videoView.seekTo(50);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.setZOrderOnTop(true);
        videoView.start();

    }



}

