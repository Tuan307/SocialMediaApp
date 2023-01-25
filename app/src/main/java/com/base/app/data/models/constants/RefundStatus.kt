package com.base.app.data.models.constants

enum class RefundStatus(val value: String) {
    NEW("new"),
    CONFIRMED("confirmed"),
    REFUSE("refuse"),
    SUCCESS("success"),
    FAIL("fail")
}