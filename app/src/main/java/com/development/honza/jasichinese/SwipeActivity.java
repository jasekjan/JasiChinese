package com.development.honza.jasichinese;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import com.development.honza.jasichinese.db.Characters;
import com.development.honza.jasichinese.db.CharactersOpenHelper;

import java.util.ArrayList;

/**
 * Created by Honza on 9. 3. 2018.
 */

public class SwipeActivity extends FragmentActivity {
    ViewPager viewPager;
    CharactersOpenHelper db;
    Integer count;
    final Context context = this;
    private Button button;
    private EditText userInput;
    Integer pozice = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ArrayList<Characters> charactersArrayList;
        db = new CharactersOpenHelper(this);

        String category = getIntent().getStringExtra("category");
        String poradi = getIntent().getStringExtra("poradi");
        final String flashCard = getIntent().getStringExtra("flashcard");
        charactersArrayList = db.getAllLCharacters(category, poradi, flashCard);

        setContentView(R.layout.actitvity_swipe);

        count = charactersArrayList.size();

        FragmentStatePagerAdapter fspa = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Characters characters;
                characters = charactersArrayList.get(position);
                Fragment fragment = new PageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("inChinese", characters.getInChinese());
                bundle.putString("inCzech", characters.getInCzech());
                bundle.putString("inPinyin", characters.getInPinyin());
                bundle.putString("flashcard", flashCard);
                fragment.setArguments(bundle);

                EditText et = (EditText)findViewById(R.id.editText_set_position);
                et.setText(String.valueOf(viewPager.getCurrentItem() +1));
                et.setSelection(et.getText().length());

                return fragment;
            }

            /*
            @Override
            public float getPageWidth(final int position) {
                // this will have 3 pages in a single view
                return 0.59f;
            }
            */

            @Override
            public CharSequence getPageTitle(int position) {
                return String.valueOf(position +1)+"/"+String.valueOf(getCount());
            }

            @Override
            public int getCount() {
                return count;
            }
        };

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        viewPager.setAdapter(fspa);
    }


    /*public void showAlert(View view) {
        // get prompts.xml view

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt_swipe_start, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editText_prompt_start_position);

        userInput.setText(String.valueOf(viewPager.getCurrentItem()));

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                pozice = Integer.valueOf(String.valueOf(userInput.getText()));
                            }
                        })
                .setNeutralButton("Storno", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    */

    public void SwipeSetPosition(View view) {
        TextView tv = (TextView)findViewById(R.id.editText_set_position);
        viewPager.setCurrentItem(Integer.valueOf(String.valueOf(tv.getText())) -1);
    }
}
