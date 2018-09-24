package com.development.honza.jasichinese;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.development.honza.jasichinese.db.Characters;
import com.development.honza.jasichinese.db.CharactersOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.Toast;

public class WordsListActivity extends Activity {

    static int WORD_SAVED = 0;
    static int WORD_ADDED = 0;
    static int WORD_SAVED_OK = 1;
    ListView listView;
    static final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list);
        String category = getIntent().getStringExtra("category");
        String poradi = getIntent().getStringExtra("poradi");

        final ArrayList<Characters> charactersArrayList;

        registerForContextMenu((ListView)findViewById(R.id.listView_words));
        listView = (ListView) findViewById(R.id.listView_words);

        CharactersOpenHelper db = new CharactersOpenHelper(this);

        charactersArrayList = db.getAllLCharacters(category, poradi, "Znaky");
        Toast.makeText(getApplicationContext(), "Kokotina", Toast.LENGTH_LONG);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(
                this, list,
                R.layout.row_layout,
                new String[]{"inCzech", "inPinyin", "inChinese", "id"},
                new int[]{R.id.tv_inCzech, R.id.tv_inPinyin, R.id.tv_inChinese, R.id.tv_hiddenId}
        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap o = (HashMap) listView.getItemAtPosition(i);
                Intent intent = new Intent(view.getContext(), WordShowActivity.class);
                intent.putExtra("id", String.valueOf(o.get("id")));
                startActivityForResult(intent, WORD_ADDED);
            }
        });

        populateList(charactersArrayList);
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


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
         MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_word_show, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        listView = (ListView) findViewById(R.id.listView_words);
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
        CharactersOpenHelper db = new CharactersOpenHelper(getApplicationContext());
        ret = db.deleteRecord(id);
        finish();
        startActivity(getIntent());
        return ret;
    }

    public void addWord(View view) {
        Intent i = new Intent(view.getContext(), WordShowActivity.class);
        i.putExtra("id", "0");
        startActivityForResult(i, WORD_SAVED);
    }


}

