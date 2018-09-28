package com.development.honza.jasichinese;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.development.honza.jasichinese.db.FlashcardRead;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {
    TextView tv_myForeign, tv_myReading, tv_myLang;
    ImageButton imageButton;

    private int currentMyLang;
    private int currentMyReading;
    private int transparentColor = Color.TRANSPARENT;
    private int blackColor = Color.BLACK;

    private String flashcard;

    public PageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page_layout, container, false);
        tv_myForeign = (TextView)view.findViewById(R.id.tv_fragmentDetailShow);
        tv_myReading = (TextView)view.findViewById(R.id.tv_fragmentDetailReading);
        tv_myLang = (TextView)view.findViewById(R.id.tv_fragmentDetailMyLanguage);
        imageButton = (ImageButton)view.findViewById(R.id.ib_fragment_play);

        Bundle bundle = getArguments();
        final String myForeign = bundle.getString("myForeign");
        final String myLang = bundle.getString("myLang");
        final String myReading = bundle.getString("myReading");

        flashcard = bundle.getString("flashcard");

        tv_myForeign.setText(myForeign);
        tv_myReading.setText(myReading);
        tv_myLang.setText(myLang);

        tv_myReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMyReading = tv_myReading.getCurrentTextColor();

                if (currentMyReading == transparentColor) {
                    tv_myReading.setTextColor(blackColor);
                } else {
                    tv_myReading.setTextColor(transparentColor);
                }
            }
        });

        tv_myLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMyLang = tv_myLang.getCurrentTextColor();

                if (currentMyLang == transparentColor) {
                    tv_myLang.setTextColor(blackColor);
                } else {
                    tv_myLang.setTextColor(transparentColor);
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FlashcardRead fr = new FlashcardRead();
                switch (flashcard.toLowerCase()) {
                    case "znaky" :
                        fr.testTextToSpeech(getContext(), myForeign);
                        break;
                    case "čeština" :
                        fr.testTextToSpeech(getContext(), myReading);
                        break;
                    case "pinyin" :
                        fr.testTextToSpeech(getContext(), myReading);
                        break;
                }
            }
        });
        return view;
    }



}
