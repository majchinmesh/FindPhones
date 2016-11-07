package com.lol.majchin.findphones;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by majch on 09-10-2016.
 */
public class TestActivity extends AppCompatActivity {


    ImageView main ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        main = (ImageView) findViewById(R.id.img_t_main);
        new PostAsync().execute("sent");
    }



    class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
        private static final String TEST_URL = "http://chinmeshmanjrekar.co.nf/AndroidUploadImage/test.php";
        //private static final String TEST_URL = "http://findphones.hostfree.pw/mail2/send.php";
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_IMAGE = "image";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(TestActivity.this);
            pDialog.setMessage("Attempting test...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            //Toast.makeText(TestActivity.this, "Pre execution done", Toast.LENGTH_LONG ).show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("sent", args[0]);
                //params.put("password", args[1]);
                Log.d("request", "starting");
                JSONObject json = jsonParser.makeHttpRequest(
                        TEST_URL, "POST", params);
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

            //Toast.makeText(TestActivity.this,"Length of json "+ json.length() , Toast.LENGTH_LONG ).show();

            int success = 0;
            String image = "";
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (json != null) {
                //Toast.makeText(TestActivity.this, json.toString(), Toast.LENGTH_LONG ).show(); // must be commented finally
                try {
                    success = json.getInt(TAG_SUCCESS);
                    image = json.getString(TAG_IMAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

                Toast.makeText(TestActivity.this, "Network Error", Toast.LENGTH_LONG ).show();

            }


            if (success == 1) {

                Log.d("Success!", " image retrived ");


                byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
                main.setImageBitmap(decodedByte);

                // convert string to image and display

            }else{
                Toast.makeText(TestActivity.this, " image Not retrived " , Toast.LENGTH_LONG ).show();
                Log.d("Failure", " image Not retrived ");
            }

            //Toast.makeText(TestActivity.this, "Post execution done", Toast.LENGTH_LONG ).show();
        }
    }





}
