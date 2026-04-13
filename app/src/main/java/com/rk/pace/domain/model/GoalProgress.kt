package com.rk.pace.domain.model

data class GoalProgress(
    val type: GoalType,
    val progress: Float,
    val goal: Float,
    val isSet: Boolean
) {
    val fraction: Float
        get() = if (isSet && goal > 0f) (progress / goal).coerceIn(0f, 1f) else 0f

    val isCompleted: Boolean
        get() = isSet && progress >= goal
}
