package com.example.nlukic.webviewtest;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.nlukic.webviewtest.utils.Config;

public class ViewerFragment extends Fragment {

    private String userId;
    private String accessToken;

    public ViewerFragment() {

    }

    public ViewerFragment(String userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.webview_layout, container, false);

        String url = Config.getConfigValue(this.getActivity(), "api_url")+"?user_id="+this.userId+"&access_token="+this.accessToken;

        Log.v("Opening URL", url);

        WebView myWebView = (WebView) view.findViewById(R.id.webview);
        myWebView.loadUrl(url);
        //myWebView.loadUrl("http://www.google.com");
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.addJavascriptInterface(new WebAppInterface(getActivity()), "Android");

        return view;
    }

}