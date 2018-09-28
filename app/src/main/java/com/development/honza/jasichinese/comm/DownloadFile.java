package com.development.honza.jasichinese.comm;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.development.honza.jasichinese.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Honza on 22. 4. 2018.
 */

public class DownloadFile extends AsyncTask<String, Integer, Integer> {

    private byte buffer;
    private String fileUrl;
    private TextView mTextView ;
    private ProgressBar mProgressBar;
    private Activity activity;
    int total = 0;

    public DownloadFile(Activity activity) {
        this.activity = activity;
        mTextView = (TextView) activity.findViewById(R.id.tv_download);
        mProgressBar = (ProgressBar) activity.findViewById(R.id.pb_download);
    }

    @Override
    protected Integer doInBackground(String... fileUrl) {
        int count;
        try {
           URL url = new URL(fileUrl[0]);
            URLConnection conection = url.openConnection();
            conection.connect();

            File filepath = Environment.getExternalStorageDirectory();
            File dir = new File(filepath.getAbsolutePath() + "/chineseData/");
            dir.mkdirs();
            File file = new File(dir, "listOfWords.csv");

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];
            String buffer = input.toString();

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
               // publishProgress((int) ((total * 100) / lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return total;
    }

    @Override
    protected void onPreExecute() {
        mProgressBar.setIndeterminate(true);
    }

    @Override
    protected void onPostExecute(Integer i) {

        mProgressBar.setIndeterminate(false);
        mTextView.setText("Staženo : 100 % (" +  i + " bytů)" );
    }

    // After each task done
    protected void onProgressUpdate(Integer... progress){
        // Display the progress on text view
       // mTextView.setText("Staženo : "+progress[0] + " %");
        // Update the progress bar
        //mProgressBar.setProgress(progress[0]);
    }

}