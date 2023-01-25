package com.base.app.data.models.response


import com.google.gson.annotations.SerializedName

data class ReasonsResponse(
    @SerializedName("meta")
    var meta: Meta?,
    @SerializedName("data")
    var `data`: List<Data?>?
) {
    data class Meta(
        @SerializedName("page")
        var page: Int?,
        @SerializedName("take")
        var take: Int?,
        @SerializedName("total")
        var total: Int?,
        @SerializedName("totalPage")
        var totalPage: Int?,
        @SerializedName("hasPreviousPage")
        var hasPreviousPage: Boolean?,
        @SerializedName("hasNextPage")
        var hasNextPage: Boolean?
    )

    data class Data(
        @SerializedName("id")
        var id: String?,
        @SerializedName("createdAt")
        var createdAt: String?,
        @SerializedName("updatedAt")
        var updatedAt: String?,
        @SerializedName("deletedAt")
        var deletedAt: String?,
        @SerializedName("code")
        var code: String?,
        @SerializedName("content")
        var content: String?,
        @SerializedName("type")
        var type: String?,
        @SerializedName("status")
        var status: String?
    )
}