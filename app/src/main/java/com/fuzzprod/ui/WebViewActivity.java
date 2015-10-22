package com.fuzzprod.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fuzzprod.R;

import butterknife.Bind;

public class WebViewActivity extends BaseActivity {

    public static final String KEY_URL_TO_LOAD = "URL";

    @Bind(R.id.webView)
    WebView wb;

    private String targetURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wb.setWebViewClient(new MyBrowser());

        Intent intent = getIntent();
        if (intent != null) {
            targetURL = intent.getStringExtra(KEY_URL_TO_LOAD);
            if(targetURL != null && !TextUtils.isEmpty(targetURL)) {
                wb.getSettings().setLoadsImagesAutomatically(true);
                wb.getSettings().setJavaScriptEnabled(true);
                wb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                wb.loadUrl(targetURL);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("targetURL", targetURL);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        targetURL = savedInstanceState.getString("targetURL");
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}