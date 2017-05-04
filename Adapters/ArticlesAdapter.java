package in.thesoup.thesoup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import in.thesoup.thesoup.Activities.ArticleWebViewActivity;
import in.thesoup.thesoup.Application.AnalyticsApplication;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.SoupContract;

import static android.R.attr.start;

/**
 * Created by Jani on 14-04-2017.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private List<Articles> articles;
    private Context mcontext;
    private AnalyticsApplication application;
    private Tracker mTracker;
    private SharedPreferences pref;


    public ArticlesAdapter(List<Articles> articles, Context mcontext) {
        this.articles = articles;
        this.mcontext = mcontext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articles, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String imageUrl = articles.get(position).getImageUrl();
        String articleTitle = articles.get(position).getArticletitle();
        String articleTitlehtml;

        if(Build.VERSION.SDK_INT >= 24){
            articleTitlehtml = String.valueOf(Html.fromHtml(articleTitle , Html.FROM_HTML_MODE_LEGACY));

        }else{

            articleTitlehtml = String.valueOf (Html.fromHtml(articleTitle));
        }
        String newsSource = articles.get(position).getSourceName();

        holder.articleTitle.setText(articleTitlehtml);
        holder.newsource.setText(newsSource);
        Picasso.with(mcontext).load(imageUrl).centerCrop().placeholder(R.drawable.placeholder).resize(80,80).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView articleTitle, readmore, newsource;
        private WebView wView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.article_image);
            articleTitle = (TextView) itemView.findViewById(R.id.article_title);
            readmore = (TextView) itemView.findViewById(R.id.readmore);
            newsource = (TextView) itemView.findViewById(R.id.source_name);

            readmore.setOnClickListener(this);
            imageView.setOnClickListener(this);
            articleTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            application = AnalyticsApplication.getInstance();
            mTracker = application.getDefaultTracker();
            pref = PreferenceManager.getDefaultSharedPreferences(mcontext);

            if (TextUtils.isEmpty(pref.getString("auth_token", null))) {

                application.sendEvent(mTracker, SoupContract.CLICK, SoupContract.CLICK_SOURCES,SoupContract.ARTICLES_PAGE);

            }else{

                application.sendEventUser(mTracker,SoupContract.CLICK,SoupContract.CLICK_SOURCES,SoupContract.ARTICLES_PAGE
                        ,pref.getString(SoupContract.FB_ID,null),
                        pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null));

            }

            int mposition = getAdapterPosition();

            String ArticleURL = articles.get(mposition).getUrl();

            Intent intent = new Intent(mcontext, ArticleWebViewActivity.class);
            intent.putExtra("ArticleURL",ArticleURL);
            mcontext.startActivity(intent);


            // TODO: add intents for chrome browser
        }
    }
}

