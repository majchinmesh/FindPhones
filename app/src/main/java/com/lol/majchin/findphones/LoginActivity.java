package com.lol.majchin.findphones;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


public class LoginActivity extends AppCompatActivity implements AsyncResponse,View.OnClickListener {

    EditText etEmail , etPassword ;
    Button btnLogin , btnRegister;

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
        //myIntent.putExtra("key", value); //Optional parameters
        LoginActivity.this.startActivity(gotoRegister);

        LoginActivity.this.finish();
    }



    @Override
    public void processFinish(String eresult ) {

        String status = new String();

        try {
            JSONObject json= (JSONObject) new JSONTokener(eresult).nextValue();
            status = (String) json.get("status");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (status.equals("success")) {

            Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show();

            SaveSharedPreference.setUserName(getApplicationContext() ,etEmail.getText().toString() );

            Intent gotoHome = new Intent(LoginActivity.this, HomeActivity.class);

            LoginActivity.this.startActivity(gotoHome);

            LoginActivity.this.finish();
        }

        else {

            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();

        }






    }

    @Override
    public void onClick(View v) {

        HashMap postData = new HashMap();
        postData.put("mobile", "android");
        postData.put("txtEmail", etEmail.getText().toString());
        postData.put("txtPassword", etPassword.getText().toString());

        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
        //task.execute("http://192.168.1.2/client/login.php");
        task.execute("http://chinmeshmanjrekar.co.nf/client/login.php");


    }

}
