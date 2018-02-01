package com.example.nlukic.webviewtest.utils;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.nlukic.webviewtest.MainFragment;
import com.example.nlukic.webviewtest.R;

import org.json.JSONObject;


public class WebViewClientWithListener extends WebViewClient {
    Context mContext;

    /**
     * Instantiate the interface and set the context
     */
    public WebViewClientWithListener(Context c) {
        mContext = c;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url != null && url.startsWith("basiq://")) {
            Integer payloadStart = url.indexOf("{");
            String payloadString;
            String event;
            if (payloadStart != -1) {
                payloadString = url.substring(payloadStart);
                event = url.substring(url.indexOf("//") + 2, payloadStart - 1);
            } else {
                payloadString = "{}";
                event = url.substring(url.indexOf("//") + 2);
            }
            try {
                JSONObject payload = new JSONObject(payloadString);
                parseEventData(event, payload);
            } catch (Exception ex) {
                Log.v("ReceivedProtocolError", "JSON exception: "+ex.getMessage());
            }
            Log.v("ReceivedProtocol", "URI: " + url + " | Event: " + event + " | Payload: " + payloadString);
            return true;
        } else {
            return false;
        }
    }

    private void parseEventData(String event, JSONObject payload) {
        try {
            switch (event) {
                case "connection":
                    setConnectionId(payload.getString("id"));
                    break;
                case "cancelation":
                    cancelActions();
                    break;
            }
        } catch (Exception ex) {
            Log.v("ReceivedProtocolError", "JSON exception: "+ex.getMessage());
        }
    }

    private void setConnectionId(String connectionId) {
        transitionToFragment(connectionId);
    }

    private void cancelActions() {
        transitionToFragment("Click on the fab button to link your account.");
    }

    private void transitionToFragment(String text) {
        AppCompatActivity activity = ((AppCompatActivity) mContext);

        MainFragment mainFragment = new MainFragment();
        mainFragment.setTextContent(text);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mainFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
