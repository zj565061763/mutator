package com.sd.lib.mutator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class FScope(scope: CoroutineScope) {
    private val _scope = scope
    private val _jobHolder: MutableMap<Job, String> = WeakHashMap()

    /**
     * 启动协程
     */
    @Synchronized
    fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        return _scope.launch(
            context = context,
            start = start,
            block = block,
        ).also {
            _jobHolder[it] = ""
        }
    }

    /**
     * 取消协程
     */
    @Synchronized
    fun cancel() {
        _jobHolder.keys.forEach { it.cancel() }
        _jobHolder.clear()
    }
}