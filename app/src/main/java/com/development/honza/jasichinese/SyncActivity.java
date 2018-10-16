package com.development.honza.jasichinese;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.development.honza.jasichinese.R;
import com.development.honza.jasichinese.comm.DownloadFile;
import com.development.honza.jasichinese.comm.ImportData;
import com.development.honza.jasichinese.db.WordsOpenHelper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SyncActivity extends AppCompatActivity {
    String fileUrl;
    Integer ACTIVITY_CHOOSE_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        fileUrl = getIntent().getStringExtra("fileurl");

    }

    public void loadVocabulary(View view) {
        try {
            if (isNetworkConnected()) {
                int corePoolSize = 60;
                int maximumPoolSize = 80;
                int keepAliveTime = 10;

                BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
                Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

                if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
                    new DownloadFile(this).executeOnExecutor(threadPoolExecutor, fileUrl);

                    new ImportData(this, this).executeOnExecutor(threadPoolExecutor);
                } else {
                    new DownloadFile(this).execute(fileUrl);

                    new ImportData(this, this).execute();
                 }
                    Toast.makeText(getApplicationContext(), "Hotovo", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Nejste připojeni k internetu", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public  void deleteAll(final View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        WordsOpenHelper db = new WordsOpenHelper(getApplicationContext());
                        db.deleteAll();
                        Toast.makeText(view.getContext(), "Všechna slovíčka byla smazána.", Toast.LENGTH_SHORT).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Toast.makeText(view.getContext(), "Slovíčka nebyla smazána.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Opravdu chcete vše smazat?").setPositiveButton("Ano", dialogClickListener)
                .setNegativeButton("NE", dialogClickListener).show();

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        String path     = "";
        if(requestCode == ACTIVITY_CHOOSE_FILE)
        {
            Uri uri = data.getData();
            String FilePath = getRealPathFromURI(uri); // should the path be here in this string
            System.out.print("Cesta  = " + FilePath);
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String [] proj      = {MediaStore.Images.Media.DATA};
        Cursor cursor       = getContentResolver().query( contentUri, proj, null, null,null);
        if (cursor == null) return null;
        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
