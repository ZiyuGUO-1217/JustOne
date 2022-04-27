package com.example.justone.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WordTranslatorRequest(
    @SerializedName("source") val word: String,
    @SerializedName("trans_type") val translationType: String,
    @SerializedName("request_id") val requestId: String = "demo",
    @SerializedName("detect") val shouldDetect: Boolean = true
):Serializable
