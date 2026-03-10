package ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.ui.model.Theme
import domain.usecase.preference.GetThemeUseCase
import domain.usecase.preference.SetThemeUseCase
import kotlinx.coroutines.launch

class PreferenceViewModel(
    getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase,
) : ViewModel() {

    val theme = getThemeUseCase().stateInUi(Theme.SYSTEM)

    fun setTheme(theme: Theme) {
        viewModelScope.launch { setThemeUseCase(theme) }
    }
}
