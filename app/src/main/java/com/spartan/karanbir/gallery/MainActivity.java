package com.spartan.karanbir.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spartan.karanbir.gallery.adapter.ImagePagerAdapter;
import com.spartan.karanbir.gallery.adapter.OnImageClickedListener;
import com.spartan.karanbir.gallery.adapter.VideoPagerAdapter;
import com.spartan.karanbir.gallery.model.Animal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author karanbir
 * @since 5/20/17
 */

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, OnImageClickedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private VideoPagerAdapter mVideoPagerAdapter;
    private ImagePagerAdapter mImagePagerAdapter;
    private ArrayList<Animal> mAnimals;
    @BindView(R.id.video_pager)
    ViewPager mVideoPager;
    @BindView(R.id.image_pager)
    ViewPager mImagePager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Retrofit retrofit = setupRetrofit(getString(R.string.base_url));
        callApi(retrofit);
        mAnimals = new ArrayList<>();
        mImagePager.setPageMargin(-250);
        mImagePager.setOffscreenPageLimit(3);
        mVideoPagerAdapter = new VideoPagerAdapter(this, mAnimals);
        mImagePagerAdapter = new ImagePagerAdapter(this, mAnimals);
        mImagePagerAdapter.setOnImageClickedListener(this);
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

    @Override
    public void onImageClicked(int position) {
        mImagePager.setCurrentItem(position, true);
    }
}
