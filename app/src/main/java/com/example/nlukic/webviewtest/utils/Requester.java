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
import java.util.HashMap;
import java.util.Map;

public class Requester {

    private UserRequestListener ctx;

    public Requester (UserRequestListener ctx) throws JSONException {
        this.ctx = (UserRequestListener) ctx;
    }

    public void getUser() {
        RequestQueue queue = Volley.newRequestQueue((Context) this.ctx);
        //String url = "https://au-api.basiq.io/oauth2/token";
        String url = "http://ec2-34-242-249-39.eu-west-1.compute.amazonaws.com/user";
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", "nenadspp@gmail.com");

                return params;
            }

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


            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }*/
        };

        theRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(theRequest);
    }
}
