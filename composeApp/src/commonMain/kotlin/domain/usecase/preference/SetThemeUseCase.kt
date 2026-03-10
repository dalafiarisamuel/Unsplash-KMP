package domain.usecase.preference

import data.ui.model.Theme
import data.ui.repository.PreferenceRepository

class SetThemeUseCase(private val preferenceRepository: PreferenceRepository) {
    suspend operator fun invoke(theme: Theme) = preferenceRepository.setTheme(theme)
}
