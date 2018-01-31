package com.example.nlukic.webviewtest.utils;


import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Requester {

    private UserRequestListener ctx;
    private String host;

    public Requester (UserRequestListener ctx) throws JSONException {
        this.ctx = (UserRequestListener) ctx;
        this.host = Config.getConfigValue((Context) this.ctx, "api_url");
    }

    public void createUser() {
        RequestQueue queue = Volley.newRequestQueue((Context) this.ctx);
        //String url = "https://au-api.basiq.io/oauth2/token";
        String url = this.host+"/user";
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("email", "nenadspp@gmail.com");
        } catch (JSONException ex) {
            System.out.println("JSON EXCEPTION: " + ex.toString());
        }
        final String requestBody = jsonBody.toString();
        Log.v("Response.Start", "Starting the request");
        // Request a string response from the provided URL.
        JsonObjectRequest theRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Requester.this.ctx.onUserActionSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Requester.this.ctx.onUserActionFailure(error);
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        theRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(theRequest);
    }

    public void getAccessToken() {
        RequestQueue queue = Volley.newRequestQueue((Context) this.ctx);
        //String url = "https://au-api.basiq.io/oauth2/token";
        String url =this.host+"/access_token";
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("client_id", "in12ij3n1onb12");
        } catch (JSONException ex) {
            System.out.println("JSON EXCEPTION: " + ex.toString());
        }
        final String requestBody = jsonBody.toString();
        Log.v("Response.Start", "Starting the request");
        // Request a string response from the provided URL.
        JsonObjectRequest theRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Requester.this.ctx.onTokenActionSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Requester.this.ctx.onTokenActionFailure(error);
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        theRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(theRequest);
    }
}
