package com.lol.majchin.findphones;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by majch on 02-10-2016.
 */
public class RegistrationActivity extends AppCompatActivity implements AsyncResponse,View.OnClickListener {

    String Vcode ;

    Button btnRegister ;
    EditText  etFullname , etPassword , etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_main);


        btnRegister = (Button)findViewById(R.id.btn_r_register);

        etFullname = (EditText)findViewById(R.id.et_r_fullname) ;
        etPassword = (EditText)findViewById(R.id.et_r_password);
        etEmail = (EditText)findViewById(R.id.et_r_email);

        btnRegister.setOnClickListener(this) ;
    }


    @Override
    public void processFinish(String eresult) {

        String status = new String();



        try {
            Log.d("CREATION" , "Status : "+status);
            JSONObject json= new JSONObject(eresult);
            status = (String) json.getString("status");

        } catch (JSONException e) {
            Log.d("CREATION" , "Status : "+status);
            Log.d("CREATION" , "Jobject : "+eresult);
            e.printStackTrace();
        }

        if (status.equals("success")) {





            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Go to Your email for Verification", Toast.LENGTH_LONG).show();

            Intent gotoLogin = new Intent(RegistrationActivity.this, LoginActivity.class);
            RegistrationActivity.this.startActivity(gotoLogin);
            RegistrationActivity.this.finish();
        }
        else if (status.equals("userid_taken")){

            Toast.makeText(this, "UserID already taken", Toast.LENGTH_LONG).show();
        }
        else {

            Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {


        Vcode = vcode.getVcode();
        String email = etEmail.getText().toString() ;
        String password = etPassword.getText().toString()  ;
        String fullname = etFullname.getText().toString()  ;
        String devicename = Device.getDeviceName() ;

        if(email.length() != 0 && password.length() != 0 && fullname.length() != 0) {

            new PostAsync().execute(fullname, email, password, devicename, Vcode);
        }else{
            Toast.makeText(this, "Some of the field's are missing.", Toast.LENGTH_LONG).show();
        }


    }




    class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
        private static final String LOGIN_URL = "http://chinmeshmanjrekar.co.nf/client/register.php";
        //private static final String LOGIN_URL = "http://findphones.hostfree.pw/mail2/send.php";
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(RegistrationActivity.this);
            pDialog.setMessage("Attempting registration...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("fullname", args[0]);
                params.put("email", args[1]);
                params.put("password", args[2]);
                params.put("devicename", args[3]);
                params.put("vcode", args[4]);


                Log.d("request", "starting");
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);
                if (json != null) {
                    Log.d("JSON result", json.toString());
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            int success = 0;
            String message = "";
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (json != null) {
                Toast.makeText(RegistrationActivity.this, json.toString(), Toast.LENGTH_LONG ).show(); // must be commented finally
                try {
                    success = json.getInt(TAG_SUCCESS);
                    message = json.getString(TAG_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

                Toast.makeText(RegistrationActivity.this, "Network Error", Toast.LENGTH_LONG ).show();

            }


            if (success == 1) {

                Log.d("Success!", message);

                Intent gotoLogin = new Intent(RegistrationActivity.this, LoginActivity.class);

                RegistrationActivity.this.startActivity(gotoLogin);

                RegistrationActivity.this.finish();

                Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_LONG ).show();

            }else{
                Log.d("Failure", message);
                Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_LONG ).show();
            }
        }

    }









}





