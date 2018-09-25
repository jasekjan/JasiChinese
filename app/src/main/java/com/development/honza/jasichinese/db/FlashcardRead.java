package com.development.honza.jasichinese.db;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

import com.development.honza.jasichinese.MainActivity;
import com.development.honza.jasichinese.PageFragment;

import java.util.HashMap;
import java.util.Locale;

public class FlashcardRead {
    TextToSpeech tts;
    private String txt;
    private String text;
    private Context context;

    public  FlashcardRead(){};

    public FlashcardRead(Context view, String text){
        this.text = text;
        this.context = view;
    }

    public void testTextToSpeech(Context context, String text) {
        final String toSpeak = text;
        final int mode = TextToSpeech.QUEUE_FLUSH;
        final HashMap<String, String> hashMap = new HashMap<String, String>();

        SettingsOpenHelper db = new SettingsOpenHelper(context);
        final Settings setting = db.findSettingsById(1);


        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {

                    tts.setLanguage(Locale.CHINESE);
                    if (setting == null || setting.getSpeed() == 0) {
                        tts.setSpeechRate(0.43f);
                    } else {
                        tts.setSpeechRate(setting.getSpeed());
                    }
                    tts.speak(toSpeak, mode, hashMap);
                }
            }
        });
    }
}
