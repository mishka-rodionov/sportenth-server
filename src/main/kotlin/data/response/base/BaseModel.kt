package com.sportenth.data.response.base

import com.google.gson.annotations.SerializedName

open class BaseModel(
    @SerializedName("status")
    var status: Int? = null,

    @SerializedName("errors")
    val errors: List<BaseError>? = null
)