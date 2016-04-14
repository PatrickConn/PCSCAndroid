package com.example.patrick.pcsc;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
//this is where the user withdraws the money from th account on the app to there paypal account
public class PayOut extends ActionBarActivity{

    private TextView User;
    JSONArray productObj;
    String User_ID = "Error";
    String updateUser = "Error";
    NumberPicker picker;
    String PaypalEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payout);
        User = (TextView) findViewById(R.id.CoachesId);
        new	checkBalance().execute();
        picker = (NumberPicker) findViewById(R.id.numberPicker);
        picker.setMinValue(0);
        new	checkBalance().execute();


    }

    public void onClick(View view) {
        PaypalEmail = User.getText().toString();
        new	withdraw().execute();
    }
    public void Back(View view) {

        Intent in = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(in);;
    }


    class checkBalance extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        private String Login;
        //this is the link to the sevre the app need to ge the card
        private static final String urlGet = "http://patrickpcsc.pythonanywhere.com/android/CheckMyBalanece/payout";
        Intent in = new Intent(getApplicationContext(),MainActivity.class);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PayOut.this);
            pDialog.setMessage("Loading Users details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... params) {

            //get the user name and id for memuy
            String KEY = "UserName";
            SharedPreferences pref = getApplicationContext().getSharedPreferences("UserName", 0);
            String UserName = pref.getString(KEY, "0");

            String KEY2 = "Users_Id";
            SharedPreferences pref2 = getApplicationContext().getSharedPreferences("Users_Id", 0);
            String Users_Id = pref2.getString(KEY2, "0");
            // Building Parameters
            //this send the user username and id of the json class so it can then it can be add to the query og the sever
            List<NameValuePair> query = new ArrayList<NameValuePair>();
            query.add(new BasicNameValuePair("User", UserName));
            query.add(new BasicNameValuePair("User_Id", Users_Id));
            //add User details by making HTTP request
            //Note that User details url will use GET reque
            JSONParser json2 = new JSONParser();
            JSONObject json = json2.makeHttpRequest(urlGet, "GET", query);
            try {
                productObj = json.getJSONArray("Add"); // JSON Array
                JSONObject c = productObj.getJSONObject(0);
                //get the frist element to id if the users datils where add the the databases of if they need too try signingup agen.
                Login = c.getString("Login");
                if(Login.equals("1"))
                {
                    //if the wihtdraw was good
                    //shows the massage for the sever
                    c = productObj.getJSONObject(1);
                    User_ID = c.getString("Balance");

                }
                if (Login.equals("2"))
                {
                    //get the error maesgges for the svvear
                    c = productObj.getJSONObject(1);
                    User_ID = c.getString("Error");
                }

            }//try
            catch (JSONException e)
            {	e.printStackTrace();	}//catch

            return null;
        }//doInBackground
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (Login.equals("1"))
            {

               //is set the max vlaue the picke go to and it is the uer balance
                picker.setMaxValue((int)Double.parseDouble(User_ID));

            }else{

                pDialog = new ProgressDialog(PayOut.this);
                pDialog.setMessage(User_ID);
                pDialog.setCancelable(false);
                pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pDialog.dismiss();
                        // Intent in = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(in);;
                    }
                });
                pDialog.show();


            }
        }//onPostExecute
    }

    class withdraw extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        private String Login;
        private static final String urlGet2 = "http://patrickpcsc.pythonanywhere.com/android/payout";
        Intent in = new Intent(getApplicationContext(),MainActivity.class);
        String  Amont = Integer.toString(picker.getValue());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PayOut.this);
            pDialog.setMessage("Loading Users details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... params) {


            String KEY = "UserName";
            SharedPreferences pref = getApplicationContext().getSharedPreferences("UserName", 0);
            String UserName = pref.getString(KEY, "0");

            String KEY2 = "Users_Id";
            SharedPreferences pref2 = getApplicationContext().getSharedPreferences("Users_Id", 0);
            String Users_Id = pref2.getString(KEY2, "0");
            // Building Parameters
            List<NameValuePair> query = new ArrayList<NameValuePair>();
            query.add(new BasicNameValuePair("User", UserName));
            query.add(new BasicNameValuePair("User_Id", Users_Id));
            query.add(new BasicNameValuePair("PayEmail", PaypalEmail));
            query.add(new BasicNameValuePair("amount", Amont));
            //add User details by making HTTP request
            //Note that User details url will use GET reque
            JSONParser json3 = new JSONParser();
            JSONObject json4 = json3.makeHttpRequest(urlGet2, "GET", query);
            try {
                productObj = json4.getJSONArray("Add"); // JSON Array
                JSONObject c1 = productObj.getJSONObject(0);
                //get the frist element to id if the users datils where add the the databases of if they need too try signingup agen.
                Login = c1.getString("Login");
                if(Login.equals("1"))
                {
                    // get the users id so it can be use later
                    c1 = productObj.getJSONObject(1);
                    updateUser = c1.getString("Balance");

                }
                if (Login.equals("2"))
                {
                    //get the error maesgges for the svvear
                    c1 = productObj.getJSONObject(1);
                    updateUser = c1.getString("Error");
                }

            }//try
            catch (JSONException e)
            {	e.printStackTrace();	}//catch

            return null;
        }//doInBackground
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (Login.equals("1"))
            {
                pDialog = new ProgressDialog(PayOut.this);
                pDialog.setMessage(updateUser);
                pDialog.setCancelable(false);
                pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pDialog.dismiss();
                        // Intent in = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(in);;
                    }
                });
                pDialog.show();

            }else{

                pDialog = new ProgressDialog(PayOut.this);
                pDialog.setMessage(updateUser);
                pDialog.setCancelable(false);
                pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pDialog.dismiss();
                        // Intent in = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(in);;
                    }
                });
                pDialog.show();


            }
        }//onPostExecute
    }

}