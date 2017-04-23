package in.thesoup.thesoup.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.thesoup.thesoup.Adapters.ArticlesAdapter;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.gsonConversion;

/**
 * Created by Jani on 13-04-2017.
 */

public class ArticlesActivity extends AppCompatActivity{
    private TextView mtextView;

    private List<Articles> mArticles;
    private RecyclerView ArticlesView;
    private ArticlesAdapter mArticlesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getarticles);




        Intent in = getIntent();
        mArticles =(List<Articles>) in.getSerializableExtra("ARTICLELIST");

        //gsonConversion articlelist = new gsonConversion();
        //articlelist.ArticleJson(mString,mArticles);


        ArticlesView = (RecyclerView) findViewById(R.id.list_articles);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
       ArticlesView.setLayoutManager(layoutManager);

      ArticlesView.setHasFixedSize(true);

        mArticlesAdapter = new ArticlesAdapter(mArticles,this);

        ArticlesView.setAdapter(mArticlesAdapter);





    }
}
