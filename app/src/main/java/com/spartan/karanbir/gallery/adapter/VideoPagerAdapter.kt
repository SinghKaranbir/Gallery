package com.spartan.karanbir.gallery.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity

import com.spartan.karanbir.gallery.VideoFragment
import com.spartan.karanbir.gallery.model.Animal

import java.util.ArrayList

/**
 * @author karanbir
 * *
 * @since 5/20/17.
 */

class VideoPagerAdapter(context: Context, private var mAnimals: ArrayList<Animal>?) : FragmentPagerAdapter((context as AppCompatActivity).supportFragmentManager) {

    override fun getItem(position: Int): Fragment {
        val animal = mAnimals!![position]
        return VideoFragment.newInstance(animal.videoUrl)
    }

    override fun getCount(): Int {
        return mAnimals!!.size
    }

    fun setData(animals: ArrayList<Animal>) {
        notifyDataSetChanged()
        mAnimals = animals
    }
}
