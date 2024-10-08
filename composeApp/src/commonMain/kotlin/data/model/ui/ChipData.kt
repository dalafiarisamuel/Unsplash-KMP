package data.model.ui

import androidx.compose.runtime.Stable

@Stable
internal data class ChipData(val emoji: String, val chipText: String) {
    val displayText get() = "$emoji $chipText"
    fun isChipSelected(selectedText: String?): Boolean =
        selectedText?.equals(this.chipText, ignoreCase = true) ?: false
}