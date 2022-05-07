package com.example.justone.data.ble

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.Integer.max
import javax.inject.Inject

class JustOneAdapter @Inject constructor(private val bleManager: BLEManager) {
    private lateinit var roomUuid: String
    private lateinit var localGameData: AdvertiseGameData
    private val playerMap: MutableMap<String, String?> = mutableMapOf()

    // 输入房间号和玩家姓名，开始游戏
    fun setupGame(roomId: String, playerId: String) {
        roomUuid = BLE_ADV_UUID.replaceRange(BLE_ADV_UUID.length - roomId.length, BLE_ADV_UUID.length, roomId)
        localGameData = AdvertiseGameData(playerId)
    }

    // 猜词玩家向其它玩家提供关键词
    fun announceKeyWord(keyWord: String) {
        localGameData.keyWord = keyWord
        localGameData.gameState = GameState.KEY
    }

    // 其它玩家向猜词玩家提供线索词
    fun raiseClueWord(clueWord: String) {
        localGameData.clueWord = clueWord
        localGameData.gameState = GameState.CLUE
    }

    // 猜词玩家向其它玩家公布猜词结果
    fun announceResult(result: Boolean) {
        localGameData.guessResult = result
    }

    // 进入下一轮
    fun startNextRound() {
        // 重置数据
        playerMap.replaceAll { _, _ -> null }
        localGameData.guessResult = null
        localGameData.keyWord = null
        localGameData.clueWord = null
        localGameData.gameState = GameState.READY
    }

    fun updateData() {
        val serializedGameData = Json.encodeToString(AdvertiseGameData.serializer(), localGameData)
        bleManager.startAdvertising(roomUuid, serializedGameData)
        bleManager.startScan(roomUuid, this::collectGameData)
    }

    fun iAmGuessPlayer(): Boolean {
        return localGameData.guessPlayer == localGameData.playerId
    }

    private fun collectGameData(results: MutableList<String>) {
        val gameDataList = results.map { Json.decodeFromString<AdvertiseGameData>(it) }
        // 已知玩家数量
        var knownPlayerCount = max(gameDataList.size + 1, localGameData.playerCount)
        // 更新玩家列表与总玩家数量
        gameDataList.forEach {
            val player = it.playerId
            if (playerMap.contains(player).not()) {
                playerMap[player] = null
                // 总玩家数量加上本地玩家
                localGameData.playerCount = playerMap.size + 1
            }
            // 如果该数据扫描到了更多的玩家，更新已知玩家数量
            if (it.playerCount > knownPlayerCount) {
                knownPlayerCount = it.playerCount
            }
        }

        when(localGameData.gameState) {
            // 等待其它玩家阶段
            GameState.PREPARE -> {

            }
            // 初始化游戏，决定下一个猜词的人
            GameState.READY -> {
                // 取出所有玩家ID并排序
                val sortedPlayerList = playerMap.keys.sorted()
                when {
                    // 如果你没有获得完整的玩家列表，从其它玩家中决定当前猜词的人
                    localGameData.playerCount < knownPlayerCount -> {
                        localGameData.guessPlayer = gameDataList.first {
                            it.playerCount == knownPlayerCount && it.guessPlayer.isNullOrEmpty().not()
                        }.guessPlayer
                    }
                    // 如果你有完整的玩家列表，决定下一个猜词的人
                    localGameData.guessPlayer != null -> {
                        val guessPlayerIndex = sortedPlayerList.indexOf(localGameData.guessPlayer)
                        localGameData.guessPlayer = sortedPlayerList[guessPlayerIndex + 1]
                    }
                    // 如果是第一次猜词，取列表头的玩家
                    else -> {
                        localGameData.guessPlayer = sortedPlayerList[0]
                    }
                }
                localGameData.gameState = GameState.KEY
            }
            GameState.KEY -> {
                // 你不是猜词的人，你获得关键词以后开始写线索
                if (iAmGuessPlayer().not()) {
                    localGameData.keyWord = gameDataList.first {
                        // 从猜词玩家处获得关键词或者从已经取得关键词的其它玩家处获得关键词
                        localGameData.guessPlayer == it.playerId || (it.gameState == GameState.CLUE && it.keyWord.isNullOrEmpty().not())
                    }.keyWord
                    localGameData.gameState = GameState.CLUE
                }
            }
            GameState.CLUE -> {
                // 你是猜词的人，你要获得线索词
                if (iAmGuessPlayer()) {
                    gameDataList.forEach {
                        playerMap[it.playerId] = it.clueWord
                    }
                    // 判断是否获得除猜词玩家以外所有玩家的线索词
                    if (playerMap.filter { it.value.isNullOrEmpty().not() }.size == knownPlayerCount - 1) {
                        localGameData.gameState = GameState.GUESS
                    } else {
                        // 如果没有，重新扫描结果
                    }
                }
            }
            GameState.GUESS -> {
                // 你不是猜词的人，需要从猜词的人那里获得结果
                if (iAmGuessPlayer().not()) {
                    localGameData.guessResult = gameDataList.first {
                        // 从猜词玩家处获得结果或者从已经取得结果的其它玩家处获得结果
                        localGameData.guessPlayer == it.playerId || (it.gameState == GameState.PREPARE && it.guessResult != null)
                    }.guessResult
                    localGameData.gameState = GameState.PREPARE
                }
            }
        }
    }

    companion object {
        const val BLE_ADV_UUID = "0c30cc77-145b-49a8-9e43-15a80bfd4ddf"
    }
}