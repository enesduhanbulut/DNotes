package com.duhapp.dnotes.features.base.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

abstract class BaseRepository(private val dispatcher: CoroutineDispatcher) {
    suspend fun <T> runOnIO(block: suspend () -> T) =
        withContext(dispatcher) {
            async { block() }
        }.await()
}