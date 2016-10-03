package com.lol.majchin.findphones;

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

            //Mailer.sendMail(etEmail.getText().toString(),Vcode);

            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Go to Your email for Verification", Toast.LENGTH_LONG).show();
            //Toast.makeText(this, "Go Back To Login", Toast.LENGTH_LONG).show();
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

        HashMap postData = new HashMap();
        postData.put("mobile", "android");
        postData.put("txtPassword", etPassword.getText().toString());
        postData.put("txtFullname", etFullname.getText().toString());
        postData.put("txtEmail", etEmail.getText().toString() );
        postData.put("vcode",  Vcode );

        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
        //task.execute("http://192.168.1.2/client/register.php");
        task.execute("http://chinmeshmanjrekar.co.nf/client/register.php");

    }
}
