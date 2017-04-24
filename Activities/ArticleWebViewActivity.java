package in.thesoup.thesoup.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import in.thesoup.thesoup.R;

/**
 * Created by Jani on 24-04-2017.
 */

public class ArticleWebViewActivity extends AppCompatActivity {

    private WebView wView;
    private String URL;
    private ProgressBar progress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_web_view);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();

        URL = extras.getString("ArticleURL");

        wView = (WebView)findViewById(R.id.webview);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);

        wView.setWebViewClient(new MyWebViewClient());

        wView.getSettings().setLoadsImagesAutomatically(true);
        wView.getSettings().setJavaScriptEnabled(true);
        wView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wView.loadUrl(URL);




    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progress.setVisibility(View.GONE);
            ArticleWebViewActivity.this.progress.setProgress(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progress.setVisibility(View.VISIBLE);
            ArticleWebViewActivity.this.progress.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
