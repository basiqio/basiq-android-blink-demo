package com.example.nlukic.webviewtest.utils;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface UserRequestListener {
    void onUserActionSuccess(JSONObject resp);

    void onUserActionFailure(VolleyError err);
}
