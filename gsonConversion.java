package in.thesoup.thesoup;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.thesoup.thesoup.Activities.DetailsActivity;
import in.thesoup.thesoup.Activities.MainActivity;
import in.thesoup.thesoup.Activities.feedActivity;
import in.thesoup.thesoup.Adapters.SingleStoryAdapter;
import in.thesoup.thesoup.Adapters.StoryFeedAdapter;
import in.thesoup.thesoup.GSONclasses.FeedGSON.GetStoryFeed;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.GetSingleStory;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Substories;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Substoryjsondata;

/**
 * Created by Jani on 07-04-2017.
 */

public class gsonConversion {


    private JSONObject mJsonObject;
    private List<StoryData> mListFromJson;
    private Substoryjsondata mSubstoryjsonData;
    private String StoryTitle , followstatus;
    private List<Substories> mSubstories;

    public void fillUI(JSONObject jsonObject,Context context){

        mListFromJson = new ArrayList<>();
        mJsonObject = jsonObject;

        Gson gson = new Gson();
        GetStoryFeed red = gson.fromJson(jsonObject.toString(),GetStoryFeed.class);

        for(int i=0;i<red.getStoryDataList().size();i++){

            mListFromJson.add(red.getStoryDataList().get(i));
        }



        if(context instanceof MainActivity) {
            MainActivity activity = (MainActivity)context;

            activity.startAdapter(mListFromJson);

        }

        if(context instanceof feedActivity){
            feedActivity activity = (feedActivity)context;
            activity.startAdapter(mListFromJson);
        }
    }

    public void fillStoryUI(JSONObject jsonObject, Context context){

        mSubstories = new ArrayList<>();

        mJsonObject = jsonObject;

        Gson gson = new Gson();
        GetSingleStory redstory = gson.fromJson(jsonObject.toString(),GetSingleStory.class);

        StoryTitle = redstory.getdata().getStoryName();

       for(int i=0;i<redstory.getdata().getSubstories().size();i++){

           mSubstories.add(redstory.getdata().getSubstories().get(i));
        }

        Log.d("substories",mSubstories.toString());


        DetailsActivity activity = (DetailsActivity) context;

        activity.startAdapter(mSubstories,StoryTitle);

      //mSingleStoryAdapter.refreshData(substories,StoryTitle,followstatus);



    }

    public void ArticleJson(String ArticleString, List<Articles> mArticles){

        Gson gson = new Gson();
        Type ArticleListType = new TypeToken<ArrayList<Articles>>(){}.getType();

        List<Articles> articlesList = gson.fromJson(ArticleString,ArticleListType);

        for (int i=0;i<articlesList.size();i++){
            mArticles.add(articlesList.get(i));
        }
    }





}
