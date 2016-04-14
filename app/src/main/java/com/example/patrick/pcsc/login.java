package com.example.patrick.pcsc;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
//this is the login screen
public class login extends ActionBarActivity{
	String  name;
	String test;
	private EditText User;
	private EditText Pass;
	Intent NEW;
	JSONArray productObj;
	String userlogin;
	String Password;
	String User_ID = "Error";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		User = (EditText) findViewById(R.id.CoachesId);
		Pass = (EditText) findViewById(R.id.addname);
		Intent myIntent = getIntent();
		name = myIntent.getStringExtra("Activity");


	}

	public void onClick(View view) {

		NEW = new Intent(this, MainActivity.class);
		userlogin = User.getText().toString();
		Password = Pass.getText().toString();
		//make shore that the user has input
		if (userlogin.trim().equals("") || Password.trim().equals("") ) {
			Toast.makeText(getApplicationContext(), "Sorry you need to fill in all the felads",
					Toast.LENGTH_LONG).show();
		} else {
			new	Login().execute();
		}


	}
	public void Back(View view) {

		Intent in = new Intent(getApplicationContext(),MainActivity.class);
		startActivity(in);;
	}



	class Login extends AsyncTask<String, String, String> {



		private ProgressDialog pDialog;
		private String Login;
		//this is the link to the sevre the app need to ge the card
		private static final String urlGet = "http://patrickpcsc.pythonanywhere.com/login/andriod";

		@Override
		protected void onPreExecute() {
			//this will show a message on the screen while the app get the value of the card
			super.onPreExecute();
			pDialog = new ProgressDialog(login.this);
			pDialog.setMessage("Loading Users details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		protected String doInBackground(String... params) {

			try{
				//this is where the user info is encryped so is can sent of a network
				userlogin = encrypt(userlogin);
				Password = encrypt(Password);
			} catch (Exception e){

			}
			// Building Parameters
			//this send the user username and id of the json class so it can then it can be add to the query og the sever
			List<NameValuePair> query = new ArrayList<NameValuePair>();
			query.add(new BasicNameValuePair("User", userlogin));
			query.add(new BasicNameValuePair("Pass", Password));

			test = test + userlogin;
			//getting user details by making HTTP request
			//Note that user details url will use GET reque
			JSONParser json2 = new JSONParser();
			JSONObject json = json2.makeHttpRequest(urlGet, "GET", query);
			//Log.d("Geting Users ", json.toString());
			try {
				productObj = json.getJSONArray("login"); // JSON Array
				// get first user object from JSON Array
				JSONObject c = productObj.getJSONObject(0);
				// user with this pid found
				Login = c.getString("Login");
				if(Login.equals("1"))
				{
					//get the suer id for the srver
					c = productObj.getJSONObject(1);
					User_ID = c.getString("User_Id");


				}
				if (Login.equals("2"))
				{
					c = productObj.getJSONObject(1);
					User_ID = c.getString("Error");
				}
				if (Login.equals("3"))
				{
					c = productObj.getJSONObject(1);
					User_ID = c.getString("Error");
				}

				// display user data in EditText
			}//try
			catch (JSONException e)
			{	e.printStackTrace();	}//catch
			return null;
		}//doInBackground
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			//this if the login was good
			if (Login.equals("1"))
			{
				//set the user username and id so it can be user later
				SharedPreferences myprefs= getApplicationContext().getSharedPreferences("Users_Id", MODE_WORLD_READABLE);
				myprefs.edit().putString("Users_Id", User_ID).commit();

				SharedPreferences User = getApplicationContext().getSharedPreferences("UserName", MODE_WORLD_READABLE);
				User.edit().putString("UserName", userlogin).commit();
				String KEY = "login";


				//set the login to 1 so the user can play and add moeny
				String result = "1";
				SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);

				//Storing the string in pref file
				SharedPreferences.Editor prefEditor = pref.edit();
				prefEditor.putString(KEY, result);
				prefEditor.commit();

				PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("login", "1").commit();
				pDialog = new ProgressDialog(login.this);
				pDialog.setMessage("Thank you for sign in. you can now go play.");
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

				pDialog = new ProgressDialog(login.this);
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
	}//login
	// this is where the user info is encrypt befor it is sent to the srvear
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