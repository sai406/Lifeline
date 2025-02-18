package com.mstech.lifeline.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mstech.lifeline.fragments.Chat;
import com.mstech.lifeline.fragments.Message;


public class MessagePagerAdapter extends FragmentPagerAdapter {

    public MessagePagerAdapter(FragmentManager fms) {
        super(fms);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frags= new Fragment();

        if(position==0)
        {
            frags = new Message();
        }

        if(position==1)
        {
            // hide on 29/8/2020
            frags = new Chat();
        }
        return frags;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        CharSequence ch="";

        if(position==0)
        {
            ch="Messages";

        }
        if(position==1)
        {
            ch="Chat";
        }

        return ch;
    }
}

