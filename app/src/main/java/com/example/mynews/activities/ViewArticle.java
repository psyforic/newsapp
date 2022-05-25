package com.example.mynews.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.mynews.R;
import com.example.mynews.utils.Utils;
import com.example.mynews.utils.ViewAnimation;
import com.sdsmdg.tastytoast.TastyToast;

import static com.example.mynews.utils.Utils.*;

public class ViewArticle extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.webview)
    WebView webview;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.image)
    ImageView image;
    private final static int LOADING_DURATION = 5000;
    String title, desc, author, source, published_at, url, urlToImage, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);
        ButterKnife.bind(this);
        initData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                ViewAnimation.fadeOut(lyt_progress);
            }
        }, LOADING_DURATION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                webViewContent(url);

            }
        }, LOADING_DURATION + 100);
        initToolbar();

    }

    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        desc = intent.getStringExtra("desc");
        author = intent.getStringExtra("author");
        source = intent.getStringExtra("source");
        published_at = intent.getStringExtra("published_at");
        urlToImage = intent.getStringExtra("imageUrl");
//        RequestOptions requestOptions=new RequestOptions();
//        requestOptions.error(Utils.getRandomDrawbleColor());
        Glide.with(this)
                .load(urlToImage)
                // .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image);

        TastyToast.makeText(getApplicationContext(), "" + urlToImage, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void webViewContent(String url) {
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl(url);
        //  card.setVisibility(View.VISIBLE);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                return true;
            case R.id.action_save:
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_share:
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}