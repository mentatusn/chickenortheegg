package com.dilemma.chickenortheegg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.getTabAt(1).select();
        navigateTo(MainFragment.LANG_JAVA);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    navigateTo(MainFragment.LANG_JAVA);
                } else {
                    navigateTo(MainFragment.LAMG_KOTLIN);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }





    private void showMessage(String text) {
        if(!isFinishing())
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void navigateTo(int type) {

        String tag = MainFragment.class.getSimpleName() + "," + type;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = MainFragment.getInstance(type);
            transaction.replace(R.id.container, fragment, tag);
        } else {
            transaction.show(fragment);
        }
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .commit();
    }


}