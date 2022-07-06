package com.sd.lib.mutator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class FScopeMutator(scope: CoroutineScope) : FMutator() {
    private val _scope = scope
    private val _jobHolder: MutableMap<Job, String> = WeakHashMap()

    /**
     * 启动协程调用[mutate]
     */
    fun launchMutate(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        priority: Int = 0,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        return launch(
            context = context,
            start = start,
        ) {
            mutate(priority) { block() }
        }
    }

    /**
     * 启动协程
     */
    fun launchWithLock(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        return launch(
            context = context,
            start = start,
        ) {
            withLock { block() }
        }
    }

    /**
     * 启动协程
     */
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
            synchronized(this@FScopeMutator) {
                _jobHolder[it] = ""
            }
        }
    }

    /**
     * 取消协程
     */
    fun cancelLaunch() {
        synchronized(this@FScopeMutator) {
            _jobHolder.keys.forEach { it.cancel() }
            _jobHolder.clear()
        }
    }
}