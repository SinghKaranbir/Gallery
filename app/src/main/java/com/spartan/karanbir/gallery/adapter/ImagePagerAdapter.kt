package com.spartan.karanbir.gallery.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout

import com.spartan.karanbir.gallery.R
import com.spartan.karanbir.gallery.model.Animal
import com.squareup.picasso.Picasso

import java.util.ArrayList

/**
 * Created by karanbir on 6/6/17.
 */

class ImagePagerAdapter(private val mContext: Context, private var mAnimals: ArrayList<Animal>?) : PagerAdapter() {
    private val mLayoutInflater: LayoutInflater
    private var mCallback: OnImageClickedListener? = null

    init {
        mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return mAnimals!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.image_pager_item, container, false)
        val animal = mAnimals!![position]

        val imageView = itemView.findViewById(R.id.animal_image) as ImageView
        imageView.tag = position
        Picasso.with(mContext).load(animal.imageUrl)
                .error(R.mipmap.ic_launcher)
                .into(imageView)
        container.addView(itemView)
        imageView.setOnClickListener { v ->
            val position = v.tag as Int
            mCallback!!.onImageClicked(position)
        }

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    fun setData(animals: ArrayList<Animal>) {
        notifyDataSetChanged()
        mAnimals = animals
    }

    override fun getPageWidth(position: Int): Float {
        return 0.90f
    }

    fun setOnImageClickedListener(imageClickedListener: OnImageClickedListener) {
        mCallback = imageClickedListener
    }
}


