package com.example.nlukic.webviewtest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.example.nlukic.webviewtest.utils.Requester;
import com.example.nlukic.webviewtest.utils.UserRequestListener;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements UserRequestListener {

    private String userId;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.this.userId == null || MainActivity.this.accessToken == null) {
                    CharSequence text = "Application is loading initial data!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(MainActivity.this, text, duration);
                    toast.show();
                    Log.v("AttemptedWebview", "UserID: " + MainActivity.this.userId + " | Token: " + MainActivity.this.accessToken);

                    return;
                }

                // Create new fragment and transaction
                ViewerFragment viewerFragment = new ViewerFragment();
                viewerFragment.setUserId(MainActivity.this.userId);
                viewerFragment.setAccessToken(MainActivity.this.accessToken);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, viewerFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        try{
            Requester requester = new Requester(this);
            requester.getAccessToken();
            requester.createUser();
        }catch(JSONException ex){
            Log.v("Requester.Exception", ex.getMessage());
        }
        // Create new fragment and transaction
        MainFragment mainFragment = new MainFragment();
        mainFragment.setTextContent("Click on the fab button to link your account.");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, mainFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onUserActionSuccess(JSONObject resp) {
        try {
            JSONObject result = resp.getJSONObject("result");
            this.userId = result.getString("id");
            Log.v("onUserActionSuccess", "User ID: " + this.userId);
        } catch (JSONException ex) {
            Log.v("onUserActionSuccess", ex.toString());
        }
    }

    @Override
    public void onUserActionFailure(VolleyError error) {
        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null && networkResponse.data != null) {
            String jsonError = new String(networkResponse.data);
            Log.v("Response.OnError", "Response is: " + jsonError);
        }
        Log.v("Response.OnError", error.toString());
    }

    @Override
    public void onTokenActionSuccess(JSONObject resp) {
        try {
            JSONObject result = resp.getJSONObject("result");
            this.accessToken = result.getString("access_token");
            Log.v("onUserActionSuccess", "AccessToken: " + this.accessToken);
        } catch (JSONException ex) {
            Log.v("onTokenActionSuccess", ex.toString());
        }
    }

    @Override
    public void onTokenActionFailure(VolleyError error) {
        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null && networkResponse.data != null) {
            String jsonError = new String(networkResponse.data);
            Log.v("Response.OnError", "Response is: " + jsonError);
        }
        Log.v("Response.OnError", error.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
