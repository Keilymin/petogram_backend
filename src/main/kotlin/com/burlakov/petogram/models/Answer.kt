package com.burlakov.petogram.models

import com.google.gson.annotations.SerializedName

class Answer(var message: String, @SerializedName(value = "positive") var isPositive: Boolean, var user: User?) {
    constructor(message: String, isPositive: Boolean) : this(message, isPositive, null)
}