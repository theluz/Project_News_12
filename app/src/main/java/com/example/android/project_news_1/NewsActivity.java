package com.example.android.project_news_1;

import android.content.Context;
import android.content.Intent;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {


    private static final String LOG_TAG = NewsActivity.class.getName();

    //URL fixa anterior
    //private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?show-tags=contributor&show-fields=body-text&q=games&api-key=6b66f283-2d0a-4cec-b253-ee27a84ba59b";
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";

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
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            String orderPreference = sharedPrefs.getString(
                    getString(R.string.settings_order_by_key),
                    getString(R.string.settings_order_by_default));

            Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("show-tags","contributor");
            uriBuilder.appendQueryParameter("show-fields","body-text");
            uriBuilder.appendQueryParameter("q","games");
            uriBuilder.appendQueryParameter("order-by",orderPreference);
            uriBuilder.appendQueryParameter("api-key","6b66f283-2d0a-4cec-b253-ee27a84ba59b");

            Log.e("######################URL Montada",uriBuilder.toString());
            Log.e("VARIAVEL ORDERPREFERENCE = ",orderPreference);
            return new NewsLoader(this, uriBuilder.toString());
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

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            int id = item.getItemId();
            if (id == R.id.action_settings){
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
}