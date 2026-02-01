package domain.usecase.preference

import data.ui.repository.PreferenceRepository

class GetDarkThemeUseCase(private val preferenceRepository: PreferenceRepository) {
    operator fun invoke() = preferenceRepository.isDarkMode
}
