package com.base.app.data.models.constants

enum class TransactionState(val value: String) {
    SUCCESS("success"),
    WAITING("processing"),
    FAILED("fail"),
    DRAFT("draft"),
    REFUSE("refuse"),
    CONFIRMED("confirmed"),
    NEW("new"),
    PENDING ("pending")
}