package com.example.t00062765.weatherapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView titleView, authorView;
    private Button queryButton;
    private EditText queryText;

    public static final String CUSTOM_ACTION = "com.example.mrahman.myapplication.CUSTOM_ACTION";

    private ResultReceiver mResultReceiver = new ResultReceiver(new Handler()){
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultCode == 1){
                if(resultData != null){
                    String res = resultData.getString("RESULT");
                    processJSONRespose(res);
                }
            }
        }
    };


    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("BroadcastReceiver", "Broadcast Received");
            String action = intent.getAction();
            if(MainActivity.CUSTOM_ACTION.equals(action)) {
                // When receive it will show an toast popup message.

                Toast.makeText(context, "Custom Broadcast Receiver One Receive Message.", Toast.LENGTH_SHORT).show();
                String s = intent.getStringExtra("RESULT");
                processJSONRespose(s);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    protected void onResume(){
        super.onResume();
        IntentFilter filter = new IntentFilter(CUSTOM_ACTION);
        registerReceiver(myBroadcastReceiver, filter);
    }

    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleView = findViewById(R.id.titleView);
        authorView = findViewById(R.id.authorView);
        queryText = findViewById(R.id.queryText);

        myBroadcastReceiver = new MyBroadcastReceiver();

    }

    public void fetchDataOnClick(View view){
        String s = queryText.getText().toString();
        //String res = NetworkUtils.getBookInfo(s);
        //Log.d(TAG, res);
        //new MyAsyncTask().execute(s);

        // Using Intent Filter
        //Intent intent = new Intent(this, MyIntentService.class);
        //intent.putExtra("QUERY", s);
        //intent.putExtra("RECEIVER", mResultReceiver);

        //startService(intent);


        // Using Volly
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =NetworkUtils.getURL(s);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        processJSONRespose(response);
                        Log.v(TAG, "R: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private class MyAsyncTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, s);
            processJSONRespose(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            return NetworkUtils.getBookInfo(strings[0]);
        }
    }


    private class Container {
//        String kind;
//        int totalItems;
//        Item[] items;
        Weather[] weather;
    }

    private class Weather{
        int id;
        String description;
    }

    private class Item {
        String kind;
        String id;
        String etag;
        ///blah
        VolumeInfo volumeInfo;
        String publisher;
        ///etc.

    }

    private class VolumeInfo {
        String title;
        String[] authors;
    }

    private void processJSONRespose(String s){

        Container fullJsonObject = new Gson().fromJson(s, Container.class);
//        Item itm = fullJsonObject.items[0];
//        titleView.setText(itm.volumeInfo.title);
//        authorView.setText(itm.volumeInfo.authors[0]);
        Weather weather = fullJsonObject.weather[0];
        titleView.setText(weather.description);
//        authorView.setText(weather);

        /*

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            for(int i = 0; i<itemsArray.length(); i++){
                JSONObject book = itemsArray.getJSONObject(i);
                String title=null;
                String authors=null;
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");


                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                } catch (Exception e){
                    e.printStackTrace();
                }


                if (title != null && authors != null){
                    titleView.setText(title);
                    authorView.setText(authors);
                    return;
                }
            }


            titleView.setText("No Results Found");
            authorView.setText("");


        } catch (Exception e){
            titleView.setText("No Results Found");
            authorView.setText("");
            e.printStackTrace();
        }
        */
    }
}
