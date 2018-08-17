package com.example.android.project_news_1;

import android.text.TextUtils;
import android.util.Log;
import android.util.StringBuilderPrinter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.R.attr.author;

/**
 * Created by Luz on 03/08/2018.
 */

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static String makeHttpRequest (URL url) throws IOException{
        String jsonResponse = "";
        if (url == null){
            return jsonResponse;
        }
        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static List<News> extractFeatureFromJson(String newsJSON){
        String pubAuthor ="";
        String bodyText = "";
        String newsText = "";
        if (TextUtils.isEmpty(newsJSON)){
            return null;
        }
        List<News> newses = new ArrayList<>();
        try {
            JSONObject baseJsonObject = new JSONObject(newsJSON);
            JSONObject newsObject = baseJsonObject.getJSONObject("response");
            JSONArray currentNewsArray = newsObject.getJSONArray("results");

            for (int i = 0; i < currentNewsArray.length(); i++){
                JSONObject currentNews = currentNewsArray.getJSONObject(i);
                String sectionName = currentNews.getString("sectionName");
                String webTitle = currentNews.getString("webTitle");
                String webUrl = currentNews.getString("webUrl");
                //Tentando pegar o Array FIELDS para o campo BodyText
                JSONObject bodyArray = currentNews.getJSONObject("fields");
                if (bodyArray.length() > 0 ){
                    for (int b = 0; b < bodyArray.length(); b++){
                        bodyText = bodyArray.getString("bodyText");
                    }
                    int body = bodyText.indexOf(".");
                    newsText = bodyText.substring(0,body);
                    //Log.e("2 pontos da noticia",newsText);
                }
                String pubDate = currentNews.getString("webPublicationDate");

                //Tentando pegar o Array TAGS para pegar o campo webTitle com o autor
                JSONArray authorArray = currentNews.getJSONArray("tags");
                if (authorArray.length() > 0) {
                    for (int a = 0; a < authorArray.length(); a++) {
                        JSONObject currentAuthor = authorArray.getJSONObject(a);
                        pubAuthor = currentAuthor.getString("webTitle");
                    }
                }else {pubAuthor = "";}
                News news = new News(sectionName,webTitle,webUrl,newsText,pubDate,pubAuthor);
                newses.add(news);
            }
        }catch (JSONException e){
            Log.e("QueryUtils", "Problem parsing the News JSON results", e);
        }
        return newses;
    }

    public static List<News> fetchNewsData(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> newses = extractFeatureFromJson(jsonResponse);
        return newses;
    }
}
