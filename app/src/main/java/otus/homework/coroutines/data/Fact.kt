package otus.homework.coroutines.data

import com.google.gson.annotations.SerializedName

data class Fact(
	@field:SerializedName("fact")
	val text: String,
	@field:SerializedName("length")
	val length: Int
)