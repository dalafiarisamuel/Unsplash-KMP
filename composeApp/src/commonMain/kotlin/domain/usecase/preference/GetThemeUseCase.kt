package domain.usecase.preference

import data.ui.repository.PreferenceRepository

class GetThemeUseCase(private val preferenceRepository: PreferenceRepository) {
    operator fun invoke() = preferenceRepository.theme
}
