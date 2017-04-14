package in.thesoup.thesoup.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.gsonConversion;

/**
 * Created by Jani on 13-04-2017.
 */

public class ArticlesActivity extends AppCompatActivity{
    private TextView mtextView;
    private String mString;
    private List<Articles> mArticles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles);

        Intent in = getIntent();
        mArticles =(List<Articles>) in.getSerializableExtra("ARTICLELIST");

        //gsonConversion articlelist = new gsonConversion();
        //articlelist.ArticleJson(mString,mArticles);

        mtextView= (TextView)findViewById(R.id.article_text);

        String articletitle = mArticles.get(0).getArticletitle();

        mtextView.setText(articletitle);


    }
}
