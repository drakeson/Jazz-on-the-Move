package ug.code.jazzonthemove.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

import ug.code.jazzonthemove.R;

public class VideoActivity extends AppCompatActivity {
    static final String GOOGLE_API_KEY = "AIzaSyBMSfTr__184Qe6luUWH5lOlW5tOdiWfB8";
    static final String YOUTUBE_VIDEO_ID = "Fy67Hlww9Hc";
    private static final String TAG = "VideoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        startActivity(YouTubeStandalonePlayer.createVideoIntent(this, GOOGLE_API_KEY, YOUTUBE_VIDEO_ID, 0, true, false));
    }
}