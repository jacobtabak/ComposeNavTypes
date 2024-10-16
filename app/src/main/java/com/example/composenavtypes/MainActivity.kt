package com.example.composenavtypes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composenavtypes.ui.AppComposables.SeparateNavHost
import com.example.composenavtypes.ui.AppComposables.SeparateNavHostWithoutCustomType
import com.example.composenavtypes.ui.AppRoute
import com.example.composenavtypes.ui.animatedComposable
import com.example.composenavtypes.ui.theme.ComposeNavTypesTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainActivity : ComponentActivity() {
    @Stable
    data class Props(val code: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var props by remember { mutableStateOf(Props("")) }
            LaunchedEffect(Unit) {
                // randomly update the value of `props` every second to trigger recompositions
                randomStringFlow().collect {
                    props = props.copy(code = it)
                }
            }

            ComposeNavTypesTheme {
                Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                    val navController = rememberNavController()

                    /**
                     * This nav host re-navigates to the start destination whenever `props` change.
                     * This should definitely not be happening!
                     */
                    SeparateNavHost(props)

                    /**
                     * This nav host is identical to the one above, except it is defined in-line
                     * and does not misbehave when `props` change
                     */
                    NavHost(navController, startDestination = AppRoute.WithCustomType(null)) {
                        animatedComposable<AppRoute.WithCustomType>(
                            typeMap = mapOf(serializableTypeMapping<ProtectedString?>(nullable = true))
                        ) {
                            Text("Inline NavHost: ${props.code}")
                        }
                    }

                    /**
                     * This nav host starts on a destination that does not have a custom type, but
                     * is otherwise identical to the misbehaving nav host
                     */
                    SeparateNavHostWithoutCustomType(props)
                }
            }
        }
    }
}

fun generateRandomString(length: Int = 10): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")

}

fun randomStringFlow(): Flow<String> = flow {
    while (true) {
        emit(generateRandomString())
        delay(1000)
    }
}