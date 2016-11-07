package com.lol.majchin.findphones;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by majch on 08-10-2016.
 */
public class advertDetailsActivity extends AppCompatActivity {


    int id ;

    TextView phonename , phonemodel , phonecompany, phoneprice,phonedescription, selleremail,selleremail2 , sellercontact ;
    ImageView phoneimage ;

    JSONObject post ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");

        phonename = (TextView)findViewById(R.id.txt_ad_phonename_v);
        phonecompany = (TextView)findViewById(R.id.txt_ad_phonecompany_v);
        phonemodel = (TextView)findViewById(R.id.txt_ad_phonemodel_v);
        phoneprice = (TextView)findViewById(R.id.txt_ad_phoneprice_v);
        phonedescription = (TextView)findViewById(R.id.txt_ad_phonedescription_v);
        sellercontact = (TextView)findViewById(R.id.txt_ad_sellerphone_v);
        selleremail = (TextView)findViewById(R.id.txt_ad_selleremail_v);
        selleremail2 = (TextView)findViewById(R.id.txt_ad_selleremail2_v);
        phoneimage = (ImageView)findViewById(R.id.img_ad_main);

        new PostAsync().execute(Integer.toString(id));
    }





















    class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
        private static final String POSTS_URL = "http://chinmeshmanjrekar.co.nf/AndroidUploadImage/getpost.php";

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        private static final String TAG_POSTS = "post";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(advertDetailsActivity.this);
            pDialog.setMessage("Fetching data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("id",args[0]);// how many to fetch
                //params.put("password", args[1]);
                Log.d("request", "starting");
                JSONObject json = jsonParser.makeHttpRequest(
                        POSTS_URL, "POST", params);
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
            //JSONObject row = new JSONObject() ;

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (json != null) {
                //Toast.makeText(HomeActivity.this, json.toString(), Toast.LENGTH_LONG ).show(); // must be commented finally
                try {
                    success = json.getInt(TAG_SUCCESS);
                    message = json.getString(TAG_MESSAGE);
                    post = json.getJSONObject(TAG_POSTS) ;


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

                Toast.makeText(advertDetailsActivity.this, "Network Error", Toast.LENGTH_LONG ).show();

            }


            if (success == 1) {

                Log.d("Success!", message);

                try {
                    //row = posts.getJSONObject(0);

                    phonename.setText(post.getString("phonename"));
                    phonemodel.setText(post.getString("modelname"));
                    phonecompany.setText(post.getString("companyname"));
                    phoneprice.setText(post.getString("sellingprice"));
                    phonedescription.setText(post.getString("description"));
                    selleremail.setText(post.getString("selleremail1"));
                    selleremail2.setText(post.getString("selleremail2"));
                    sellercontact.setText(post.getString("sellerphoneno"));


                    byte[] decodedString = Base64.decode(post.getString("imagedata"), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
                    phoneimage.setImageBitmap(decodedByte);


                }catch (JSONException je){
                    Log.e("JSON Post","Problem with posts data");
                    je.printStackTrace();
                }

            }else{
                Toast.makeText(advertDetailsActivity.this, message , Toast.LENGTH_LONG ).show();
                Log.d("Failure", message);
            }

        }
    }








}
