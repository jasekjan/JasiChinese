package com.development.honza.jasichinese;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;

import com.development.honza.jasichinese.db.Words;
import com.development.honza.jasichinese.db.WordsOpenHelper;
import com.development.honza.jasichinese.db.FlashcardRead;

/**
 * Created by Honza on 19. 3. 2018.
 */

public class WordShowActivity extends Activity {
    Integer itemId;
    Words words;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WordsOpenHelper db = new WordsOpenHelper(this);
        itemId = Integer.valueOf(getIntent().getStringExtra("id"));

        setContentView(R.layout.activity_show_word);
        EditText id = (EditText) findViewById(R.id.et_show_id);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_detail_play);
        id.setText(String.valueOf(itemId));

        if (itemId > 0) {
            words = db.findCharactersById(itemId);

            EditText myLang = (EditText) findViewById(R.id.et_show_myLang);
            final EditText myForeign = (EditText) findViewById(R.id.et_show_myForeign);
            EditText myReading = (EditText) findViewById(R.id.et_show_myReading);
            EditText category = (EditText) findViewById(R.id.et_show_category);

            myLang.setText(words.getmyLang());
            myForeign.setText(words.getmyForeign());
            myReading.setText(words.getmyReading());
            category.setText(words.getCategory());

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FlashcardRead fr = new FlashcardRead();
                    fr.testTextToSpeech(getApplicationContext(), myForeign.getText().toString());
                }
            });
        }
    }



    public void saveWord(View view) {
        Words wordsToSave = new Words();
        WordsOpenHelper db = new WordsOpenHelper(this);

        EditText czech = (EditText) findViewById(R.id.et_show_myLang);
        EditText chinese = (EditText) findViewById(R.id.et_show_myForeign);
        EditText pinyin = (EditText) findViewById(R.id.et_show_myReading);
        EditText category = (EditText) findViewById(R.id.et_show_category);
        EditText id = (EditText) findViewById(R.id.et_show_id);

        wordsToSave.setmyForeign(String.valueOf(chinese.getText()));
        wordsToSave.setmyLang(String.valueOf(czech.getText()));
        wordsToSave.setmyReading(String.valueOf(pinyin.getText()));
        wordsToSave.setCategory(String.valueOf(category.getText()));

        if (Integer.valueOf(String.valueOf(id.getText())) > 0) {
            wordsToSave.setId(Integer.valueOf(String.valueOf(id.getText())));
            db.updateCharacter(wordsToSave);
        } else {
            db.addCharacters(wordsToSave);
        }

        if (getParent() == null) {
            setResult(Activity.RESULT_OK, getIntent());
        } else {
            getParent().setResult(Activity.RESULT_OK, getIntent());
        }
        finish();
    }

}
