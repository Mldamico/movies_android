package com.mldamico.movies.models


import com.google.gson.annotations.SerializedName
import com.mldamico.movies.models.Result

data class Movies(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)