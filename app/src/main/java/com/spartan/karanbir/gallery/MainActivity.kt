package com.spartan.karanbir.gallery

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.GsonBuilder
import com.spartan.karanbir.gallery.adapter.ImagePagerAdapter
import com.spartan.karanbir.gallery.adapter.OnImageClickedListener
import com.spartan.karanbir.gallery.adapter.VideoPagerAdapter
import com.spartan.karanbir.gallery.model.Animal
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, OnImageClickedListener {

    companion object {
        val TAG: String = MainActivity::class.java.canonicalName
    }

    private var mVideoPagerAdapter: VideoPagerAdapter? = null
    private var mImagePagerAdapter: ImagePagerAdapter? = null
    private var mAnimals: ArrayList<Animal>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Setting up Retrofit and call api
        callApi(retrofit = setupRetrofit(getString(R.string.base_url)))
        image_pager.pageMargin = -250
        image_pager.offscreenPageLimit = 3
        mAnimals = Collections.EMPTY_LIST as ArrayList<Animal>?
        mImagePagerAdapter = ImagePagerAdapter(this,mAnimals)
        mVideoPagerAdapter = VideoPagerAdapter(this,mAnimals)
        mImagePagerAdapter?.setOnImageClickedListener(this)
        image_pager.adapter = mImagePagerAdapter
        video_pager.adapter = mVideoPagerAdapter

        image_pager.setPageTransformer(false, ViewPager.PageTransformer { view, position ->
            val normalizedPosition = Math.abs(Math.abs(position) - 1)
            view.scaleX = normalizedPosition / 2 + 0.5f
            view.scaleY = normalizedPosition / 2 + 0.5f
        })

        image_pager.addOnPageChangeListener(this)
        image_pager.addOnPageChangeListener(this)
    }

    private fun setupRetrofit(baseUrl: String?): Retrofit {
        val gson = GsonBuilder().create()
        return Retrofit.Builder().
                baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    private fun callApi(retrofit: Retrofit?) {
        val animalApi = retrofit?.create(AnimalService::class.java)
        val animalCall = animalApi?.getAnimals()

        animalCall?.enqueue(object : Callback<List<Animal>> {
            override fun onResponse(call: Call<List<Animal>>, response: Response<List<Animal>>) {
                val statusCode = response.code()
                Log.d(TAG, Integer.toString(statusCode))
                if (statusCode == 200) {
                    mAnimals?.addAll(response.body() as List<Animal>)
                    if(mAnimals != null){
                        mImagePagerAdapter?.setData(mAnimals!!)
                        mVideoPagerAdapter?.setData(mAnimals!!)
                    }

                }
            }

            override fun onFailure(call: Call<List<Animal>>, t: Throwable) {

            }
        })
    }


    override fun onPageScrollStateChanged(state: Int) {
        TODO("not implemented")
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        TODO("not implemented")
    }

    override fun onPageSelected(position: Int) {
        image_pager.setCurrentItem(position, true)
        video_pager.setCurrentItem(position, true)
    }

    override fun onImageClicked(position: Int) {
        image_pager.setCurrentItem(position, true)
    }
}


