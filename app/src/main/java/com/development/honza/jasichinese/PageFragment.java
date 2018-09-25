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
    TextView tv_inChinese, tv_inPinyin, tv_inCzech;
    ImageButton imageButton;

    private int currentCzech;
    private int currentPinyin;
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
        tv_inChinese = (TextView)view.findViewById(R.id.tv_fragmentDetailInChinese);
        tv_inPinyin = (TextView)view.findViewById(R.id.tv_fragmentDetailInPinyin);
        tv_inCzech = (TextView)view.findViewById(R.id.tv_fragmentDetailInCzech);
        imageButton = (ImageButton)view.findViewById(R.id.imageButton_play);
        Bundle bundle = getArguments();
        final String inChinese = bundle.getString("inChinese");
        final String inCzech = bundle.getString("inCzech");
        final String inPinyin = bundle.getString("inPinyin");

        flashcard = bundle.getString("flashcard");

        tv_inChinese.setText(inChinese);
        tv_inPinyin.setText(inPinyin);
        tv_inCzech.setText(inCzech);

        tv_inPinyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPinyin = tv_inPinyin.getCurrentTextColor();

                if (currentPinyin == transparentColor) {
                    tv_inPinyin.setTextColor(blackColor);
                } else {
                    tv_inPinyin.setTextColor(transparentColor);
                }
            }
        });

        tv_inCzech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCzech = tv_inCzech.getCurrentTextColor();

                if (currentCzech == transparentColor) {
                    tv_inCzech.setTextColor(blackColor);
                } else {
                    tv_inCzech.setTextColor(transparentColor);
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FlashcardRead fr = new FlashcardRead();
                switch (flashcard.toLowerCase()) {
                    case "znaky" :
                        fr.testTextToSpeech(getContext(), inChinese);
                        break;
                    case "čeština" :
                        fr.testTextToSpeech(getContext(), inPinyin);
                        break;
                    case "pinyin" :
                        fr.testTextToSpeech(getContext(), inPinyin);
                        break;
                }
            }
        });
        return view;
    }



}
