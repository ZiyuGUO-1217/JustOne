package com.example.justone.translator.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WordTranslateRequest(
    @SerializedName("source") val word: String,
    @SerializedName("trans_type") val translationType: String,
    @SerializedName("request_id") val requestId: String = "demo",
    @SerializedName("detect") val shouldDetect: Boolean = true
):Serializable
