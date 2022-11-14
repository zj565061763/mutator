package com.sd.demo.mutator

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.mutator.databinding.ActivityMainBinding
import com.sd.lib.mutator.FScopeMutator
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val _mutator by lazy { FScopeMutator(MainScope()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_mutate_1 -> {
                _mutator.launchMutate {
                    start("mutate_1")
                }
            }
            R.id.btn_mutate_2 -> {
                _mutator.launchMutate(priority = 1) {
                    start("mutate_2")
                }
            }
            R.id.btn_cancel_mutate -> {
                _mutator.cancelMutator()
            }

            R.id.btn_launch -> {
                _mutator.launch {
                    start("launch")
                }
            }
            R.id.btn_cancel_launch -> {
                _mutator.cancelLaunch()
            }
        }
    }

    private suspend fun start(tag: String) {
        val uuid = UUID.randomUUID().toString()
        logMsg { "$tag delay before $uuid" }

        try {
            delay(5000)
        } catch (e: Exception) {
            logMsg { "$tag delay Exception:$e $uuid" }
            e.printStackTrace()
            throw e
        }

        logMsg { "$tag delay after $uuid" }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mutator.cancelMutator()
    }
}

fun logMsg(block: () -> String) {
    Log.i("FMutator-demo", block())
}