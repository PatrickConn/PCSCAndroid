package com.example.patrick.pcsc;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//this is where the user buy new cards
public class buyAcard extends ActionBarActivity{
    String  name;
    Intent NEW;
    JSONArray productObj;
    String User_ID = "Error";
    String NewBalance = "Error";
    String OLDBalance = "Error";
    String winOrLose = "Error";
    String Value = "Error";
    Button button;
    ImageView image;
    String UserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_a_card);
        Intent myIntent = getIntent();
        name = myIntent.getStringExtra("Activity");
        //get the user name and id for memuy
        SharedPreferences myprefs= getSharedPreferences("Users_Id", MODE_WORLD_READABLE);
        User_ID = myprefs.getString("Users_Id", null);
        SharedPreferences myprefs2= getSharedPreferences("UserName", MODE_WORLD_READABLE);
        UserName = myprefs2.getString("UserName", null);
        NEW = new Intent(this, MainActivity.class);
        //it stars be buying one card the
        new	Buy_A_Card().execute();
        image = (ImageView) findViewById(R.id.imageView1);
        button = (Button) findViewById(R.id.btnChangeImage);

    }

    public void onClick(View view) {
        //this is if the user hits the but new button on the screen
        Intent in = new Intent(getApplicationContext(),buyAcard.class);
        startActivity(in);;
    }

    public void Back(View view) {
        //this will take the user back to the home screen
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(in);
        ;
    }
            //this is when the user hit the scratch card button it will get the value an see if it euqus 0,10,25,50,100,200
            //and get the `corresponding image to mach the card
            public void ScratchAcards(View arg0) {
                // this is a an array of image of if the vales is a 0
                 int[] lost = {
                        R.mipmap.lose1,
                        R.mipmap.lose2,
                        R.mipmap.lose3,
                };
                if(Value.equals("0")) {
                    //if the value is 0 the it will pick an random number between 0 and 2
                    // then set the random image to the screen
                    Random r = new Random();
                    image.setImageResource(lost[r.nextInt(2 - 0 + 1) + 0]);
                }
                if(Value.equals("10")) {
                    image.setImageResource(R.mipmap.win10);
                }
                if(Value.equals("25")) {
                    image.setImageResource(R.mipmap.win20);
                }
                if(Value.equals("50")) {
                    image.setImageResource(R.mipmap.win50);
                }
                if(Value.equals("100")) {
                    image.setImageResource(R.mipmap.win100);
                }
                if(Value.equals("200")) {
                    image.setImageResource(R.mipmap.win200);
                }
            }

    //this is how the app get the value of the card
    class Buy_A_Card extends AsyncTask<String, String, String> {



        private ProgressDialog pDialog;
        private String Login;
        //this is the link to the sevre the app need to ge the card
        private static final String urlGet = "http://patrickpcsc.pythonanywhere.com/android/BuyACard";

        @Override
        protected void onPreExecute() {
            //this will show a message on the screen while the app get the value of the card
            super.onPreExecute();
            pDialog = new ProgressDialog(buyAcard.this);
            pDialog.setMessage("Checking Account...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... params) {

            // Building Parameters
            //this send the user username and id of the json class so it can then it can be add to the query og the sever
            List<NameValuePair> query = new ArrayList<NameValuePair>();
            query.add(new BasicNameValuePair("User_id", User_ID));
            query.add(new BasicNameValuePair("UserName", UserName));

            //getting Users details by making HTTP request
            //Note that Users details url will use GET reque
            JSONParser json2 = new JSONParser();
            JSONObject json = json2.makeHttpRequest(urlGet, "GET", query);

            try {
                productObj = json.getJSONArray("buy"); // JSON Array
                // get first Users object from JSON Array
                JSONObject c = productObj.getJSONObject(0);
                // this makes shore the user can bay a card
                Login = c.getString("buyAcard");
                if(Login.equals("1"))
                {
                    c = productObj.getJSONObject(1);
                    NewBalance = c.getString("NewBalance");
                    c = productObj.getJSONObject(2);
                    OLDBalance = c.getString("OLDBalance");
                    c = productObj.getJSONObject(3);
                    winOrLose = c.getString("winOrLose");
                    c = productObj.getJSONObject(4);
                    Value = c.getString("Value");
                }
                if (Login.equals("0"))
                {
                    c = productObj.getJSONObject(1);
                    NewBalance = c.getString("Error");
                }

            }//try
            catch (JSONException e)
            {	e.printStackTrace();	}//catch

            return null;
        }//doInBackground
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            //if the user has eugth money and it is a user the so the user can sraches the card
            if (Login.equals("1"))
            {
                pDialog = new ProgressDialog(buyAcard.this);
                pDialog.setMessage("Thank you.You can now play the card.");
                pDialog.setCancelable(false);
                pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pDialog.dismiss();
                    }
                });
                pDialog.show();
            }else{
                //else it will show the error got form the srever
                pDialog = new ProgressDialog(buyAcard.this);
                pDialog.setMessage(NewBalance);
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


}