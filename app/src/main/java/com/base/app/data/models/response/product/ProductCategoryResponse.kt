package com.base.app.data.models.response.product

import com.google.gson.annotations.SerializedName

class ProductCategoryResponse(
    @SerializedName("meta") var meta: Meta? = null,
    @SerializedName("data") var data: ArrayList<Category>? = null
) {
    data class Meta(

        @SerializedName("page") var page: Int? = null,
        @SerializedName("take") var take: Int? = null,
        @SerializedName("total") var total: Int? = null,
        @SerializedName("totalPage") var totalPage: Int? = null,
        @SerializedName("hasPreviousPage") var hasPreviousPage: Boolean? = null,
        @SerializedName("hasNextPage") var hasNextPage: Boolean? = null

    )
}


