package com.flyn.smartandroid.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flyn.smartandroid.R;
import com.flyn.smartandroid.util.IntentUtils;


public class WebViewToolBar extends LinearLayout implements OnClickListener
{
    private WebView mWebView;
    private Context mContext;
    private ImageView iv_webview_back;
    private ImageView iv_webview_refresh;
    private ImageView iv_webview_forward;
    private ImageView iv_webview_browser;
    private NumberProgressBar pb_loading;

    public WebViewToolBar(Context context)
    {
        super(context);
        if (isInEditMode())
        {
            return;
        }

        this.mContext = context;
        init();
    }

    public WebViewToolBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        if (isInEditMode())
        {
            return;
        }

        this.mContext = context;
        init();
    }

    private void init()
    {
        removeAllViews();

        View view = LayoutInflater.from(mContext).inflate(R.layout.view_webviewbar, null);

        view.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        addView(view);

        pb_loading = (NumberProgressBar) view.findViewById(R.id.pb_loading);
        pb_loading.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible);
        iv_webview_back = (ImageView) view.findViewById(R.id.iv_webview_back);
        iv_webview_refresh = (ImageView) view.findViewById(R.id.iv_webview_refresh);
        iv_webview_forward = (ImageView) view.findViewById(R.id.iv_webview_forward);
        iv_webview_browser = (ImageView) view.findViewById(R.id.iv_webview_browser);

        iv_webview_back.setOnClickListener(this);
        iv_webview_refresh.setOnClickListener(this);
        iv_webview_forward.setOnClickListener(this);
        iv_webview_browser.setOnClickListener(this);
    }

    public void setWebView(WebView webView)
    {
        this.mWebView = webView;
    }

    @Override
    public void onClick(View v)
    {
        if (null == mWebView)
        {
            return;
        }

        if (v.getId() == R.id.iv_webview_back)
        {
            if (mWebView.canGoBack())
            {
                mWebView.goBack();
            }
        } else if (v.getId() == R.id.iv_webview_refresh)
        {
            mWebView.reload();
        } else if (v.getId() == R.id.iv_webview_forward)
        {
            if (mWebView.canGoForward())
            {
                mWebView.goForward();
            }
        } else if (v.getId() == R.id.iv_webview_browser)
        {
            mContext.startActivity(IntentUtils.openLink(mWebView.getUrl()));
        }
    }

    public void setProgressNumber(int progress)
    {
        if (progress >= 100)
        {
            pb_loading.setVisibility(View.INVISIBLE);
        } else
        {
            pb_loading.setVisibility(View.VISIBLE);
        }

        pb_loading.setProgress(progress);
    }

}
