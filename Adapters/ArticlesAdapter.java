package in.thesoup.thesoup.Adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoup.thesoup.R;

/**
 * Created by Jani on 14-04-2017.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private List<Articles> articles;
    private Context mcontext;


    public ArticlesAdapter(List<Articles> articles,Context mcontext){
        this.articles = articles;
        this.mcontext= mcontext;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articles,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String imageUrl = articles.get(position).getImageUrl();
        String articleTitle = articles.get(position).getArticletitle();
        String newsSource = articles.get(position).getSourceName();

        holder.articleTitle.setText(articleTitle);
        holder.newsource.setText(newsSource);
        Picasso.with(mcontext).load(imageUrl).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView articleTitle,readmore,newsource;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.article_image);
            articleTitle = (TextView)itemView.findViewById(R.id.article_title);
            readmore = (TextView)itemView.findViewById(R.id.readmore);
            newsource =(TextView)itemView.findViewById(R.id.source_name);

            readmore.setOnClickListener(this);
            imageView.setOnClickListener(this);
            articleTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //TODO: add intents for chrome browser
        }
    }
}
