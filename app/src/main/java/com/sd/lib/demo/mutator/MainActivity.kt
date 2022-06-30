package com.sd.lib.demo.mutator

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sd.lib.demo.mutator.databinding.ActivityMainBinding
import com.sd.lib.mutator.FMutator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    private val _mutator = FMutator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_1 -> {
                lifecycleScope.launch {
                    try {
                        mutate(0, "btn_1")
                    } catch (e: Exception) {
                        Log.e(TAG, "btn_1 mutate Exception:$e")
                        e.printStackTrace()
                        throw e
                    }
                }
            }
            R.id.btn_2 -> {
                lifecycleScope.launch {
                    mutate(1, "btn_2")
                }
            }
            R.id.btn_cancel -> {
                _mutator.cancel()
            }
        }
    }

    private suspend fun mutate(priority: Int, tag: String) {
        _mutator.mutate(priority) {
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
    }

    companion object {
        const val TAG = "MainActivity"
    }
}