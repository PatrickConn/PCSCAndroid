package com.example.patrick.pcsc;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JSONParser {

    static InputStream instream = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }


    @SuppressWarnings("deprecation")
    public static JSONObject makeHttpRequest(String url, String method,
                                             List<NameValuePair> params) {

        // Making HTTP request
        try {

            // check for the requested method
            if(method == "POST"){
                // if the requested method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                instream = httpEntity.getContent();

            }else if(method == "GET"){
                // if the requested method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String queryValuestring = URLEncodedUtils.format( params, "UTF-8");
                url += "?" + queryValuestring;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                instream = httpEntity.getContent();
                httpGet.setHeader("Content-type", "application/json");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    instream, "UTF-8"), 8);
            StringBuilder result = new StringBuilder();
            String data = null;
            while ((data = reader.readLine()) != null) {
                result.append(data + "\n");
            }
            instream.close();
            json = result.toString();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        // try parseing the string to a JSON object
        try {
            jObj = new JSONObject(json);
        }  catch (Exception e) {
            e.printStackTrace();
        }


        // return JSON String
        return jObj;

    }
    @SuppressWarnings("deprecation")
    public JSONObject getJSONFromUrl(final String url) {

        // Making HTTP request
        try {
            // Construct the client and the HTTP request.
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            // Execute the POST request and store the response locally.
            HttpResponse httpResponse = httpClient.execute(httpPost);
            // Extract data from the response.
            HttpEntity httpEntity = httpResponse.getEntity();
            // Open an inputStream with the data content.
            instream = httpEntity.getContent();

        }  catch (Exception e) {
            e.printStackTrace();
        }

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"), 8);
            StringBuilder result = new StringBuilder();
            String data = null;

            // Build the string until null.
            while ((data = reader.readLine()) != null) {
                result.append(data + "\n");
            }

            // Close the input stream.
            instream.close();
            // Convert the string builder data to an actual string.
            json = result.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error " + e.toString());
        }

        // Try to parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (Exception e) {
            Log.e("JSON Parser", "Error" + e.toString());
        }

        // Return the JSON Object.
        return jObj;

    }

    public String toJson(ArrayList<HashMap<String, String>> coachesList) {
        // TODO Auto-generated method stub
        return null;
    }





}
