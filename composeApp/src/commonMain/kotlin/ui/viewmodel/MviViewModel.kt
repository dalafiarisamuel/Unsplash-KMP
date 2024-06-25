package ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

abstract class MviViewModel<Event, State>(defaultState: State) : ViewModel() {

    protected val events = MutableSharedFlow<Event>()

    var state by mutableStateOf(defaultState)
        protected set

    fun dispatch(event: Event) {
        viewModelScope.launch { events.emit(event) }
    }

    protected inline fun <reified SpecificEvent : Event> on(
        crossinline handle: suspend (event: SpecificEvent) -> Unit,
    ) {
        events
            .filterIsInstance<SpecificEvent>()
            .map { runCatching { handle(it) } }
            .map { it.exceptionOrNull() }
            .filterNotNull()
            .onEach(this::onError)
            .launchIn(viewModelScope)
    }

    protected open fun onError(throwable: Throwable) = Unit
}
