package com.sd.demo.mutator

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.mutator.databinding.ActivityMainBinding
import com.sd.lib.mutator.FScopeMutator
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay

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
                _mutator.cancel()
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
        Log.i(TAG, "$tag delay before")
        try {
            delay(5000)
        } catch (e: Exception) {
            Log.e(TAG, "$tag delay Exception:$e")
            e.printStackTrace()
            throw e
        }
        Log.i(TAG, "$tag delay after")
    }

    override fun onDestroy() {
        super.onDestroy()
        _mutator.cancel()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}