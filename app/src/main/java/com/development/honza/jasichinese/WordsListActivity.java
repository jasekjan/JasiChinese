package com.development.honza.jasichinese;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.development.honza.jasichinese.db.Words;
import com.development.honza.jasichinese.db.WordsOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import android.widget.Switch;

public class WordsListActivity extends Activity {

    static int WORD_SAVED = 0;
    static int WORD_ADDED = 0;
    static int WORD_SAVED_OK = 1;
    ListView listView;
    static final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private EditText editText;
    private Switch aSwitch;
    private String categorySql;
    private ArrayList<Words> words;
    private WordsOpenHelper db;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list);

        category = getIntent().getStringExtra("category");
        if (category.equals("vše")) { category = "all"; };

        String poradi = getIntent().getStringExtra("poradi");

        registerForContextMenu((ListView)findViewById(R.id.lv_words));
        listView = (ListView) findViewById(R.id.lv_words);
        aSwitch = (Switch) findViewById(R.id.switch_category);

        db = new WordsOpenHelper(this);
        words = db.getAllLCharacters("all", poradi, "Znaky");

        if (aSwitch.isChecked()) {
            categorySql = category;
        } else {
            categorySql = "all";
        }

        final SimpleAdapter simpleAdapter = new SimpleAdapter(
                this, list,
                R.layout.row_layout,
                new String[]{"myLang", "myReading", "myForeign", "id"},
                new int[]{R.id.tv_myLang, R.id.tv_myReading, R.id.tv_myForeign, R.id.tv_hiddenId}
        );

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                        categorySql = category;
                } else {
                       categorySql = "all";
                }

                words = db.getAllLCharactersByAny(editText.getText().toString(), categorySql);

                populateList(words);
                listView.setAdapter(simpleAdapter);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap o = (HashMap) listView.getItemAtPosition(i);
                Intent intent = new Intent(view.getContext(), WordShowActivity.class);
                intent.putExtra("id", String.valueOf(o.get("id")));
                //startActivityForResult(intent, WORD_ADDED);
                startActivity(intent);
            }
        });

        editText = (EditText) findViewById(R.id.et_search);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    words = db.getAllLCharactersByAny(s.toString(), categorySql);

                    populateList(words);
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

        populateList(words);
        listView.setAdapter(simpleAdapter);

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_word, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_word_item_add:
                Intent intent = new Intent(listView.getContext(), WordShowActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateList(ArrayList<Words> al) {
        list.clear();
        for (int i = 0; i < al.size(); i++) {
            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put("myLang", al.get(i).getmyLang());
            temp.put("myReading", al.get(i).getmyReading());
            temp.put("myForeign", al.get(i).getmyForeign());
            temp.put("id", String.valueOf(al.get(i).getId()));
            list.add(temp);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
         MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_word_show, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        listView = (ListView) findViewById(R.id.lv_words);
        int position;

        AdapterView.AdapterContextMenuInfo ami = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        HashMap<String, String> itemInfo = new HashMap<>();
        itemInfo = (HashMap)listView.getItemAtPosition(ami.position);
        switch (item.getItemId()) {
            case R.id.menu_item_del:
                deleteWord( Integer.valueOf(String.valueOf(itemInfo.get("id"))));
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == WORD_SAVED) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                startActivity(getIntent());
            }
        }

        if (requestCode == WORD_ADDED) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                finish();
                startActivity(getIntent());
            }
        }
    }

    protected boolean deleteWord(Integer id) {
        boolean ret;
        WordsOpenHelper db = new WordsOpenHelper(getApplicationContext());
        ret = db.deleteRecord(id);
        finish();
        startActivity(getIntent());
        return ret;
    }

    public void addWord(View view) {
        Intent i = new Intent(view.getContext(), WordShowActivity.class);
        i.putExtra("id", "0");
        //startActivityForResult(i, WORD_SAVED);
        startActivity(i);
    }


}

