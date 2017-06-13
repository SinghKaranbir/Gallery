package com.spartan.karanbir.gallery

import com.spartan.karanbir.gallery.model.Animal
import retrofit2.Call
import retrofit2.http.GET

/**
 * @author karanbir
 * @since 6/13/17
 *
 */
interface AnimalService {

    @GET("pictures")
    fun getAnimals(): Call<List<Animal>>
}