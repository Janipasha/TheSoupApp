package in.thesoup.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoup.Adapters.StoryFeedAdapter;
import in.thesoup.thesoup.Application.AnalyticsApplication;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilswithToken;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.SoupContract;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.R.attr.data;

/**
 * Created by Jani on 23-04-2017.
 */

public class feedActivity extends AppCompatActivity {


    private StoryFeedAdapter mStoryfeedAdapter;
    private List<StoryData> mStoryData;
    private RecyclerView StoryView;
    private HashMap<String, String> params;
    private SharedPreferences pref;
    private Button Discover, MyFeed;
    private Intent intent1;
    private Tracker mTracker;
    private AnalyticsApplication application;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        intent1 = getIntent();

        application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        setContentView(R.layout.getstorieslist);




        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Semibolditalic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        View viewDetails = findViewById(R.id.line_view_details);
        viewDetails.setVisibility(View.GONE);


        Discover = (Button) findViewById(R.id.discover);
        Discover.setTextColor(Color.parseColor("#80ffffff"));
        MyFeed = (Button) findViewById(R.id.myfeed);



        StoryView = (RecyclerView) findViewById(R.id.list);
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
        mStoryfeedAdapter = new StoryFeedAdapter(mStoryData, feedActivity.this);
        StoryView.setAdapter(mStoryfeedAdapter);

        StoryView.setHasFixedSize(true);

        if (intent1.hasExtra("following stories")) {
            StoryView.setVisibility(View.GONE);

            TextView mTextView = (TextView) findViewById(R.id.empty_view);
            mTextView.setText("You have not followed any SOUPS yet\n");

        } else {


            params.put("auth_token", pref.getString("auth_token", null));
            params.put("myfeed", "1"); // 1 is the value required for getting myfeed
            params.put("page", "0");

            Log.d("auth_token1", pref.getString("auth_token", null));

            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(feedActivity.this, mStoryData, params);


            networkutilsToken.getFeed();
        }

    }





    public void startAdapter(List<StoryData> mStoryData) {
        mStoryfeedAdapter.refreshData(mStoryData);

    }

    public void ActivityInflate(View view) {

        if (view == Discover) {
          // google analytics
            if(pref.contains(SoupContract.FB_ID)){

                Log.d("fb_id",pref.getString(SoupContract.FB_ID,null));

                String name = pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null);
                application.sendEventUser(mTracker, SoupContract.CLICK,SoupContract.CLICK_DISCOVER,
                        SoupContract.HOME_PAGE,SoupContract.FB_ID,name);
            }else {

                application.sendEvent(mTracker, SoupContract.CLICK, SoupContract.CLICK_DISCOVER, SoupContract.HOME_PAGE);
            }
          //

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if (view == MyFeed) {

            if(pref.contains(SoupContract.FB_ID)){

                Log.d("fb_id",pref.getString(SoupContract.FB_ID,null));

                String name = pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null);
                application.sendEventUser(mTracker, SoupContract.CLICK,SoupContract.CLICK_MYFEED,
                        SoupContract.HOME_PAGE,SoupContract.FB_ID,name);
            }else {

                application.sendEvent(mTracker, SoupContract.CLICK, SoupContract.CLICK_MYFEED, SoupContract.HOME_PAGE);
            }
            Intent intent = new Intent(this, feedActivity.class);
            finish();
            startActivity(intent);
            // TODO: Write code to handle errror, change intents as well
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        application.sendScreenName(mTracker,SoupContract.MYFEED_VIEWED);

        if(pref.contains(SoupContract.FB_ID)){

            String name = pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null);
            application.sendEventUser(mTracker, SoupContract.PAGE_VIEW,SoupContract.MYFEED_VIEWED,
                    SoupContract.HOME_PAGE,SoupContract.FB_ID,name);
        }else {

            application.sendEvent(mTracker, SoupContract.PAGE_VIEW, SoupContract.MYFEED_VIEWED, SoupContract.HOME_PAGE);
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
        startActivityForResult(intent, 35);
    }

    public void demo1(int position, String followstatus) {
        mStoryData.get(position).changeFollowStatus(followstatus);
        mStoryfeedAdapter.refreshfollowstatus(mStoryData);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}

//TODO: 2) implement pagination in feed activity as well

