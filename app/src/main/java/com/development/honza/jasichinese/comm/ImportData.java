package com.development.honza.jasichinese.comm;

import android.app.Activity;
import android.content.Context;
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
    private Activity activity;;
    private TextView mTextView ;
    private ProgressBar mProgressBar;
    private TextView mTextViewResult;
    private int countInserted;

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
            Words findChar;

            while ((line = br.readLine()) != null) {
                totalLines++;
            }

            br = new BufferedReader(new FileReader(file));


            mTextView = (TextView) activity.findViewById(R.id.tv_load);
            mProgressBar = (ProgressBar) activity.findViewById(R.id.pb_load);
            mTextViewResult = (TextView) activity.findViewById(R.id.tv_count);

            while ((line = br.readLine()) != null) {
                currentLines++;
                tokens = line.split(",");
                id = tokens[0];
                myLang = tokens[1];
                myReading = tokens[2];
                myForeign = tokens[3];
                category = tokens[4];

                publishProgress( (int)(currentLines * 100) / totalLines);


                if (!id.equals("id")) {
                    Words words = new Words(myLang, myReading, myForeign, category);
                    findChar = db.findCharactersByChinese(myForeign);
                    if (findChar == null && myLang != null) {
                        if (findChar == null || !findChar.equals(words)) {
                            db.addCharacters(words);
                            countInserted++;
                        }
                    }
                }
            }
            br.close();
            db.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return countInserted;
    }


    // After each task done
    protected void onProgressUpdate(Integer... progress){
        // Display the progress on text view
        mTextView.setText("Nahráno : "+progress[0] + " %");
        // Update the progress bar
        mProgressBar.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);

        mTextViewResult.setText("Uloženo " + i + " nových slovíček");
    }
}
