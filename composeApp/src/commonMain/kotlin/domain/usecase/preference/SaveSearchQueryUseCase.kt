package domain.usecase.preference

import data.ui.repository.PreferenceRepository

class SaveSearchQueryUseCase(private val preferenceRepository: PreferenceRepository) {
    companion object {
        const val MAX_QUERY_LENGTH = 20
    }

    suspend operator fun invoke(query: String) =
        preferenceRepository.saveSearchQuery(query.take(MAX_QUERY_LENGTH))
}
