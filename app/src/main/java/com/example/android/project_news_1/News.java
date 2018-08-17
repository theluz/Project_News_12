package com.example.android.project_news_1;

import java.util.Date;

/**
 * Created by Luz on 03/08/2018.
 */

public class News {
    private String mSectionName;
    private String mTitle;
    private String mUrl;
    private String mText;
    private String mPubDate;
    private String mAuthor;

    public News(String sectionName, String title, String url, String text, String pubDate,String pubAuthor){
        mSectionName = sectionName;
        mTitle = title;
        mUrl = url;
        mText = text;
        mPubDate = pubDate;
        mAuthor = pubAuthor;
    }
    public String getSectionName(){return mSectionName;}
    public String getTitle(){return mTitle;}
    public String getUrl(){return mUrl;}
    public String getText(){return mText;}
    public String getPubDate(){return mPubDate;}
    public String getAuthor(){return mAuthor;}

}
