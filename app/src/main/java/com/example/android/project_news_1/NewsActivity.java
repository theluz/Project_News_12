package com.example.android.project_news_1;

import android.content.Context;
import android.content.Intent;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {


    private static final String LOG_TAG = NewsActivity.class.getName();

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?show-tags=contributor&show-fields=body-text&q=games&api-key=6b66f283-2d0a-4cec-b253-ee27a84ba59b";

    private static final int NEWS_LOADER_ID = 1;

    private NewsAdapter mAdapter;

    private TextView mEmptyTextView;

        @Override
        protected void onCreate (Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.news_main);
            ListView newsListView = (ListView) findViewById(R.id.list);
            mEmptyTextView = (TextView) findViewById(R.id.empty_view);
            newsListView.setEmptyView(mEmptyTextView);

            mAdapter = new NewsAdapter(this, new ArrayList<News>());
            newsListView.setAdapter(mAdapter);

            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    News currentNews = mAdapter.getItem(position);
                    Uri newsUri = Uri.parse(currentNews.getUrl());
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                    startActivity(websiteIntent);
                }
            });

            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(NEWS_LOADER_ID, null, this);
            } else {
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
                mEmptyTextView.setText("No Internet Conection");
            }
        }
        @Override
        public Loader<List<News>> onCreateLoader(int i, Bundle bundle){
            return new NewsLoader(this, GUARDIAN_REQUEST_URL);
        }
        @Override
        public void onLoadFinished(Loader<List<News>> loader, List<News> newses){
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyTextView.setText("No News Found");
            mAdapter.clear();

            if (newses != null && !newses.isEmpty()){
                mAdapter.addAll(newses);
            }
        }
        @Override
        public void onLoaderReset(Loader<List<News>> loader){
            mAdapter.clear();
        }
}