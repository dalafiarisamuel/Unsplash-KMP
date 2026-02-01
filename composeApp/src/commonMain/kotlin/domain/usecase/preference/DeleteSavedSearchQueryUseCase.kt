package domain.usecase.preference

import data.ui.repository.PreferenceRepository

class DeleteSavedSearchQueryUseCase(private val preferenceRepository: PreferenceRepository) {
    suspend operator fun invoke(query: String) = preferenceRepository.deleteSavedSearchQuery(query)
}
