package com.example.composenavtypes.ui

import com.example.composenavtypes.ProtectedString
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute {
    @Serializable
    data class WithCustomType(val password: ProtectedString?) : AppRoute

    @Serializable
    data class NoCustomType(val password: String?) : AppRoute
}