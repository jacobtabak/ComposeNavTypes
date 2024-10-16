package com.example.composenavtypes.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.composenavtypes.MainActivity.Props
import com.example.composenavtypes.ProtectedString
import com.example.composenavtypes.serializableTypeMapping

object AppComposables {
    @Composable
    fun SeparateNavHost(props: Props) {
        val navController = rememberNavController()
        NavHost(navController, startDestination = AppRoute.WithCustomType(null)) {
            animatedComposable<AppRoute.WithCustomType>(
                typeMap = mapOf(serializableTypeMapping<ProtectedString?>(nullable = true))
            ) {
                Text("Separate NavHost (renavigating every sec): ${props.code}")
            }
        }
    }

    @Composable
    fun SeparateNavHostWithoutCustomType(props: Props) {
        val navController = rememberNavController()
        NavHost(navController, startDestination = AppRoute.NoCustomType(null)) {
            animatedComposable<AppRoute.NoCustomType> {
                Text("Separate NavHost, No Custom Type: ${props.code}")
            }
        }
    }
}