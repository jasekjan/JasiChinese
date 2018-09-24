package com.development.honza.jasichinese;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.development.honza.jasichinese.db.Characters;
import com.development.honza.jasichinese.db.CharactersOpenHelper;
import com.development.honza.jasichinese.db.FlashcardRead;

/**
 * Created by Honza on 19. 3. 2018.
 */

public class WordShowActivity extends Activity {
    Integer itemId;
    Characters characters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CharactersOpenHelper db = new CharactersOpenHelper(this);
        itemId = Integer.valueOf(getIntent().getStringExtra("id"));

        setContentView(R.layout.activity_show_word);
        EditText id = (EditText) findViewById(R.id.editText_show_id);
        ImageButton imageButton = (ImageButton)findViewById(R.id.imageButton_detail_play);
        id.setText(String.valueOf(itemId));

        if (itemId > 0) {
            characters = db.findCharactersById(itemId);

            EditText czech = (EditText) findViewById(R.id.editText_show_czech);
            final EditText chinese = (EditText) findViewById(R.id.editText_show_chinese);
            EditText pinyin = (EditText) findViewById(R.id.editText_show_pinyin);
            EditText category = (EditText) findViewById(R.id.editText_show_category);

            czech.setText(characters.getInCzech());
            chinese.setText(characters.getInChinese());
            pinyin.setText(characters.getInPinyin());
            category.setText(characters.getCategory());

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FlashcardRead fr = new FlashcardRead();
                    fr.testTextToSpeech(getApplicationContext(), chinese.getText().toString());
                }
            });
        }
    }



    public void saveWord(View view) {
        Characters characterToSave = new Characters();
        CharactersOpenHelper db = new CharactersOpenHelper(this);

        EditText czech = (EditText) findViewById(R.id.editText_show_czech);
        EditText chinese = (EditText) findViewById(R.id.editText_show_chinese);
        EditText pinyin = (EditText) findViewById(R.id.editText_show_pinyin);
        EditText category = (EditText) findViewById(R.id.editText_show_category);
        EditText id = (EditText) findViewById(R.id.editText_show_id);

        characterToSave.setInChinese(String.valueOf(chinese.getText()));
        characterToSave.setInCzech(String.valueOf(czech.getText()));
        characterToSave.setInPinyin(String.valueOf(pinyin.getText()));
        characterToSave.setCategory(String.valueOf(category.getText()));

        if (Integer.valueOf(String.valueOf(id.getText())) > 0) {
            characterToSave.setId(Integer.valueOf(String.valueOf(id.getText())));
            db.updateCharacter(characterToSave);
        } else {
            db.addCharacters(characterToSave);
        }

        if (getParent() == null) {
            setResult(Activity.RESULT_OK, getIntent());
        } else {
            getParent().setResult(Activity.RESULT_OK, getIntent());
        }
        finish();
    }

}
