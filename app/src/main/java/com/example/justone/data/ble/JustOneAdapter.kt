package com.example.justone.data.ble

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*
import javax.inject.Inject

class JustOneAdapter @Inject constructor(private val bleManager: BLEManager) {
    private var roomUuid = BLE_ADV_UUID
    private var gameData = AdvertiseGameData(UUID.randomUUID().toString())

    fun setRoomId(roomId: String) {
        roomUuid = roomUuid.replaceRange(roomUuid.length - roomId.length, roomUuid.length, roomId)
    }

    fun updateData() {
        val serializedGameData = Json.encodeToString(AdvertiseGameData.serializer(), gameData)
        bleManager.startAdvertising(roomUuid, serializedGameData)
        bleManager.startScan(roomUuid, this::collectGameData)
    }

    fun collectGameData(results: MutableList<String>) {
        results.forEach {
            Json.decodeFromString<AdvertiseGameData>(it)
        }
    }

    companion object {
        const val BLE_ADV_UUID = "0c30cc77-145b-49a8-9e43-15a80bfd4ddf"
    }
}