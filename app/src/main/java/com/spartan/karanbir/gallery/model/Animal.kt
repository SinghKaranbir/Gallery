package com.spartan.karanbir.gallery.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author karanbir
 * @since 6/13/17
 *
 */
data class Animal(@Expose @SerializedName("id") val id: kotlin.Int, @Expose @SerializedName("imageUrl") val imageUrl: String, @SerializedName("videoUrl") @Expose val videoUrl: String)