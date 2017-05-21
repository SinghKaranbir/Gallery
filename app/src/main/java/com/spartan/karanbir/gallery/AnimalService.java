package com.spartan.karanbir.gallery;

import com.spartan.karanbir.gallery.model.Animal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author karanbir.
 * @since 5/20/17.
 */

public interface AnimalService {

    @GET("pictures")
    Call<List<Animal>> getAnimals();
}
