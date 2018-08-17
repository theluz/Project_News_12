package com.example.android.project_news_1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


/**
 * Created by Luz on 03/08/2018.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, List<News> newsList) {
        super(context, 0, newsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        News currentNews = getItem(position);
        TextView sectionView = (TextView) listItemView.findViewById(R.id.news_section);
        String newsSection = currentNews.getSectionName();
        sectionView.setText(newsSection);

        TextView titleView = (TextView) listItemView.findViewById(R.id.news_title);
        String newsTitle = currentNews.getTitle();
        titleView.setText(newsTitle);

        TextView textView = (TextView) listItemView.findViewById(R.id.news_text);
        String newsText = currentNews.getText();
        textView.setText(newsText);

        TextView pubDateView = (TextView) listItemView.findViewById(R.id.news_date);
        String newsDate = currentNews.getPubDate();
        int iend = newsDate.indexOf("T");
        String subString = newsDate.substring(0, iend);
        pubDateView.setText(subString);
        //String formattedDate = newsDate.replace("T" ," - ").replace("Z","");
        //pubDateView.setText(formattedDate);
        //Date dateObject = new Date(currentNews.getPubDate());
        //String formattedDate = formatDate(dateObject);
        //pubDateView.setText(formattedDate);

        TextView authorView = (TextView) listItemView.findViewById(R.id.news_author);
        String newsAuthor = currentNews.getAuthor();
        authorView.setText(newsAuthor);

        return listItemView;
    }

    private String formatDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, LLL, yyyy");
        return dateFormat.format(dateString);
    }
}