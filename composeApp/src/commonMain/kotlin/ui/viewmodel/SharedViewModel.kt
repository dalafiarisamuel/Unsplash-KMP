package ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.ui.repository.PreferenceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SharedViewModel(val preferenceRepository: PreferenceRepository) : ViewModel() {

    val isDarkMode =
        preferenceRepository.isDarkMode.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            false,
        )

    fun toggleTheme(enabled: Boolean) {
        viewModelScope.launch { preferenceRepository.setDarkMode(enabled) }
    }
}
