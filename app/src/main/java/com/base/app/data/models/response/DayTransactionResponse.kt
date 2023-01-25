package com.base.app.data.models.response


import com.google.gson.annotations.SerializedName

open class DayTransactionResponse {
    @SerializedName("data")
    var data: List<Data>? = null

    open class Data {
        @SerializedName("count")
        var count: String? = null

        @SerializedName("date")
        var date: String? = null

        @SerializedName("sum")
        var sum: String? = null
    }
}

