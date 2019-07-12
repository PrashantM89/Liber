package org.app.liber.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.app.liber.R;
import org.w3c.dom.Text;

public class WebActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://134.209.159.20:8080/LiberWebApi-1.0/";

    //private static final String BASE_URL = "http://192.168.1.102:8080/LiberWebApi-1.0/";
    private WebView myWebView;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        toolbarTitle = (TextView)findViewById(R.id.common_toolbar_title_id);
        toolbarTitle.setText("Help");


        myWebView = (WebView) findViewById(R.id.webView);
//        myWebView.getSettings().setLoadWithOverviewMode(true);
//        myWebView.getSettings().setUseWideViewPort(true);
//        myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
//        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.loadUrl(BASE_URL+"help.html");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (myWebView.canGoBack()) {
                        myWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
