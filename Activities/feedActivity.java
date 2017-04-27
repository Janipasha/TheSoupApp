package in.thesoup.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoup.Adapters.StoryFeedAdapter;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.NetworkCalls.NetworkUtils;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilswithToken;
import in.thesoup.thesoup.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        intent1 = getIntent();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.getstorieslist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Discover = (Button) findViewById(R.id.discover);
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


        params.put("auth_token", pref.getString("auth_token", null));
        params.put("myfeed","1"); // 1 is the value required for getting myfeed

        Log.d("auth_token1", pref.getString("auth_token", null));

        NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(this, mStoryData, params);


        networkutilsToken.getFeed();

            }



    /*else {
            StoryView.setVisibility(View.GONE);

            TextView mTextView = (TextView) findViewById(R.id.empty_view);
            mTextView.setText("This is what I am showing logged in users who are new :\n" +
                    "\"Nothing to show since no stories followed yet.\n" +
                    "Find something of interest in the 'Discover' section\"");

        }*/




    public void startAdapter(List<StoryData> mStoryData) {
        //
        //StoryView.setAdapter(mStoryfeedAdapter);
        mStoryfeedAdapter.refreshData(mStoryData);

    }

    public void ActivityInflate(View view){

        if(view == Discover){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

        if(view== MyFeed){
                Intent intent = new Intent ( this, feedActivity.class);
                finish();
                startActivity(intent);
                // TODO: Write code to handle errror, change intents as well
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

    public void demo(String storyId ) {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("story_id",storyId);
        intent.putExtra("activity","0");
        startActivityForResult(intent,35);
    }

    public  void demo1(int position , String followstatus){
        mStoryData.get(position).changeFollowStatus(followstatus);
        mStoryfeedAdapter.refreshfollowstatus(mStoryData);
    }
    }

//TODO: 2) implement pagination in feed activity as well

