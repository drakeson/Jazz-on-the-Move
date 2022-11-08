package ug.code.jazzonthemove.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import ug.code.jazzonthemove.R;

public class WebActivity extends AppCompatActivity {
    private WebView webview;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setTitle(getIntent().getStringExtra("NAME"));
        progressBar = findViewById(R.id.progressBar);
        webview = findViewById(R.id.webview);
        webview.setWebViewClient(new myWebClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(getIntent().getStringExtra("URL"));
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }
    }
}