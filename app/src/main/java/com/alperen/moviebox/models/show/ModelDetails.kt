package com.alperen.moviebox.models.show

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelDetails(
    val name: String?,
    val summary: String?,
    val rating: String?,
    val image: String?,
    val genres: List<String?>?,
): Parcelable
