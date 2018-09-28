package com.development.honza.jasichinese.db;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Honza on 7. 3. 2018.
 */

public class WordsAdapter extends BaseAdapter {
    Context context;
    List<Words> words;

    public WordsAdapter() {
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Words> getWords() {
        return words;
    }

    public void setWords(List<Words> words) {
        this.words = words;
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return view;
    }

}
