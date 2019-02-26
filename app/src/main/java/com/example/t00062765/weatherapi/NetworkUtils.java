package com.example.t00062765.weatherapi;



import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by musfiqrahman on 2019-02-01.
 */

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;


    private static final String BOOK_BASE_URL =  "https://samples.openweathermap.org/data/2.5/weather"; // Base URI for the Books API
    private static final String QUERY_PARAM = "q"; // Parameter for the search string
    private static final String APPID = "c682f36808b547af285575d68cd1a448";
//    private static final String MAX_RESULTS = "maxResults"; // Parameter that limits search results
//    private static final String PRINT_TYPE = "printType";   // Parameter to filter by print type

    static String getURL(String queryString){

        Uri bookUri = Uri.parse(BOOK_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, queryString)
//                .appendQueryParameter(MAX_RESULTS, "5")
                .appendQueryParameter("APPID", APPID).build();
        return bookUri.toString();
    }

    static String getBookInfo(String queryString){

        String bookJSONString = null;

        Uri bookUri = Uri.parse(BOOK_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, queryString).build();
//                .appendQueryParameter(MAX_RESULTS, "5")
//                .appendQueryParameter(PRINT_TYPE, "books").build();


        BufferedReader reader = null;
        HttpURLConnection urlConnection = null;

        try {
            URL requestURL = new URL(bookUri.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {

             /* Since it's JSON, adding a newline isn't necessary (it won't affect
            parsing) but it does make debugging a *lot* easier if you print out the
            completed buffer for debugging. */

                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            bookJSONString = buffer.toString();

        }catch (Exception ex){
            ex.printStackTrace();
            return null;

        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.d(LOG_TAG, bookJSONString);
        return bookJSONString;
    }

}

/*
    private void processJSONRespose(String s){
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
                    mTitleView.setText(title);
                    mAuthorView.setText(authors);
                    return;
                }
            }


            mTitleView.setText("No Results Found");
            mAuthorView.setText("");


        } catch (Exception e){
            mTitleView.setText("No Results Found");
            mAuthorView.setText("");
            e.printStackTrace();
        }
    }

 */