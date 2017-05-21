package com.spartan.karanbir.gallery.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.spartan.karanbir.gallery.VideoFragment;
import com.spartan.karanbir.gallery.model.Animal;

import java.util.ArrayList;

/**
 * @author karanbir
 * @since 5/20/17.
 */

public class VideoPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Animal> mAnimals;

    public VideoPagerAdapter(Context context, ArrayList<Animal> mAnimals) {
        super(((AppCompatActivity) context).getSupportFragmentManager());
        this.mAnimals = mAnimals;
    }

    @Override
    public Fragment getItem(int position) {
        Animal animal = mAnimals.get(position);
        return VideoFragment.newInstance(animal.getVideoUrl());
    }

    @Override
    public int getCount() {
        return mAnimals.size();
    }

    public void setData(ArrayList<Animal> animals){
        notifyDataSetChanged();
        mAnimals =  animals;
    }
}
