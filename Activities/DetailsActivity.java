package in.thesoup.thesoup.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

        Log.d("StoryId",StoryId);


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

        nSingleStoryAdapter = new SingleStoryAdapter(mSubstories,StoryTitle,followStatus,DetailsActivity.this);
        try {
            networkutils.getSingleStory(nSingleStoryAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        SingleStoryView.setAdapter(nSingleStoryAdapter);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}