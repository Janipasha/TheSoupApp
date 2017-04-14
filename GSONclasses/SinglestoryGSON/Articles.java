package in.thesoup.thesoup.GSONclasses.SinglestoryGSON;

import java.io.Serializable;

/**
 * Created by Jani on 11-04-2017.
 */

public class Articles implements Serializable {

    String article_id;
    String title;
    String description;
    String url;
    String image_url;
    String pud_date;
    String author;
    String source_name;
    String source_thumb;


    public String getArticletitle() {
        return title;
    }
}
