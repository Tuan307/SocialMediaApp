package com.base.app.data.models.response.list_bank


import com.base.app.data.models.response.product.Meta
import com.google.gson.annotations.SerializedName

data class ListBankResponse(
    @SerializedName("data")
    var `data`: List<Data>?,
    @SerializedName("meta")
    var meta: Meta?
)