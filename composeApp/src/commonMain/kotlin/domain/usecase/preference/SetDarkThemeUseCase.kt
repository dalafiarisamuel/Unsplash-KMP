package domain.usecase.preference

import data.ui.repository.PreferenceRepository

class SetDarkThemeUseCase (private val preferenceRepository: PreferenceRepository) {
    suspend operator fun invoke(enabled: Boolean) = preferenceRepository.setDarkMode(enabled)
}