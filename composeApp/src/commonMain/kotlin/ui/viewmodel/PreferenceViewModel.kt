package ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.usecase.preference.GetDarkThemeUseCase
import domain.usecase.preference.SetDarkThemeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PreferenceViewModel(
    getDarkThemeUseCase: GetDarkThemeUseCase,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
) : ViewModel() {

    val isDarkMode =
        getDarkThemeUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), false)

    fun toggleTheme(enabled: Boolean) {
        viewModelScope.launch { setDarkThemeUseCase(enabled) }
    }
}
