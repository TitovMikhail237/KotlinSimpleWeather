package com.example.kotlinsimpleweather

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.kotlinsimpleweather.api.Clouds
import com.example.kotlinsimpleweather.api.ModelWeather
import com.example.kotlinsimpleweather.api.RetrofitBuilder.weatherApi
import com.example.kotlinsimpleweather.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), ToastUiNotification {

    private lateinit var textViewTitle: TextView
    private lateinit var editTextEnter: EditText
    private lateinit var textViewHumidity: TextView
    private lateinit var textViewPressure: TextView
    private lateinit var textViewTemperature: TextView
    private lateinit var textViewWindSpeed: TextView
    private lateinit var textViewClouds: TextView
    private lateinit var requestButton: Button
    private lateinit var binding: ActivityMainBinding
    private lateinit var url: String
    private lateinit var apiKey: String
    private lateinit var modelWeather: ModelWeather


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        url = "https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}"
        apiKey = "6344ad6b9ec027163e9d495e5e2ad8be"

        binding.requestButton.setOnClickListener {
            request()
            hideKeyboard(this)
        }
    }

    private fun request() {

        val uiNotification: ToastUiNotification = this

        val call: Call<ModelWeather> =
            weatherApi.loadWeather(binding.editTextEnter.text.toString(), apiKey)


        call.enqueue(object : Callback<ModelWeather> {

            override fun onResponse(
                call: Call<ModelWeather>?,
                response: Response<ModelWeather>?
            ) {

                val weatherOfCity: ModelWeather? = response?.body()
                if (weatherOfCity != null) {

                    modelWeather = weatherOfCity
                    binding.textViewHumidity.text = modelWeather.main.humidity.toString()
                    binding.textViewPressure.text = modelWeather.main.pressure.toString()
                    binding.textViewTemperature.text = modelWeather.main.temp.toString()
                    binding.textViewWindSpeed.text = modelWeather.wind.toString()
                    binding.textViewClouds.text = modelWeather.clouds.toString()
                } else {
                    uiNotification.showToast("not correct city name")
                }
            }


            override fun onFailure(call: Call<ModelWeather>, t: Throwable) {
                uiNotification.showToast("not connection to net")
            }
        })
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus ?: View(activity)
        val inputMethod =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethod.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.SHOW_IMPLICIT
        )
    }
}

interface ToastUiNotification {
    fun showToast(text: String)
}