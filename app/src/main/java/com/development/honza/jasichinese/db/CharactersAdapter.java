package com.development.honza.jasichinese.db;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Honza on 7. 3. 2018.
 */

public class CharactersAdapter extends BaseAdapter {
    Context context;
    List<Characters> characters;

    public CharactersAdapter() {
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Characters> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Characters> characters) {
        this.characters = characters;
    }

    @Override
    public int getCount() {
        return characters.size();
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
