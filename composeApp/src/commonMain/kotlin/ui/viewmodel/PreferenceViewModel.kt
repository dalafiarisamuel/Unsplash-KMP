package ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.usecase.preference.GetDarkThemeUseCase
import domain.usecase.preference.SetDarkThemeUseCase
import kotlinx.coroutines.launch

class PreferenceViewModel(
    getDarkThemeUseCase: GetDarkThemeUseCase,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
) : ViewModel() {

    val isDarkMode = getDarkThemeUseCase().stateInUi(false)

    fun toggleTheme(enabled: Boolean) {
        viewModelScope.launch { setDarkThemeUseCase(enabled) }
    }
}
