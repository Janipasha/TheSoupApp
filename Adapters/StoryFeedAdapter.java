package in.thesoup.thesoup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoup.Activities.DetailsActivity;
import in.thesoup.thesoup.Activities.LoginActivity;
import in.thesoup.thesoup.Activities.MainActivity;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilsFollowUnFollow;
import in.thesoup.thesoup.R;

import static com.google.gson.internal.bind.util.ISO8601Utils.format;

/**
 * Created by Jani on 07-04-2017.
 */

public class StoryFeedAdapter extends RecyclerView.Adapter<StoryFeedAdapter.DataViewHolder> {

    private List<StoryData> StoryDataList;
    private Context context;
    private int clickposition;
    private String clickStoryId;

    public StoryFeedAdapter(List<StoryData> Datalist, Context context) {
        this.StoryDataList = Datalist;
        this.context = context;
    }

    public void refreshData(List<StoryData> Datalist) {
        this.StoryDataList = Datalist;
        notifyDataSetChanged();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView storyTitle, substoryTitle, date, month, year, numberOfArticles;
        public ImageView imageView;
        public Button mButton;


        public DataViewHolder(View itemView) {
            super(itemView);

            storyTitle = (TextView) itemView.findViewById(R.id.Story_title);
            substoryTitle = (TextView) itemView.findViewById(R.id.substory_title);
            date = (TextView) itemView.findViewById(R.id.date);
            month = (TextView) itemView.findViewById(R.id.month);
            year = (TextView) itemView.findViewById(R.id.year);
            numberOfArticles = (TextView) itemView.findViewById(R.id.number_of_articles);
            mButton = (Button) itemView.findViewById(R.id.followbutton);

            imageView = (ImageView) itemView.findViewById(R.id.main_image);


            imageView.setOnClickListener(this);

            storyTitle.setOnClickListener(this);

            mButton.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {



            int mposition = getAdapterPosition();
            String mfollowstatus = StoryDataList.get(mposition).getFollowStatus();
            String mString = StoryDataList.get(mposition).getStoryId();

            if (view == imageView || view == storyTitle) {

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("story_id", mString);
                intent.putExtra("followstatus",mfollowstatus);


                context.startActivity(intent);
            } else if (view == mButton) {
                clickposition = mposition;
                clickStoryId = mString;

                followstory();
            }

        }

    }

    public void followstory() {

        Log.d("follow worked", clickStoryId);
        String mfollowstatus = StoryDataList.get(clickposition).getFollowStatus();

        if (mfollowstatus.equals("") || mfollowstatus.equals("0")) {

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);


            if (TextUtils.isEmpty(pref.getString("auth_token", null))) {


                MainActivity activity = (MainActivity) context;


                activity.demo(clickStoryId);

            } else {

                //NetworkUtilsFollowUnFollow follow = new NetworkUtilsFollowUnFollow(context,mString);

                HashMap<String, String> params = new HashMap<>();

                params.put("auth_token", pref.getString("auth_token", null));
                params.put("story_id", clickStoryId);

                NetworkUtilsFollowUnFollow followrequest = new NetworkUtilsFollowUnFollow(context, params);
                //followrequest.followRequest(clickposition);

                followrequest.followRequest(clickposition);


            }

        } else if (mfollowstatus.equals("1")) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

            HashMap<String, String> params = new HashMap<>();

            params.put("auth_token", pref.getString("auth_token", null));
            params.put("story_id", clickStoryId);

            NetworkUtilsFollowUnFollow unFollowrequest = new NetworkUtilsFollowUnFollow(context, params);

            unFollowrequest.unFollowRequest(clickposition);




        }
    }


    @Override
    public StoryFeedAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stories, parent, false);

        return new DataViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(StoryFeedAdapter.DataViewHolder holder, int position) {

        final StoryData mStoryData = StoryDataList.get(position);

        String storytitle = mStoryData.getStoryName();
        String substorytitle = mStoryData.getSubStoryName();
        String Time = mStoryData.getTime();
        String Num_of_articles = mStoryData.getNumArticle();
        String followstatus = mStoryData.getFollowStatus();

        Log.d("follow",storytitle+followstatus);

        // Log.d("followstatus",followstatus);


        String month = null;
        try {
            month = monthFomrat(Time);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Not valid time", Time);
        }

        // Log.d("Month", month);


        String Date = null;
        try {
            Date = DateFomrat(Time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String year = null;
        try {
            year = yearFomrat(Time);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String ImageUrl;

        if (mStoryData.getImageUrl().isEmpty()) {
            ImageUrl = mStoryData.getArticleImageUrl();

        } else {
            ImageUrl = mStoryData.getImageUrl();

        }

        holder.storyTitle.setText(storytitle);
        holder.substoryTitle.setText(substorytitle);
        Picasso.with(context).load(ImageUrl).into(holder.imageView);

        holder.date.setText(Date);
        holder.month.setText(month);
        holder.year.setText(year);
        holder.numberOfArticles.setText(Num_of_articles + " ARTICLES ");

        if (followstatus.equals("1")) {
            holder.mButton.setText("Following");
        } else if (followstatus.equals("0")) {
            holder.mButton.setText("Follow");
        }


    }

    @Override
    public int getItemCount() {
        return StoryDataList.size();
    }


    public String monthFomrat(String string) throws ParseException {
        SimpleDateFormat monthformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = monthformat.parse(string);
       /* try {
            date = dateformat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Time sent is not valid",string);
        }*/

        SimpleDateFormat monthFormat2 = new SimpleDateFormat("MMMM");

        return monthFormat2.format(date);


    }

    public String yearFomrat(String string) throws ParseException {
        SimpleDateFormat yearformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = yearformat.parse(string);
       /* try {
            date = dateformat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Time sent is not valid",string);
        }*/

        SimpleDateFormat yearFormat2 = new SimpleDateFormat("yyyy");

        return yearFormat2.format(date);


    }


    public String DateFomrat(String string) throws ParseException {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = dateformat.parse(string);
       /* try {
            date = dateformat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Time sent is not valid",string);
        }*/

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd");

        return dateFormat2.format(date);


    }


}
