package domain.usecase.preference

import data.ui.repository.PreferenceRepository

class ClearSavedSearchQueryUseCase(private val preferenceRepository: PreferenceRepository) {
    suspend operator fun invoke() = preferenceRepository.clearSavedSearchQuery()
}
