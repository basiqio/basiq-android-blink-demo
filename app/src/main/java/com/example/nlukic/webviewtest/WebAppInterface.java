package com.example.nlukic.webviewtest;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

public class WebAppInterface {
    Context mContext;

    /**
     * Instantiate the interface and set the context
     */
    WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void setConnectionId(String connectionId) {
        transitionToFragment(connectionId);
    }

    @JavascriptInterface
    public void cancelActions() {
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