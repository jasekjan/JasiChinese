package com.development.honza.jasichinese.comm;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.development.honza.jasichinese.R;
import com.development.honza.jasichinese.db.Words;
import com.development.honza.jasichinese.db.WordsOpenHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Honza on 22. 4. 2018.
 */

public class ImportData extends AsyncTask<String, Integer, Integer> {
    private static String FILEURI = "/chineseData/listOfWords.csv";
    private String path;
    private File file;
    private Context context;
    private int totalLines = 0;
    private int currentLines = 0;
    private Activity activity;
    ;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private TextView mTextViewResult;
    private int countInserted = 0;
    private int countUpdated = 0;

    private String id, myLang, myReading, myForeign, category;
    private String[] tokens;

    public ImportData(Context context, Activity activity) {
        this.context = context;
        this.file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + FILEURI);
        this.activity = activity;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            WordsOpenHelper db = new WordsOpenHelper(context);
            ArrayList<Words> findChars;

            while ((line = br.readLine()) != null) {
                totalLines++;
            }

            br = new BufferedReader(new FileReader(file));


            mTextView = (TextView) activity.findViewById(R.id.tv_load);
            mProgressBar = (ProgressBar) activity.findViewById(R.id.pb_load);
            mTextViewResult = (TextView) activity.findViewById(R.id.tv_count);

            line = br.readLine();
            totalLines--;

            while ((line = br.readLine()) != null) {
                currentLines++;
                tokens = line.split(",");
                id = tokens[0];
                myLang = tokens[1];
                myReading = tokens[2];
                myForeign = tokens[3];
                category = tokens[4];

                publishProgress((int) ((((double) currentLines / totalLines)) * 100));

                Words words = new Words(myLang, myReading, myForeign, category);
                findChars = db.findCharactersForInsert(myForeign, myReading);
                Words findWord = new Words();
                boolean insUpd = true;

                if (findChars.size() > 0) {
                    //projdu vsechna nalezena slova, a když se nektere shoduje s nove vkladanym, tak se nic nestane, pokud se ale vsechna lisi, tak dojde normalne k insertu/updatu
                    for (int i = 0; i < findChars.size(); i++) {
                        findWord = findChars.get(0);
                        if (findWord == null || (!myLang.equals(findWord.getmyLang()) && !myReading.equals(findWord.getmyReading()))) {
                            db.addCharacters(words);
                            countInserted++;
                        } else {
                            if (findWord != null && myLang.equals(findWord.getmyLang()) && myForeign.equals(findWord.getmyForeign()) && (!category.equals(findWord.getCategory()) || !myReading.equals(findWord.getmyReading()))) {
                                findWord.setCategory(category);
                                findWord.setmyReading(myReading);
                                db.updateCharacter(findWord);
                                countUpdated++;
                            }
                        }
                    }

                } else {
                    db.addCharacters(words);
                    countInserted++;
                }
            }
            br.close();
            db.close();
        } catch (
                IOException e)

        {
            //You'll need to add proper error handling here
        }

        currentLines = 0;
        return countInserted * 31 + countUpdated;
    }


    // After each task done
    protected void onProgressUpdate(Integer... progress) {
        // Display the progress on text view
        mTextView.setText("Nahráno : " + progress[0] + " %");
        // Update the progress bar
        mProgressBar.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);
        Integer upd = i % 31;
        Integer ins = (i - upd) / 31;
        mTextViewResult.setText("Uloženo " + ins + " nových slovíček\nUpraveno " + upd + " slovíček");
    }
}
