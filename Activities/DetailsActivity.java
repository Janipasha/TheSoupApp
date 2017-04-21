package in.thesoup.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import in.thesoup.thesoup.Adapters.SingleStoryAdapter;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Substories;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilsStory;
import in.thesoup.thesoup.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Jani on 09-04-2017.
 */

public class DetailsActivity extends AppCompatActivity {

    private List<Substories> mSubstories;
    //private List<Substoryjsondata> substoryjsondataList;
    private RecyclerView SingleStoryView;
    private SingleStoryAdapter nSingleStoryAdapter;
    private String StoryTitle, followStatus,StoryId;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getstorydetails);

        Bundle bundle = getIntent().getExtras();
        StoryId = bundle.getString("story_id");
        followStatus = bundle.getString("followstatus");

        Log.d("followstatus details1",followStatus);

        if(TextUtils.isEmpty(followStatus)){
            followStatus = "0";
        }

        Log.d("StoryId",StoryId);
        Log.d("followstatus details2",followStatus);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Semibolditalic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        mSubstories = new ArrayList<>();


        SingleStoryView = (RecyclerView) findViewById(R.id.list_story);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        SingleStoryView.setLayoutManager(layoutManager);

        SingleStoryView.setHasFixedSize(true);


        NetworkUtilsStory networkutils = new NetworkUtilsStory(DetailsActivity.this, mSubstories,StoryTitle,followStatus,StoryId);


        try {
            networkutils.getSingleStory();
        } catch (JSONException e) {
            e.printStackTrace();
        }





    }

    public void startAdapter(List<Substories> mSubstories, String StoryTitle){
        nSingleStoryAdapter = new SingleStoryAdapter(mSubstories,StoryTitle,followStatus,DetailsActivity.this,StoryId);
        SingleStoryView.setAdapter(nSingleStoryAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 36) {
            Log.d("result in Details", "worked");
            nSingleStoryAdapter.followstory();

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void demo(String storyId ) {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("story_id",storyId);
        intent.putExtra("activity","1");
        startActivityForResult(intent,36);
    }

    public void DetailsActivitydemo(String mfollowStatus){
        followStatus = mfollowStatus;
        nSingleStoryAdapter.refreshData(followStatus);


    }
}