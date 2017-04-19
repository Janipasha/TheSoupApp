package in.thesoup.thesoup.NetworkCalls;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.thesoup.thesoup.SoupContract;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.Adapters.StoryFeedAdapter;
import in.thesoup.thesoup.gsonConversion;

/**
 * Created by Jani on 06-04-2017.
 */

public class NetworkUtils {
    private Context mcontext;
    private List<StoryData> mStoryData;

    public NetworkUtils(Context context, List<StoryData> storyData) {
        this.mcontext = context;
        this.mStoryData = storyData;


    }


    public void getFeed(final StoryFeedAdapter feedAdapter) {

        MySingleton singleton = MySingleton.getInstance(mcontext);

        //RequestQueue queue = singleton.getRequestQueue();


   JsonObjectRequest jsObjRequest = new JsonObjectRequest
            (Request.Method.GET, SoupContract.URL, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("akunamatata", response.toString());



                   gsonConversion mpopulateUI = new gsonConversion();

                 mpopulateUI.fillUI(response,mStoryData,feedAdapter);


                }


                //mEarthquakedatajsonclass = red;

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // Auto-generated method stub

                }
            });


    singleton.addToRequestQueue(jsObjRequest);




}





}