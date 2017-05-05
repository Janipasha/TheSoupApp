package in.thesoup.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoup.Application.AnalyticsApplication;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilswithToken;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.Adapters.StoryFeedAdapter;
import in.thesoup.thesoup.SoupContract;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private StoryFeedAdapter mStoryfeedAdapter;
    private List<StoryData> mStoryData;
    private RecyclerView StoryView;
    private HashMap<String, String> params;
    private SharedPreferences pref;
    private Button Discover, MyFeed;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Tracker mTracker;
    private AnalyticsApplication application;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());

        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        Bundle extras = getIntent().getExtras();
        Intent intent = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.getstorieslist);

      application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Discover = (Button) findViewById(R.id.discover);

        View viewFeed = (View)findViewById(R.id.line_view_feed);
        viewFeed.setVisibility(View.GONE);

        MyFeed = (Button) findViewById(R.id.myfeed);
        MyFeed.setTextColor(Color.parseColor("#80ffffff"));


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
        mStoryfeedAdapter = new StoryFeedAdapter(mStoryData, MainActivity.this);
        StoryView.setAdapter(mStoryfeedAdapter);
        StoryView.setHasFixedSize(true);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };

        StoryView.addOnScrollListener(scrollListener);


        if (TextUtils.isEmpty(pref.getString("auth_token", null))) {
            params.put("page", "0");
            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(MainActivity.this, mStoryData, params);
            networkutilsToken.getFeed();
        } else {
            params.put("auth_token", pref.getString("auth_token", null));
            params.put("page", "0");

            Log.d("auth_token", pref.getString("auth_token", null));

            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(MainActivity.this, mStoryData, params);
            networkutilsToken.getFeed();

        }

    }


    public void loadNextDataFromApi(int offset) {

        String Page = String.valueOf(offset);

        if (TextUtils.isEmpty(pref.getString("auth_token", null))) {


            params.put("page", Page);


            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(MainActivity.this, mStoryData, params);


            networkutilsToken.getFeed();

        } else {

            params.put("auth_token", pref.getString("auth_token", null));
            params.put("page", Page);

            Log.d("auth_token", pref.getString("auth_token", null));

            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(MainActivity.this, mStoryData, params);


            networkutilsToken.getFeed();

        }


    }


    public void startAdapter(List<StoryData> mStoryData) {
        //
        //StoryView.setAdapter(mStoryfeedAdapter);
        mStoryfeedAdapter.refreshData(mStoryData);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onStart() {
        super.onStart();
        application.sendScreenName(mTracker, SoupContract.DISCOVER_VIEWED);

        if(pref.contains(SoupContract.FB_ID)){

            Log.d("fb_id",pref.getString(SoupContract.FB_ID,null));

            String name = pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null);
            application.sendEventUser(mTracker, SoupContract.PAGE_VIEW,SoupContract.DISCOVER_VIEWED,
                    SoupContract.HOME_PAGE,pref.getString(SoupContract.FB_ID,null),
                    pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null));
        }else {

            application.sendEvent(mTracker, SoupContract.PAGE_VIEW, SoupContract.DISCOVER_VIEWED, SoupContract.HOME_PAGE);
        }
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

    public void demo(String storyId) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("story_id", storyId);
        intent.putExtra("activity", "0");
        startActivity(intent);
    }

    public void demo1(int position, String followstatus) {
        mStoryData.get(position).changeFollowStatus(followstatus);
        mStoryfeedAdapter.refreshfollowstatus(mStoryData);
    }

    public void ActivityInflate(View view) {

        if (view == Discover) {

            if(pref.contains(SoupContract.FB_ID)){

                Log.d("fb_id",pref.getString(SoupContract.FB_ID,null));

                String name = pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null);
                application.sendEventUser(mTracker, SoupContract.CLICK,SoupContract.CLICK_DISCOVER,
                        SoupContract.HOME_PAGE,SoupContract.FB_ID,name);
            }else {

                application.sendEvent(mTracker, SoupContract.CLICK, SoupContract.CLICK_DISCOVER, SoupContract.HOME_PAGE);
            }


            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        }

        if (view == MyFeed) {
            if (TextUtils.isEmpty(pref.getString("auth_token", ""))) {

                application.sendEvent(mTracker, SoupContract.CLICK, SoupContract.CLICK_MYFEED, SoupContract.HOME_PAGE);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);

            } else {


                String name = pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null);
                application.sendEventUser(mTracker, SoupContract.CLICK,SoupContract.CLICK_MYFEED,
                        SoupContract.HOME_PAGE,SoupContract.FB_ID,name);

                Intent intent = new Intent(this, feedActivity.class);
                finish();
                startActivity(intent);

            }
        }
    }




}



