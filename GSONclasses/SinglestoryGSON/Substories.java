package in.thesoup.thesoup.GSONclasses.SinglestoryGSON;

import java.util.List;

import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Articles;

/**
 * Created by Jani on 11-04-2017.
 */

public class Substories {

    String substory_id;
    String substory_created;
    String substory_name;
    String top_article_image;
    String top_article_title;
    List<Articles> articles;

    public String getSubstoryId(){
        return substory_id;
    }

    public  String getTime(){
       return substory_created;
    }

    public  String  getSubstoryName(){
        return  substory_name;
    }

    public int getNumberofArticles() {
        return articles.size();

    }
}
