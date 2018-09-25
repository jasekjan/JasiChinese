package com.development.honza.jasichinese;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.development.honza.jasichinese.comm.DownloadFile;
import com.development.honza.jasichinese.comm.ImportData;
import com.development.honza.jasichinese.db.CharactersOpenHelper;
import com.development.honza.jasichinese.db.FlashcardRead;
import com.development.honza.jasichinese.db.Settings;
import com.development.honza.jasichinese.db.SettingsOpenHelper;
import com.development.honza.jasichinese.db.SyncActivity;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {
    private static final boolean AUTO_HIDE = false;
    Spinner mySpinner;
    ArrayList<String> categories;
    private View mContentView;
    public static final int progress_bar_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        CharactersOpenHelper db = new CharactersOpenHelper(getApplicationContext());
        db.deleteAll();
        */

        FlashcardRead fr = new FlashcardRead();
        fr.testTextToSpeech(getApplicationContext(), "");
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        populateSpinner();

    }

    public void showWordsCategory(View view) {
        Intent i = new Intent(MainActivity.this, WordsListActivity.class);
        Spinner sp = (Spinner) findViewById(R.id.spinner_category);
        Spinner spPoradi = (Spinner) findViewById(R.id.spinner_poradi);
        i.putExtra("poradi", spPoradi.getSelectedItem().toString());
        i.putExtra("category", sp.getSelectedItem().toString());
        startActivity(i);
    }

    public void showSwipeActivityCategory(View view) {
        String category;
        Intent i = new Intent(MainActivity.this, SwipeActivity.class);

        Spinner sp = (Spinner) findViewById(R.id.spinner_category);
        Spinner spPoradi = (Spinner) findViewById(R.id.spinner_poradi);
        Spinner spFlashcard = (Spinner) findViewById(R.id.spinner_flashcards);
        i.putExtra("poradi", spPoradi.getSelectedItem().toString());
        i.putExtra("category", sp.getSelectedItem().toString());
        i.putExtra("flashcard", spFlashcard.getSelectedItem().toString());
        startActivity(i);
    }

    public  void showSettings(View view) {
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);

        startActivity(i);
    }

    public void searchActivity(View view) {
        Intent i = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(i);
    }

    private ArrayList<String> getCategories() {
        CharactersOpenHelper db = new CharactersOpenHelper(this);
        return db.getCategories();
    }

    private void populateSpinner() {
        categories = getCategories();
        Spinner spinner = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item, categories);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        ArrayList<String> poradi = new ArrayList<>();
        poradi.add("Dle abecedy");
        poradi.add("Náhodně");
        Spinner spinnerPoradi = (Spinner)findViewById(R.id.spinner_poradi);
        ArrayAdapter adapterPoradi = new ArrayAdapter(this, R.layout.spinner_item, poradi);
        adapterPoradi.setDropDownViewResource(R.layout.spinner_item);
        spinnerPoradi.setAdapter(adapterPoradi);

        ArrayList<String> flashcard = new ArrayList<>();
        flashcard.add("Znaky");
        flashcard.add("Čeština");
        flashcard.add("Pinyin");
        Spinner spinnerFlashcard = (Spinner)findViewById(R.id.spinner_flashcards);
        ArrayAdapter adapterFlashcard = new ArrayAdapter(this, R.layout.spinner_item, flashcard);
        adapterFlashcard.setDropDownViewResource(R.layout.spinner_item);
        spinnerFlashcard.setAdapter(adapterFlashcard);
    }

    public void syncWords(View view) {
        String category;
        //category = String.valueOf((Spinner)findViewById(R.id.spinner_category).getId()));
        Intent i = new Intent(MainActivity.this, SyncActivity.class);
        SettingsOpenHelper db = new SettingsOpenHelper(MainActivity.this);
        Settings settings = db.findSettingsById(1);

        if (settings == null || settings.getPath() == null || settings.getPath().equals("")) {
            Toast.makeText(getApplicationContext(), "Nejdřív proveďte nastavení", Toast.LENGTH_LONG).show();
        } else {
            i.putExtra("fileurl", settings.getPath());
            startActivity(i);
        }
        populateSpinner();
    }
}
