package com.example.android.project_news_1;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Luz on 03/08/2018.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>>{

    private static final String LOG_TAG = NewsLoader.class.getName();
    private String mUrl;

    public NewsLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading(){forceLoad();}

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<News> newses = QueryUtils.fetchNewsData(mUrl);
        return newses;
    }








}
