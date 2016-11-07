package com.lol.majchin.findphones;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.asynctask.PostResponseAsyncTask ;

import com.kosalgeek.asynctask.AsyncResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;

import com.lol.majchin.findphones.HomeActivity ;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail , etPassword ;
    Button btnLogin , btnRegister;

    String email ;

    SharedPreferences loginstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);





        etEmail = (EditText)findViewById(R.id.et_l_email);
        etPassword = (EditText)findViewById(R.id.et_l_password);
        btnLogin = (Button)findViewById(R.id.btn_l_login);
        btnRegister = (Button)findViewById(R.id.btn_l_register);

        btnLogin.setOnClickListener(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registration();
            }
        });
    }



    public void registration(){

        Toast.makeText(this, "Fill The Form To Register", Toast.LENGTH_LONG).show();

        Intent gotoRegister = new Intent(LoginActivity.this, RegistrationActivity.class);

        LoginActivity.this.startActivity(gotoRegister);

        LoginActivity.this.finish();
    }

    @Override
    public void onClick(View v) {

        email = etEmail.getText().toString() ;
        String password = etPassword.getText().toString()  ;
        new PostAsync().execute(email, password);

    }




    class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
        private static final String LOGIN_URL = "http://chinmeshmanjrekar.co.nf/client/login.php";
        //private static final String LOGIN_URL = "http://findphones.hostfree.pw/mail2/send.php";
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", args[0]);
                params.put("password", args[1]);
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
                Toast.makeText(LoginActivity.this, json.toString(), Toast.LENGTH_LONG ).show(); // must be commented finally
                try {
                    success = json.getInt(TAG_SUCCESS);
                    message = json.getString(TAG_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

                Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_LONG ).show();

            }


            if (success == 1) {

                Log.d("Success!", message);

                SaveSharedPreference.setUser(getApplicationContext() , email);

                Intent gotoHome = new Intent(LoginActivity.this, HomeActivity.class);

                LoginActivity.this.startActivity(gotoHome);

                LoginActivity.this.finish();


            }else{
                Toast.makeText(LoginActivity.this, message , Toast.LENGTH_LONG ).show();
                Log.d("Failure", message);
            }
        }
    }



    class GetAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
        private static final String LOGIN_URL = "http://chinmeshmanjrekar.co.nf/client/login.php";
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("name", args[0]);
                params.put("password", args[1]);
                Log.d("request", "starting");
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "GET", params);
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
                Toast.makeText(LoginActivity.this, json.toString(),
                        Toast.LENGTH_LONG).show();
                try {
                    success = json.getInt(TAG_SUCCESS);
                    message = json.getString(TAG_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (success == 1) {
                Log.d("Success!", message);
            }else{
                Log.d("Failure", message);
            }
        }
    }

}