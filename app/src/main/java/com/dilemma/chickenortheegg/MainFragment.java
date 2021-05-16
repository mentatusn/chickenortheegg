package com.dilemma.chickenortheegg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private static final String KEY_LANG = "LANG";
    private static final String JAVA_UNIT_1 = "com.dilemma.chickenortheegg.javaunits.Unit1";
    private static final String JAVA_UNIT_2 = "Unit2";
    private static final String JAVA_UNIT_3 = "Unit2";


    private static final String KOTLIN_UNIT_ = "KOTLIN_UNIT_";

    public static final int LANG_JAVA = 1;
    public static final int LAMG_KOTLIN = 2;


    private ProgressBar progress;


    private int lang;

    public static MainFragment getInstance(int type) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LANG, type);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            lang = getArguments() != null ? getArguments().getInt(KEY_LANG) : 0;
        } else {
            lang = savedInstanceState.getInt(KEY_LANG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (lang == LANG_JAVA) {
            return inflater.inflate(R.layout.fragment_java, container, false);
        } else if (lang == LAMG_KOTLIN) {
            return inflater.inflate(R.layout.fragment_kotlin, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_rxjava, container, false);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        progress = view.findViewById(R.id.progressbar);

        if (lang == LAMG_KOTLIN) {
            initialKotlin(view);
        } else if (lang == LANG_JAVA) {
            initialJava(view);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            lang = savedInstanceState.getInt(KEY_LANG);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_LANG, lang);
        super.onSaveInstanceState(outState);
    }


    private void initialKotlin(View view) {
        //view.findViewById(R.id.kotlin_unit).setOnClickListener(l -> loadAd(KOTLIN_UNIT_));

    }

    private void initialJava(View view) {
        Log.d("mylogs",""+view.getClass());
        view.findViewById(R.id.java_unit_1).setOnClickListener(l -> loadUnit(JAVA_UNIT_1));
        view.findViewById(R.id.java_unit_2).setOnClickListener(l -> loadUnit(JAVA_UNIT_2));
    }


    private void loadUnit(final String unitName) {
        //progress.setVisibility(View.VISIBLE);

        try {
            Class<?> c = Class.forName(unitName);
            Intent intent = new Intent(getActivity(), c);
            startActivity(intent);
        } catch (ClassNotFoundException ignored) {
        }


    }



    private void showToast(String text) {
        if (getContext() != null && isAdded()) {
            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
        }
    }


}
