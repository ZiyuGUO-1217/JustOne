package com.example.justone.model

import com.example.justone.data.JustOneRepository
import com.example.justone.data.ble.JustOneAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class JustOneOnlineViewModel @Inject constructor(
    repository: JustOneRepository,
    private val adapter: JustOneAdapter
) : JustOneViewModel(repository) {
    private var onlineSetup: Boolean = false
    private var playerList: MutableList<String> = mutableListOf()
        set(value) {
            _onlinePlayers.tryEmit(value)
        }
    private val _onlinePlayers = MutableStateFlow(playerList)
    val onlinePlayers = _onlinePlayers.asStateFlow()

    override fun dispatch(action: JustOneAction) {
        if (action is JustOneAction.OnlineAction) {
            dispatchOnlineAction(action)
        } else {
            super.dispatch(action)
        }
    }

    private fun dispatchOnlineAction(action: JustOneAction.OnlineAction) {
        when (action) {
            is JustOneAction.OnlineAction.SetupOnlineGame -> setupOnlineGame(action.roomId, action.playerId)
        }
    }

    private fun setupOnlineGame(roomId: String, playerId: String) {
        if (playerId.isNotEmpty()) {
            adapter.setupGame(roomId, playerId)
            onlineSetup = true
        }
    }
}
