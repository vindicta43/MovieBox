package com.alperen.moviebox.models.show

import com.alperen.moviebox.models.user.show.ModelShow
import com.google.gson.annotations.SerializedName

data class ModelSearch(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("show")
	val show: ModelShow? = null
)
