package com.spartan.karanbir.gallery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spartan.karanbir.gallery.adapter.VideoPagerAdapter;
import com.spartan.karanbir.gallery.model.Animal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author karanbir
 * @since 5/20/17
 */

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private VideoPagerAdapter mVideoPagerAdapter;
    private ImagePagerAdapter mImagePagerAdapter;
    private ArrayList<Animal> mAnimals;
    private ViewPager mImagePager, mVideoPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Retrofit retrofit = setupRetrofit(getString(R.string.base_url));
        callApi(retrofit);
        mAnimals = new ArrayList<>();
        mVideoPager = (ViewPager) findViewById(R.id.video_pager);
        mImagePager = (ViewPager) findViewById(R.id.image_pager);
        mImagePager.setPageMargin(-250);
        mImagePager.setOffscreenPageLimit(3);
        mVideoPagerAdapter = new VideoPagerAdapter(this, mAnimals);
        mImagePagerAdapter = new ImagePagerAdapter(this, mAnimals);
        mImagePager.setAdapter(mImagePagerAdapter);
        mVideoPager.setAdapter(mVideoPagerAdapter);
        mImagePager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                final float normalizedPosition = Math.abs(Math.abs(position) - 1);
                view.setScaleX(normalizedPosition / 2 + 0.5f);
                view.setScaleY(normalizedPosition / 2 + 0.5f);
            }
        });

        mImagePager.addOnPageChangeListener(this);
        mVideoPager.addOnPageChangeListener(this);
    }


    private Retrofit setupRetrofit(String baseUrl) {
        Gson gson = new GsonBuilder().create();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    private void callApi(Retrofit retrofit) {
        AnimalService animalApi = retrofit.create(AnimalService.class);
        Call<List<Animal>> animalCall = animalApi.getAnimals();
        animalCall.enqueue(new Callback<List<Animal>>() {
            @Override
            public void onResponse(Call<List<Animal>> call, Response<List<Animal>> response) {
                int statusCode = response.code();
                Log.d(TAG, Integer.toString(statusCode));
                if (statusCode == 200) {
                    mAnimals.addAll(response.body());
                    mImagePagerAdapter.setData(mAnimals);
                    mVideoPagerAdapter.setData(mAnimals);
                }
            }

            @Override
            public void onFailure(Call<List<Animal>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        mImagePager.setCurrentItem(i, true);
        mVideoPager.setCurrentItem(i, true);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private class ImagePagerAdapter extends PagerAdapter {

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

            final ImageView imageView = (ImageView) itemView.findViewById(R.id.animal_image);
            imageView.setTag(position);
            Picasso.with(mContext).load(animal.getImageUrl())
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
            container.addView(itemView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    mImagePager.setCurrentItem(position);
                }
            });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

        public void setData(ArrayList<Animal> animals) {
            notifyDataSetChanged();
            mAnimals = animals;
        }

        @Override
        public float getPageWidth(int position) {
            return 0.90f;
        }
    }

}
