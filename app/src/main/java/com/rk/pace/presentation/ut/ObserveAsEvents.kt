package com.rk.pace.presentation.ut

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    latest: Boolean = false,
    key2: Any? = null,
    key3: Any? = null,
    onEvent: suspend (T) -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(
        key1 = lifecycleOwner,
        key2 = key2,
        key3 = key3
    ) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                if (latest){
                    flow.collectLatest(
                        onEvent
                    )
                } else {
                    flow.collect(
                        onEvent
                    )
                }
            }
        }
    }

}