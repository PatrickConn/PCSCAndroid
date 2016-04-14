package com.example.patrick.pcsc;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
//this is the sign up screen
public class Sign_Up extends ActionBarActivity{
    String  name;
    String test;
    private EditText User;
    private EditText Pass;
    private EditText Email;
    private EditText Fname;
    private EditText Lname;
    String userName;
    String userPass;
    String userEmail;
    String userFname;
    String userLname;
    Intent NEW;
    JSONArray productObj;
    String User_ID = "Error";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        User = (EditText) findViewById(R.id.SUP_UserName2);
        Pass = (EditText) findViewById(R.id.SUP_PassWord1);
        Email = (EditText) findViewById(R.id.SUP_Email1);
        Fname = (EditText) findViewById(R.id.SUP_firstName1);
        Lname = (EditText) findViewById(R.id.SUP_LastName1);

        Intent myIntent = getIntent();
        name = myIntent.getStringExtra("Activity");

    }

    public void onClick(View view) {

        NEW = new Intent(this, MainActivity.class);
        userName = User.getText().toString();
        userPass = Pass.getText().toString();
        userEmail = Email.getText().toString();
        userFname = Fname.getText().toString();
        userLname = Lname.getText().toString();
        //this make shore the user has enter the info
        if (userName.trim().equals("") || userPass.trim().equals("") || userEmail.trim().equals("") || userFname.trim().equals("") || userLname.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Sorry you need to fill in all the felads",
                    Toast.LENGTH_LONG).show();
        } else {
            new Sign_up_run().execute();
        }
    }
    public void Back(View view) {

        Intent in = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(in);;
    }


    class Sign_up_run extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        private String Login;
        //this is the link to the sevre the app need to ge the card
        private static final String urlGet = "http://patrickpcsc.pythonanywhere.com/android/Sign_up";

        @Override
        protected void onPreExecute() {
            //this will show a message on the screen while the app get the value of the card
            super.onPreExecute();
            pDialog = new ProgressDialog(Sign_Up.this);
            pDialog.setMessage("Loading Users details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... params) {
            try{
                //this is where the user info is encryped so is can sent of a network
                userName = encrypt(userName);
                userPass = encrypt(userPass);
                //userEmail = encrypt(userEmail);
                userFname = encrypt(userFname);
                userLname = encrypt(userLname);

            } catch (Exception e){

            }

            // Building Parameters
            //this send the user username and id of the json class so it can then it can be add to the query og the sever
            List<NameValuePair> query = new ArrayList<NameValuePair>();
            query.add(new BasicNameValuePair("User", userName));
            query.add(new BasicNameValuePair("Pass", userPass));
            query.add(new BasicNameValuePair("Email", userEmail.trim()));
            query.add(new BasicNameValuePair("firstName", userFname));
            query.add(new BasicNameValuePair("lastName", userLname));
            //add User details by making HTTP request
            //Note that User details url will use GET reque
            JSONParser json2 = new JSONParser();
            JSONObject json = json2.makeHttpRequest(urlGet, "GET", query);
            try {
                productObj = json.getJSONArray("Add"); // JSON Array
                JSONObject c = productObj.getJSONObject(0);
                //get the frist element to id if the users datils where add the the databases of if they need too try signingup agen.
                Login = c.getString("Login");
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


                //PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("login", "1").commit();
                pDialog = new ProgressDialog(Sign_Up.this);
                pDialog.setMessage("Thank you for signing up. Now you need to confrom your e-mail address then you will be able sign in and play.");
                pDialog.setCancelable(false);
                pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pDialog.dismiss();
                        startActivity(NEW);
                    }
                });
                pDialog.show();

            }else{

                pDialog = new ProgressDialog(Sign_Up.this);
                pDialog.setMessage(User_ID);
                pDialog.setCancelable(false);
                pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pDialog.dismiss();
                        startActivity(NEW);
                    }
                });
                pDialog.show();


            }

        }//onPostExecute
    }
    protected String encrypt(String token) throws Exception {
        // Instantiate the cipher
        final SecretKeySpec key = new SecretKeySpec("password".getBytes("ISO-8859-1"), "DES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec("password".getBytes());

        Cipher cipher = Cipher.getInstance("DES/CFB8/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        byte[] binaryData = cipher.doFinal(token.getBytes("ISO-8859-1"));

        return new String(org.apache.commons.codec.binary.Base64.encodeBase64(binaryData), "ISO-8859-1");

    }

}