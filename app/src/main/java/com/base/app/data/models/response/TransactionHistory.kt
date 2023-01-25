package com.base.app.data.models.response

data class TransactionHistory(
    var day: DayTransactionResponse.Data? = null,
    var listTransaction: TransactionMerchantResponse? = null,
    var isShow: Boolean = false
)