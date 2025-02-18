package com.mstech.lifeline.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mstech.lifeline.R;
import com.mstech.lifeline.adapter.MessagePagerAdapter;

/** HARISH GADDAM */

public class Messagescreen extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagescreen);
        setActionBarTitle();

        MessagePagerAdapter pager= new MessagePagerAdapter(getSupportFragmentManager());
        tabLayout= findViewById(R.id.MyTabmessage);
        viewPager= findViewById(R.id.myViewPagermessage);
        viewPager.setAdapter(pager);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void setActionBarTitle() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Messages");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
