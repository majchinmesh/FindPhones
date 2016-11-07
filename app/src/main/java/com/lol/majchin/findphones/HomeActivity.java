package com.lol.majchin.findphones;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by majch on 02-10-2016.
 */
public class HomeActivity extends AppCompatActivity  {

    int POSTS_COUNT = 20 ;


    Button btnLogout , btnPost ;
    String UserID ;
    String[] StringArray = { " \tYou will use Android Studio IDE to create an Android application and name it as ListDisplay under a package com.example.ListDisplay as explained in the Hello World Example chapter", "Modify the default content of res/layout/activity_main.xml file to include ListView content with the self explanatory attributes.", "No need to change string.xml, Android studio takes care of default string constants." };

    TextView homeTxt ;
    JSONArray posts ;

    ListModel List_row ;
    //ListModel[] postsListDataArray = new  ListModel[POSTS_COUNT] ;

    ListView list;
    CustomAdapter adapter;
    public  HomeActivity CustomListView = null;
    public ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();


    protected void onCreate(Bundle savedInstanceState) {

        try {
            UserID = SaveSharedPreference.getUser(getApplicationContext());
        }catch (Exception e){

        }

        if(UserID.length() == 0)
        {
            Intent gotoLogin = new Intent(HomeActivity.this, LoginActivity.class);
            HomeActivity.this.startActivity(gotoLogin);
            HomeActivity.this.finish();
        }
        else
        {




            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            CustomListView = this;

            /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/

            //setListData();



            new PostAsync2().execute("Compress");
            new PostAsync().execute( Integer.toString(POSTS_COUNT) );

            Toast.makeText(this,"Welcome "+UserID, Toast.LENGTH_LONG).show();
        }

        homeTxt = (TextView)findViewById(R.id.txt_h_heading);

        btnLogout = (Button)findViewById(R.id.btn_h_logout);

        homeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refresh();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
            }
        });


        btnPost = (Button)findViewById(R.id.btn_h_post);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoPost();
            }
        });

    }

    public  void refresh(){

        finish();
        startActivity(getIntent());

    }

    public void logout(){

        SaveSharedPreference.setUser(getApplicationContext() ,"" );

        Toast.makeText(this, "Goodbye " + UserID, Toast.LENGTH_LONG).show();
        Intent gotoLogin = new Intent(HomeActivity.this, LoginActivity.class);
        HomeActivity.this.startActivity(gotoLogin);
        HomeActivity.this.finish();
    }


    public void gotoPost(){

        Toast.makeText(this, "Ready to sell your phone ?" + UserID, Toast.LENGTH_LONG).show();
        Intent gotoPost = new Intent(HomeActivity.this, PostActivity.class);
        HomeActivity.this.startActivity(gotoPost);
        //HomeActivity.this.finish();
    }



    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        ListModel tempValues = ( ListModel ) CustomListViewValuesArr.get(mPosition);

        // SHOW ALERT
        Toast.makeText(CustomListView,"Phone Company : "+tempValues.getPhoneCompany() +"Phone Name :"+tempValues.getPhoneName()+"Price :"+tempValues.getPhonePrice(),Toast.LENGTH_LONG).show();

        Bundle bundle = new Bundle();
        bundle.putInt("id", tempValues.getADID());

        Intent gotoAdDetails = new Intent(HomeActivity.this, advertDetailsActivity.class);
        gotoAdDetails.putExtras(bundle);
        HomeActivity.this.startActivity(gotoAdDetails);
        //HomeActivity.this.finish();
    }



    public void inflateListView() // setListData()
    {

        Resources res =getResources();
        list= ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )

        Log.v("Final List","adding the addapter to the listview");
        adapter=new CustomAdapter( CustomListView, CustomListViewValuesArr,res );
        list.setAdapter( adapter );

    }





    class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
        private static final String POSTS_URL = "http://chinmeshmanjrekar.co.nf/AndroidUploadImage/getposts.php";
        
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        private static final String TAG_POSTS = "posts";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Fetching data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("count", args[0]);// how many to fetch
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
            JSONObject row = new JSONObject() ;

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (json != null) {
                //Toast.makeText(HomeActivity.this, json.toString(), Toast.LENGTH_LONG ).show(); // must be commented finally
                try {
                    success = json.getInt(TAG_SUCCESS);
                    message = json.getString(TAG_MESSAGE);
                    posts = json.getJSONArray(TAG_POSTS) ;


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

                Toast.makeText(HomeActivity.this, "Network Error", Toast.LENGTH_LONG ).show();

            }


            if (success == 1) {

                Log.d("Success!", message);

                for (int i = 0; i < posts.length(); ++i) {

                    //postsListDataArray[i] = new ListModel() ;
                    List_row = new ListModel();

                    try {
                        row = posts.getJSONObject(i);
                        //postsListDataArray[i].setPhoneName( ( row.getString("phonename") ) ) ;
                        List_row.setPhoneName( ( row.getString("phonename") ) ) ;
                        //postsListDataArray[i].setPhoneCompany( ( row.getString("companyname") ) );
                        List_row.setPhoneCompany( ( row.getString("companyname") ) );
                        //postsListDataArray[i].setPhonePrice( (String)( row.getString("sellingprice") ) );
                        List_row.setPhonePrice( (String)( row.getString("sellingprice") ) );

                        List_row.setPhoneIcon(row.getString("imagedata"));

                        List_row.setADID(row.getInt("id"));

                        CustomListViewValuesArr.add(List_row);

                        Log.v("JSON Success","inflated 1 listview");
                    }
                    catch( JSONException je){

                        Log.e("JSON Post","Problem with posts data");
                        je.printStackTrace();
                    }
                }
            }else{
                Toast.makeText(HomeActivity.this, message , Toast.LENGTH_LONG ).show();
                Log.d("Failure", message);
            }


            //setListData(); // sets data from postsListDataArray into the listsViews
            inflateListView();

        }
    }




    class PostAsync2 extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
        private static final String POSTS_URL = "http://chinmeshmanjrekar.co.nf/AndroidUploadImage/compress.php";

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        private static final String TAG_POSTS = "posts";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Fetching data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("status", args[0]);// how many to fetch
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

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

        }


    }





}


