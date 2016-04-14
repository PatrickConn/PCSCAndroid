package com.example.patrick.pcsc;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


//this is the mnau menu for the app
public class MainActivity extends ActionBarActivity {
    private static String LOG_TAG;
    boolean test;
    String login = "1";
    @Override
    //this will create the screen for th e mane menu
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //see login in value for shared preferernes
        String KEY = "login";
        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);
        login = pref.getString(KEY, "0");
    }
    // this will take the user to the login screeen
    public void onClick1(View view) {
        // PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("login", "0").commit();
        // test if the is an internet conect if not the will tell the uaer they need one
        test = isNetworkAvailable();
        if(test == false)
        {

            Toast.makeText(getApplicationContext(), "Sorry you have no internet connection",
                    Toast.LENGTH_LONG).show();
        }
        else {

            Intent NEW = new Intent(this, login.class);
            startActivity(NEW);

        }
    }
    //this is for the sign up
    public void onClick2(View view) {
        // PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("login", "0").commit();

        test = isNetworkAvailable();
        if(test == false){
            Toast.makeText(getApplicationContext(), "Sorry you have no internet connection",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Intent NEW = new Intent(this, Sign_Up.class);
            startActivity(NEW);

        }
    }
    // this for the buy a new card
    public void onClick3(View view) {
        //PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("login", "0").commit();

        test = isNetworkAvailable();
        if(test == false){

            Toast.makeText(getApplicationContext(), "Sorry you have no internet connection",
                    Toast.LENGTH_LONG).show();
        }
        //this see if the user is login in are not if not then the user will be tolde to login
        else {
            if(login.equals("0"))
            {
                Toast.makeText(getApplicationContext(), "Sorry you have to login are sign up",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Intent NEW = new Intent(this, buyAcard.class);
                startActivity(NEW);
            }

        }
    }
    public void onClick8(View view) {
        //PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("login", "0").commit();

        test = isNetworkAvailable();
        if(test == false){
            Toast.makeText(getApplicationContext(), "Sorry you have no internet connection",
                    Toast.LENGTH_LONG).show();
        }
        else {
            if(login.equals("0"))
            {
                Toast.makeText(getApplicationContext(), "Sorry you have to login are sign up",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Intent NEW = new Intent(this, Pay_Pal.class);
                startActivity(NEW);
            }

        }
    }
    public void onClick9(View view) {
        //PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("login", "0").commit();

        test = isNetworkAvailable();
        if(test == false){
            Toast.makeText(getApplicationContext(), "Sorry you have no internet connection",
                    Toast.LENGTH_LONG).show();
        }
        else {
            if(login.equals("0"))
            {
                Toast.makeText(getApplicationContext(), "Sorry you have to login are sign up",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Intent NEW = new Intent(this, checkMyBalance.class);
                startActivity(NEW);
            }

        }
    }
    public void onClick10(View view) {
        //PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("login", "0").commit();

        test = isNetworkAvailable();
        if(test == false){
            Toast.makeText(getApplicationContext(), "Sorry you have no internet connection",
                    Toast.LENGTH_LONG).show();
        }
        else {
            if(login.equals("0"))
            {
                Toast.makeText(getApplicationContext(), "Sorry you have to login are sign up",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Intent NEW = new Intent(this, PayOut.class);
                startActivity(NEW);
            }

        }
    }
    // this will log the user out and set all if the Shared Preferences to 0
    public void LogOut(View view) {
        String KEY = "login";
        // Getting Array of Coaches
        SharedPreferences myprefs= getApplicationContext().getSharedPreferences("Users_Id", MODE_WORLD_READABLE);
        myprefs.edit().putString("Users_Id", null).commit();
        //looping through All Coaches
        String result = "0";
        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);

        //Storing the string in pref file
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(KEY, result);
        prefEditor.commit();
        Toast.makeText(getApplicationContext(), "You have log out",
                Toast.LENGTH_LONG).show();
        Intent NEW = new Intent(this, MainActivity.class);
        startActivity(NEW);
    }
    //this is where the app see if the user has an internet connnection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public boolean hasActiveInternetConnection() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(LOG_TAG, "No network available!");
        }
        return false;
    }

}
