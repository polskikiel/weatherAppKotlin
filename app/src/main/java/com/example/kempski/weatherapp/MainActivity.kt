package com.example.kempski.weatherapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val KELVIN_CONST = 273.15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendRequestButton.setOnClickListener {
            val view: View = if (currentFocus == null) View(this) else currentFocus
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            val city = cityEditText.text.toString()

            if(city.isEmpty()){
                Toast.makeText(this,"Please provide city name",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val url = "http://api.openweathermap.org/data/2.5/weather?q=${city}&APPID=c8ea36039337ef8bc79b3c87744f91a4"

            val rq = Volley.newRequestQueue(this)
            val request = object: JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener<JSONObject> { response ->
                        val mainObject = response.getJSONObject("main")
                        val tmp = mainObject.getString("temp").toDouble() - KELVIN_CONST
                        val pressure = mainObject.getString("pressure")
                        val coordinates = response.getJSONObject("coord")

                        cityTextView.text = city.capitalize()
                        tmpTextView.text = String.format("%.2f", tmp)+"°C"
                        pressureTextView.text = "${pressure}hPa"

                    },
                    Response.ErrorListener {
                        Toast.makeText(this, "That didn't work!", Toast.LENGTH_SHORT).show()
                    }) {

                // override getHeader for pass session to service
                override fun getHeaders(): MutableMap<String, String> {

                    val header = mutableMapOf<String, String>()
                    header.put("Content-Type", "application/json; charset=utf-8")
                    return header
                }
            }

            rq.add(request)
        }

        val url = "http://api.openweathermap.org/data/2.5/weather?q=Gliwice&APPID=c8ea36039337ef8bc79b3c87744f91a4"

        val rq = Volley.newRequestQueue(this)
        val request = object: JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener<JSONObject> { response ->
                    val mainObject = response.getJSONObject("main")
                    val tmp = mainObject.getString("temp").toDouble() - KELVIN_CONST
                    val pressure = mainObject.getString("pressure")
                    val coordinates = response.getJSONObject("coord")

                    cityTextView.text = "Gliwice"
                    tmpTextView.text = String.format("%.2f", tmp)+"°C"
                    pressureTextView.text = "${pressure}hPa"

                },
                Response.ErrorListener {
                    Toast.makeText(this, "That didn't work!", Toast.LENGTH_SHORT).show()
                }) {

            // override getHeader for pass session to service
            override fun getHeaders(): MutableMap<String, String> {

                val header = mutableMapOf<String, String>()
                header.put("Content-Type", "application/json; charset=utf-8")
                return header
            }
        }

        rq.add(request)
    }
}
