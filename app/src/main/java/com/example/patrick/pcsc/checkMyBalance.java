package com.example.patrick.pcsc;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class checkMyBalance extends ActionBarActivity{

    private TextView User;
    Intent NEW;
    JSONArray productObj;
    String User_ID = "Error";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ckeck_my_balance);
        User = (TextView) findViewById(R.id.CoachesId);
        new	checkBalance().execute();

    }

    public void onClick(View view) {
        //this will lets the user go the paypal class so they can add money
        Intent in = new Intent(getApplicationContext(), Pay_Pal.class);
        startActivity(in);;
    }
    public void Back(View view) {

        Intent in = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(in);;
    }


    class checkBalance extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        private String Login;
        //this is the link to the sevre the app need to ge the card
        private static final String urlGet = "http://patrickpcsc.pythonanywhere.com/android/CheckMyBalanece";
        Intent in = new Intent(getApplicationContext(),MainActivity.class);
        @Override
        protected void onPreExecute() {
            //this will show a message on the screen while the app get the value of the card
            super.onPreExecute();
            pDialog = new ProgressDialog(checkMyBalance.this);
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
                    // get the users id so it can be use later
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
            //this then sent the textview to the user balanes
            if (Login.equals("1"))
            {
                User.setText(User_ID);

            }else{
                ////else it will show the error got form the srever
                pDialog = new ProgressDialog(checkMyBalance.this);
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

}