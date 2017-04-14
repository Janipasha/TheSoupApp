package in.thesoup.thesoup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.thesoup.thesoup.Activities.ArticlesActivity;
import in.thesoup.thesoup.Activities.DetailsActivity;
import in.thesoup.thesoup.Activities.LoginActivity;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Substoryjsondata;
import in.thesoup.thesoup.R;

import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Substories;

import static android.R.attr.onClick;
import static android.R.attr.start;
import static android.R.attr.subtitleTextStyle;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.media.CamcorderProfile.get;
import static in.thesoup.thesoup.R.id.month;
import static in.thesoup.thesoup.R.layout.story;

/**
 * Created by Jani on 12-04-2017.
 */

public class SingleStoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<Substories> substories;
    private Context mcontext;
    private String storyTitle;
    private String followstatus;
    private String StoryTitle = "";

    // private List<Substories> mSubstories;


    public SingleStoryAdapter(List<Substories> substories, String storyTitle, String followstatus, Context context) {
        this.substories = substories; //Check is this statement is valid
        this.mcontext = context;
        this.storyTitle = storyTitle;
        this.followstatus = followstatus;
    }

    public void refreshData(List<Substories> substories,String storyTitle,String followstatus) {
        this.substories = substories;
        this.storyTitle = storyTitle;
        this.followstatus = followstatus;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;

        }

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    public class StoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mSubstory, mNumber_of_articles, mviewMore, mDate, mMonth, mYear;

        public StoryViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.main_image_story);
            mSubstory = (TextView) itemView.findViewById(R.id.substory_title_story);
            mNumber_of_articles = (TextView) itemView.findViewById(R.id.number_of_articles_story);
            mviewMore = (TextView) itemView.findViewById(R.id.view_more_story);
            mDate = (TextView) itemView.findViewById(R.id.date_story);
            mMonth = (TextView) itemView.findViewById(R.id.month_story);
            mYear = (TextView) itemView.findViewById(R.id.year_story);


            mImageView.setOnClickListener(this);
            mSubstory.setOnClickListener(this);
            mNumber_of_articles.setOnClickListener(this);
            mviewMore.setOnClickListener(this);
            mDate.setOnClickListener(this);
            mMonth.setOnClickListener(this);
            mYear.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int nposition = getAdapterPosition();
            List<Articles> mArticles = substories.get(nposition).getArticles();


            Intent intent = new Intent(mcontext, ArticlesActivity.class);

            intent.putExtra("ARTICLELIST",(Serializable)mArticles);
            //Todo: implement putextra
            mcontext.startActivity(intent);

        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView Storytitle;
        private Button followbutton;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            Storytitle = (TextView) itemView.findViewById(R.id.story_title_story_header);
            followbutton = (Button) itemView.findViewById(R.id.followbutton_story_header);

            followbutton.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            int mposition = getAdapterPosition();
            String mString = substories.get(mposition).getSubstoryId();


            String mfollowstatus = followstatus;

            if (mfollowstatus == null || mfollowstatus.equals("0")) {

                if (AccessToken.getCurrentAccessToken() == null) {

                    Intent intent = new Intent(mcontext, LoginActivity.class);
                    //TODO: let the user be shown following after sucess

                    mcontext.startActivity(intent);
                } else {
                    followbutton.setText("Following");


                    //TODO: server request and let him know following, pass on stories ID

                }

            } else if (mfollowstatus.equals("1")) {
                followbutton.setText("Follow");

                //TODO: unfollow request network call return and then change text
            }

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story, parent, false);
            return new StoryViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.storypage_header, parent, false);
            return new HeaderViewHolder(view);

        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        final Substories mSubstories = substories.get(position);
        StoryTitle = storyTitle;
        String Time = mSubstories.getTime();
        String substoryTitle = mSubstories.getSubstoryName();
        int NumberofArticles = mSubstories.getNumberofArticles();
        String SubstoryImage = mSubstories.getSubstoryImageURL();

        Log.i("SubstoryImage",SubstoryImage);
        Log.i("Time",Time);
        Log.i("StoryTitle",StoryTitle);
        Log.i("substoryTitle", substoryTitle);

        String month = null;
        try {
            month = monthFomrat(Time);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Not valid time", Time);
        }

        Log.d("Month", month);


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


        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).Storytitle.setText(StoryTitle);

        } else if (holder instanceof StoryViewHolder) {
            ((StoryViewHolder) holder).mNumber_of_articles.setText(String.valueOf(NumberofArticles) + " ARTICLES");
            ((StoryViewHolder) holder).mDate.setText(Date);
            ((StoryViewHolder) holder).mMonth.setText(month);
            ((StoryViewHolder) holder).mYear.setText(year);
            ((StoryViewHolder) holder).mSubstory.setText(substoryTitle);
            Picasso.with(mcontext).load(SubstoryImage).into(((StoryViewHolder)holder).mImageView);

        }

    }

    @Override
    public int getItemCount() {

                    return substories.size();

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
