package com.chen.baselibrary.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.chen.baselibrary.R;

/**
 * webActivity
 * @author chen
 */
public class WebActivity extends BaseToolBarActivity implements DownloadListener{
    private static final String APP_CACAHE_DIRNAME = "web_cache";
    public static final String INTENT_KEY_URL = "url";
    public static final String INTENT_KEY_TITLE = "title";
    private WebView webview;
    private ProgressBar pbWebLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.webview = findViewById(R.id.wv_common);
        this.pbWebLoad = findViewById(R.id.pb_webload);

        this.pbWebLoad.setProgress(0);

        String title = getIntent().getStringExtra(INTENT_KEY_TITLE);
        if(!TextUtils.isEmpty(title)){
            this.actionBar.setTitle(title);
        }

        this.settingWebView();

        this.loadUrl(getIntent().getStringExtra(INTENT_KEY_URL));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_web;
    }

    /**
     * 设置webview属性
     */
    private void settingWebView(){
        webview.getSettings().setSupportZoom(false);
        webview.getSettings().setBuiltInZoomControls(false);
        try {
            webview.getSettings().setJavaScriptEnabled(true);
        } catch (Exception e) {
            System.out.println(e);
        }
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webview.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        webview.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        //设置  Application Caches 缓存目录
        webview.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webview.getSettings().setAppCacheEnabled(true);
        webview.setDownloadListener(this);
        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pbWebLoad.setProgress(newProgress);
                if(newProgress == 100){
                    pbWebLoad.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 加载url
     * @param url
     */
    private void loadUrl(String url){
        if(TextUtils.isEmpty(url)){
            return;
        }
        this.webview.loadUrl(url);
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        Log.i("TAG",url+":"+userAgent+":"+contentDisposition+":" + mimetype + ":" + contentLength);
    }
}
