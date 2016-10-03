package com.lol.majchin.findphones;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by majch on 02-10-2016.
 */
public class HomeActivity extends AppCompatActivity{

    Button btnLogout ;
    String UserID ;

    protected void onCreate(Bundle savedInstanceState) {

        UserID =  SaveSharedPreference.getUserName(getApplicationContext()) ;

        if(UserID.length() == 0)
        {
            Intent gotoLogin = new Intent(HomeActivity.this, LoginActivity.class);
            HomeActivity.this.startActivity(gotoLogin);
            HomeActivity.this.finish();
        }
        else
        {
            // Stay at the current activity.
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            Toast.makeText(this,"Welcome "+UserID, Toast.LENGTH_LONG).show();

        }


        btnLogout = (Button)findViewById(R.id.btn_h_logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
            }
        });


    }

    public void logout(){

        SaveSharedPreference.setUserName(getApplicationContext() ,"" );

        Toast.makeText(this, "Goodbye " + UserID, Toast.LENGTH_LONG).show();
        Intent gotoLogin = new Intent(HomeActivity.this, LoginActivity.class);
        HomeActivity.this.startActivity(gotoLogin);
        HomeActivity.this.finish();
    }




}
