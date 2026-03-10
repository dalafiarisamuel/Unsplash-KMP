package data.ui.model

enum class Theme {
    SYSTEM,
    LIGHT,
    DARK;

    companion object {
        fun valueOfOrDefault(value: String?, default: Theme = SYSTEM): Theme {
            return try {
                if (value == null) default else valueOf(value)
            } catch (_: Exception) {
                default
            }
        }
    }
}
