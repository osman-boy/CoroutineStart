package ru.sumin.coroutinestart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.sumin.coroutinestart.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonLoad.setOnClickListener {
            lifecycleScope.launch {
                loadData()
            }
//            loadDataWithOutCoroutine()
        }


    }


    private suspend fun loadData() {
        binding.progress.isVisible = true
        binding.buttonLoad.isEnabled = false
        val city = loadCity()
        binding.tvLocation.text = city
        val temp = loadTemperature(city)
        binding.tvTemperature.text = temp.toString()
        binding.progress.isVisible = false
        binding.buttonLoad.isEnabled = true

    }

    private fun loadDataWithOutCoroutine(step: Int = 0, obj: Any? = null) {

        when (step) {

            0 -> {
                binding.progress.isVisible = true
                binding.buttonLoad.isEnabled = false
                loadCityWithOutCoroutine {
                    loadDataWithOutCoroutine(1, it)
                }
            }


            1 -> {

                if (obj != null && obj is String) {
                    binding.tvLocation.text = obj
                    loadTemperatureWithOutCoroutine(obj) {
                        loadDataWithOutCoroutine(2, it)
                    }
                }

            }

            2 -> {
                if (obj != null && obj is Int) {
                    binding.tvTemperature.text = obj.toString()
                    binding.progress.isVisible = false
                    binding.buttonLoad.isEnabled = true
                }

            }
        }
    }

    private fun loadCityWithOutCoroutine(callback: (String) -> Unit) {

        Handler(Looper.getMainLooper()).postDelayed({
            callback("Moscow")
        }, 5000)


    }

    private fun loadTemperatureWithOutCoroutine(city: String, callback: (Int) -> Unit) {

        Toast.makeText(this, getString(R.string.toast, city), Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            callback(17)
        }, 5000)

    }

    private suspend fun loadCity(): String {
        delay(5000)
        return "Moscow"

    }

    private suspend fun loadTemperature(city: String): Int {
        Toast.makeText(this, getString(R.string.toast, city), Toast.LENGTH_SHORT).show()
        delay(5000)
        return 17
    }
}
