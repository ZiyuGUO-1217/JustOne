package com.example.justone.data.ble

import kotlinx.serialization.Serializable

@Serializable
data class AdvertiseGameData(val playerId: String) {
}