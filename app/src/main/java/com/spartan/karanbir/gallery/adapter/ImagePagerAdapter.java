package com.spartan.karanbir.gallery.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.spartan.karanbir.gallery.R;
import com.spartan.karanbir.gallery.model.Animal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @author karanbir
 * @since 5/20/17
 */

public class ImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Animal> mAnimals;

    public ImagePagerAdapter(Context context, ArrayList<Animal> animals) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAnimals = animals;
    }

    @Override
    public int getCount() {
        return mAnimals.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.image_pager_item, container, false);
        Animal animal = mAnimals.get(position);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.animal_image);
        Picasso.with(mContext).load(animal.getImageUrl())
                .error(R.mipmap.ic_launcher)
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public void setData(ArrayList<Animal> animals){
        notifyDataSetChanged();
        mAnimals =  animals;
    }

    @Override
    public float getPageWidth(int position) {
        return 0.90f;
    }
}
