package com.development.honza.jasichinese;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.development.honza.jasichinese.db.Characters;
import com.development.honza.jasichinese.db.CharactersOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends Activity {
    private EditText editText;
    private ArrayList<Characters> charactersArrayList;
    static final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final CharactersOpenHelper db = new CharactersOpenHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editText = (EditText) findViewById(R.id.editText_search);
        listView = (ListView) findViewById(R.id.lv_searchList);

        final SimpleAdapter simpleAdapter = new SimpleAdapter(
                this, list,
                R.layout.row_layout,
                new String[]{"inCzech", "inPinyin", "inChinese", "id"},
                new int[]{R.id.tv_inCzech, R.id.tv_inPinyin, R.id.tv_inChinese, R.id.tv_hiddenId}
        );

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    charactersArrayList = db.getAllLCharactersByAny(s.toString());

                    populateList(charactersArrayList);
                    listView.setAdapter(simpleAdapter);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

    }

    private void populateList(ArrayList<Characters> al) {
        list.clear();
        for (int i = 0; i < al.size(); i++) {
            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put("inCzech", al.get(i).getInCzech());
            temp.put("inPinyin", al.get(i).getInPinyin());
            temp.put("inChinese", al.get(i).getInChinese());
            temp.put("id", String.valueOf(al.get(i).getId()));
            list.add(temp);
        }
    }
}
