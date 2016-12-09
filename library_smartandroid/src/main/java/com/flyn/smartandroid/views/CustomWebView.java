package com.flyn.smartandroid.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyn.smartandroid.R;
import com.flyn.smartandroid.app.Application;
import com.flyn.smartandroid.util.NetWorkUtil;

public class CustomWebView extends FrameLayout {
    private WebViewToolBar webViewToolBar;
    private WebView mWebView;
    private TextView tv_reload;
    private LinearLayout ll_nonet;
    private String mUrl;
    private WebChromeClient webChromeClient;
    private WebViewListener webViewListener;
    private boolean shouldOverrideUrl = false;

    public CustomWebView(Context context) {
        super(context);
        if (isInEditMode()) {
            return;
        }

        initView();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }

        initView();

    }

    public void setWebViewListener(WebViewListener webViewListener) {
        this.webViewListener = webViewListener;
    }

    public WebView getWebView() {
        return mWebView;
    }

    public void setWebChromeClient(WebChromeClient webChromeClient) {
        this.webChromeClient = webChromeClient;
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_custom_webview, null);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(view);

        mWebView = (WebView) findViewById(R.id.wv_content);
        webViewToolBar = (WebViewToolBar) findViewById(R.id.webview_toolbar);
        webViewToolBar.setWebView(mWebView);
        tv_reload = (TextView) findViewById(R.id.tv_reload);
        ll_nonet = (LinearLayout) findViewById(R.id.ll_nonet);

        tv_reload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (NetWorkUtil.isNetworkConnected(getContext())) {
                    mWebView.removeAllViews();
                    mWebView.loadUrl("about:blank");
                    loadUrl(mUrl);
                    hiddenErrorWebPage();
                    Application.getInstance().runOnUiThreadDelay(new Runnable() {
                        @Override
                        public void run() {
                            mWebView.setVisibility(VISIBLE);
                        }
                    }, 500);
                } else {
                    showErrorWebPage();
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (shouldOverrideUrl) {
                    view.loadUrl(url);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (null != webViewListener) {
                    webViewListener.onReceivedTitle(view.getTitle());
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (null != webViewListener) {
                    webViewListener.onReceivedTitle(view.getTitle());
                }
                if (null != webViewListener) {
                    webViewListener.onPageFinished(view, url);
                }
                addImageClickListener();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mWebView.setVisibility(GONE);
                showErrorWebPage();
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setSaveEnabled(false);
        mWebView.addJavascriptInterface(new MyJavascriptInterface(), "imagelistner");

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                webViewToolBar.setProgressNumber(newProgress);

                if (null != webChromeClient) {
                    webChromeClient.onProgressChanged(view, newProgress);
                }

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (null != webViewListener) {
                    webViewListener.onReceivedTitle(title);
                }
            }

        });

    }

    public void setShouldOverrideUrl(boolean shouldOverrideUrl) {
        this.shouldOverrideUrl = shouldOverrideUrl;
    }

    private void addImageClickListener() {
        this.mWebView.loadUrl("javascript:(function(){" + " var objs = document.getElementsByTagName(\"img\"); " + " var arr=new Array(); for   " +
                "(var i=0;i <objs.length;i++) { arr[i]= objs[i].src;}  " + "for(var i=0;i<objs.length;i++)  " + "{" + "    objs[i].onclick=function" +
                "()  " + "    {  " + "        window.imagelistner.openImage(arr,this.src);  " + "    }  " + "}" + "})()");

    }


    public void loadUrl(String url) {
        this.mUrl = url;
        this.mWebView.loadUrl(url);
    }

    public void onResume() {
        if (Build.VERSION.SDK_INT >= 11) {
            mWebView.onResume();
        } else {
            try {
                mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onPause() {

        if (Build.VERSION.SDK_INT >= 11) {
            mWebView.onPause();
        } else {
            try {
                mWebView.getClass().getMethod("onPause").invoke(mWebView, (Object[]) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void destroy() {
        mWebView.stopLoading();
        mWebView.loadUrl("about:blank");
        mWebView.removeAllViews();
        removeAllViews();
    }

    public void showErrorWebPage() {
        ll_nonet.setVisibility(View.VISIBLE);
    }

    public void hiddenErrorWebPage() {
        ll_nonet.setVisibility(View.GONE);
    }

    public void hiddenWebViewToolBar() {
        this.webViewToolBar.setVisibility(View.GONE);
    }

    public void showWebViewToolBar() {
        this.webViewToolBar.setVisibility(View.VISIBLE);
    }

    public WebViewToolBar getWebViewToolBar() {
        return this.webViewToolBar;
    }


    public interface WebViewListener {
        void onReceivedTitle(String title);

        void onOpenImage(String[] urlList, int position);

        void onPageFinished(WebView view, String url);
    }

    private class MyJavascriptInterface {

        @JavascriptInterface
        public void openImage(String[] urlList, String url) {
            if (null != webViewListener) {

                for (int i = 0; i < urlList.length; i++) {
                    if (url.equals(urlList[i])) {
                        webViewListener.onOpenImage(urlList, i);
                        break;
                    }
                }
            }
        }
    }


}
