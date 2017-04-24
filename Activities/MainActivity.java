package in.thesoup.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.NetworkCalls.NetworkUtils;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilswithToken;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.Adapters.StoryFeedAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.R.id.empty;
import static android.os.Build.VERSION_CODES.N;

public class MainActivity extends AppCompatActivity {

    private StoryFeedAdapter mStoryfeedAdapter;
    private List<StoryData> mStoryData;
    private RecyclerView StoryView;
    private HashMap<String, String> params;
    private SharedPreferences pref;
    private Button Discover, MyFeed;
    private EndlessRecyclerViewScrollListener scrollListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());


        Bundle extras = getIntent().getExtras();
        Intent intent = getIntent();

            super.onCreate(savedInstanceState);
            setContentView(R.layout.getstorieslist);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            Discover = (Button) findViewById(R.id.discover);
            MyFeed = (Button) findViewById(R.id.myfeed);


            pref = PreferenceManager.getDefaultSharedPreferences(this);
            params = new HashMap<>();


            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath("fonts/OpenSans-Semibolditalic.ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );


            mStoryData = new ArrayList<>();


            StoryView = (RecyclerView) findViewById(R.id.list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            StoryView.setLayoutManager(layoutManager);



        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);

            }
        };

            StoryView.setHasFixedSize(true);
        StoryView.addOnScrollListener(scrollListener);

        if (TextUtils.isEmpty(pref.getString("auth_token", null))) {


            params.put("page","0");


            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(MainActivity.this, mStoryData, params);


            networkutilsToken.getFeed();

        } else {

            params.put("auth_token", pref.getString("auth_token", null));
            params.put("page","0");

            Log.d("auth_token", pref.getString("auth_token", null));

            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(MainActivity.this, mStoryData, params);


            networkutilsToken.getFeed();

        }

        }

    public void loadNextDataFromApi(int offset) {

        String Page = String.valueOf(offset);

        if (TextUtils.isEmpty(pref.getString("auth_token", null))) {


            params.put("page",Page);


            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(MainActivity.this, mStoryData, params);


            networkutilsToken.getFeed();

        } else {

            params.put("auth_token", pref.getString("auth_token", null));
            params.put("page",Page);

            Log.d("auth_token", pref.getString("auth_token", null));

            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(MainActivity.this, mStoryData, params);


            networkutilsToken.getFeed();

        }





    }






    public void startAdapter(List<StoryData> mStoryData){
        mStoryfeedAdapter = new StoryFeedAdapter(mStoryData, MainActivity.this);
        StoryView.setAdapter(mStoryfeedAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 35) {
            Log.d("activity result", "worked");
            mStoryfeedAdapter.followstory();

        }
    }

    public void demo(String storyId ) {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("story_id",storyId);
        intent.putExtra("activity","0");
        startActivityForResult(intent,35);
    }

  public  void demo1(int position , String followstatus){
        mStoryData.get(position).changeFollowStatus(followstatus);
        mStoryfeedAdapter.refreshData(mStoryData);
    }

    public void ActivityInflate(View view){

        if(view == Discover){
         Intent intent = new Intent(this,MainActivity.class);
            finish();
            startActivity(intent);
        }

        if(view== MyFeed){
            if(TextUtils.isEmpty(pref.getString("auth_token",""))){
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);

            }else {

                Intent intent = new Intent ( this, feedActivity.class);
                finish();
                startActivity(intent);

            }
        }
    }




}



