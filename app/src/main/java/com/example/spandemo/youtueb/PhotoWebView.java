package com.example.spandemo.youtueb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Browser;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.spandemo.R;

import java.util.Locale;

public class PhotoWebView extends WebView {
    private class YoutubeProxy {
        @JavascriptInterface
        public void postEvent(final String eventName, final String eventData) {
            if ("loaded".equals(eventName)) {
//                AndroidUtilities.runOnUIThread(() -> {
//                    progressBar.setVisibility(View.INVISIBLE);
//                    progressBarBlackBackground.setVisibility(View.INVISIBLE);
//                    if (setPlaybackSpeed) {
//                        setPlaybackSpeed = false;
//                        setPlaybackSpeed(playbackSpeed);
//                    }
//                    pipItem.setEnabled(true);
//                    pipItem.setAlpha(1.0f);
//                });
                Log.d("YoutubeProxy", "loaded");
            }
        }
    }
    private static final String youtubeFrame = "<!DOCTYPE html><html><head><style>" +
            "body { margin: 0; width:100%%; height:100%%;  background-color:#000; }" +
            "html { width:100%%; height:100%%; background-color:#000; }" +
            ".embed-container iframe," +
            ".embed-container object," +
            "   .embed-container embed {" +
            "       position: absolute;" +
            "       top: 0;" +
            "       left: 0;" +
            "       width: 100%% !important;" +
            "       height: 100%% !important;" +
            "   }" +
            "   </style></head><body>" +
            "   <div class=\"embed-container\">" +
            "       <div id=\"player\"></div>" +
            "   </div>" +
            "   <script src=\"https://www.youtube.com/iframe_api\"></script>" +
            "   <script>" +
            "   var player;" +
            "   var posted = false;" +
            "   YT.ready(function() {" +
            "       player = new YT.Player(\"player\", {" +
            "                              \"width\" : \"100%%\"," +
            "                              \"events\" : {" +
            "                              \"onReady\" : \"onReady\"," +
            "                              \"onError\" : \"onError\"," +
            "                              \"onStateChange\" : \"onStateChange\"," +
            "                              }," +
            "                              \"videoId\" : \"%1$s\"," +
            "                              \"height\" : \"100%%\"," +
            "                              \"playerVars\" : {" +
            "                              \"start\" : %2$d," +
            "                              \"rel\" : 1," +
            "                              \"showinfo\" : 0," +
            "                              \"modestbranding\" : 0," +
            "                              \"iv_load_policy\" : 3," +
            "                              \"autohide\" : 1," +
            "                              \"autoplay\" : 1," +
            "                              \"cc_load_policy\" : 1," +
            "                              \"playsinline\" : 1," +
            "                              \"controls\" : 1" +
            "                              }" +
            "                            });" +
            "        player.setSize(window.innerWidth, window.innerHeight);" +
            "    });" +
            "    function setPlaybackSpeed(speed) { " +
            "       player.setPlaybackRate(speed);" +
            "    }" +
            "    function onError(event) {" +
            "       if (!posted) {" +
            "            if (window.YoutubeProxy !== undefined) {" +
            "                   YoutubeProxy.postEvent(\"loaded\", null); " +
            "            }" +
            "            posted = true;" +
            "       }" +
            "    }" +
            "    function onStateChange(event) {" +
            "       if (event.data == YT.PlayerState.PLAYING && !posted) {" +
            "            if (window.YoutubeProxy !== undefined) {" +
            "                   YoutubeProxy.postEvent(\"loaded\", null); " +
            "            }" +
            "            posted = true;" +
            "       }" +
            "    }" +
            "    function onReady(event) {" +
            "       player.playVideo();" +
            "    }" +
            "    window.onresize = function() {" +
            "       player.setSize(window.innerWidth, window.innerHeight);" +
            "       player.playVideo();" +
            "    }" +
            "    </script>" +
            "</body>" +
            "</html>";
    public PhotoWebView(@NonNull Context context) {
        super(context);
        initWebView(context);
    }

    public PhotoWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initWebView(context);
    }

    public PhotoWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebView(context);

    }

    public PhotoWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initWebView(context);

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(Context context) {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        addJavascriptInterface(new YoutubeProxy(), "YoutubeProxy");
        setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("WebView", url.toString());
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));

    }

    void loadUrl() {
        loadDataWithBaseURL("", String.format(Locale.US, youtubeFrame, "MubLylzx1Kg", 0), "text/html", "UTF-8", "https://youtube.com");
    }

}
