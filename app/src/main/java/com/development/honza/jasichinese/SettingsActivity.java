package com.development.honza.jasichinese;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.development.honza.jasichinese.db.Settings;
import com.development.honza.jasichinese.db.SettingsOpenHelper;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Settings settings;

        SettingsOpenHelper db = new SettingsOpenHelper(this);

        settings = db.findSettingsById(1);

        if (!(settings == null)) {
            EditText editText_speed = (EditText) findViewById(R.id.et_speed);
            EditText editText_path = (EditText) findViewById(R.id.et_path);

            editText_path.setText(settings.getPath());
            editText_speed.setText(String.valueOf(settings.getSpeed()));
        }
    }

    @Override
    public void onBackPressed() {
        saveSettings();
        super.onBackPressed();
    }

    public void saveSettings(View view) {
        saveSettings();
    }

    public void saveSettings() {
        SettingsOpenHelper db = new SettingsOpenHelper(this);
        Settings settings = new Settings();
        EditText path = (EditText)findViewById(R.id.et_path);
        EditText speed = (EditText)findViewById(R.id.et_speed);

        if (path.getText().toString().replaceAll("\\s+", "").equals("") || speed.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Rychlost musí být mezi 0 a 1, URL musí být vyplněné.\nPro defaultní nastavení stiskněte růžové tlačítko.", Toast.LENGTH_LONG).show();
        } else {
            settings.setId(1);
            settings.setPath(path.getText().toString());
            settings.setSpeed(Float.valueOf(speed.getText().toString()));

            Settings settings2 = db.findSettingsById(1);
            if (settings2 == null) {
                db.addSettings(settings);
            } else {
                db.updateSettings(settings);
            }
        }
    }

    public void setDefault(View view) {
        EditText path = (EditText)findViewById(R.id.et_path);
        EditText speed = (EditText)findViewById(R.id.et_speed);

        path.setText("https://docs.google.com/spreadsheets/d/e/2PACX-1vR8d-0ZEZiu17iR-iBfa5OP0BOaN2N9x3y0aT8o2x78wOT_P2ypdlX3aExqNByRsRv1aP08UpUZHBYj/pub?output=csv&ndplr=1");
        speed.setText(String.valueOf(0.47));
    }

}
