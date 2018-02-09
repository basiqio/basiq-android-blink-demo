package com.example.nlukic.webviewtest.utils;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.nlukic.webviewtest.MainActivity;
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
        JSONObject data = payload;

        try {

            if (payload.has("data")) {
                data = payload.getJSONObject("data");
            }

            switch (event) {
                case "job":
                    if (!payload.getBoolean("success")) {
                        CharSequence errorText = data.getString("detail");

                        Toast toast = Toast.makeText(this.mContext, errorText, Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    CharSequence text = "ID of the job: " + data.getString("id");

                    Toast jobToast = Toast.makeText(this.mContext, text, Toast.LENGTH_SHORT);
                    jobToast.show();
                    break;
                case "connection":
                    if (!payload.getBoolean("success")) {
                        CharSequence errorText = data.getString("detail");

                        Toast connectionToast = Toast.makeText(this.mContext, errorText, Toast.LENGTH_SHORT);
                        connectionToast.show();
                        return;
                    }
                    setConnectionId(data.getString("id"));
                    break;
                case "cancellation":
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
