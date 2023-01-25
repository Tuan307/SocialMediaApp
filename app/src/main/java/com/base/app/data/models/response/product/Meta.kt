package com.base.app.data.models.response.product


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("hasNextPage")
    var hasNextPage: Boolean?=null,
    @SerializedName("hasPreviousPage")
    var hasPreviousPage: Boolean?=null,
    @SerializedName("page")
    var page: Int?=null,
    @SerializedName("take")
    var take: Int?=null,
    @SerializedName("total")
    var total: Int?=null,
    @SerializedName("totalPage")
    var totalPage: Int?=null
)