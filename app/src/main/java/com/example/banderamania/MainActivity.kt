package com.example.banderamania

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    data class Pais(val nombre: String)

    var countriesList: Array<String> = arrayOf()
    var countryOptions: MutableList<String> = mutableListOf()   //incluyendo la verdadera
    var flagSelected: String = ""
    var currentRightAns: String = ""
    val url_bycode = "https://restcountries.com/v2/alpha/"
    val url_byname = "https://restcountries.com/v2/name/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val botonDeBanderas = findViewById<Button>(R.id.next)
        botonDeBanderas.setOnClickListener{
                e -> prepareNewRound()
        }

        initVars()







    }

    fun initVars() {
        fillList()
        prepareNewRound()
    }

    fun prepareNewRound() {
        emptyLastOptionsUsed()
        getRandomFlag()
        selectWrongOptions(flagSelected)
        fillButtons()
    }

    fun emptyLastOptionsUsed() {
        if(this.countryOptions.isNotEmpty()) {
            this.countryOptions.removeAll(this.countryOptions)
        }
    }

    fun getRandomFlag() {
        flagSelected = countriesList.random()   //esto tal vez tenga que pasarlo a una variable global
        var img = findViewById<ImageView>(R.id.image_view)
        var url = "https://countryflagsapi.com/png/$flagSelected"
        Picasso.with(this).load(url).into(img)
    }

    fun fillList() {
        //poner los nombres comletos y buscar por eso
        countriesList = arrayOf(
            "ae", "ad", "af", "ag","ai","al","am","ao","aq","ar","as","at","au","aq","az","ba","bb","bd","be","bf","bg","bh"
        )
    }

    //capaz ir agregando a una lista las que esten ok o ya haya adivinado
    //capaz estaria bueno agregar a la validacion de que cuando busca las banderas, no esten las que ya adivine o me salieron

    fun selectWrongOptions(correct: String) {
        //Agarro 3 opciones de la lista excluyendo la correcta
        var bitflag = false;
        var count = 0;
        do {
            var option = countriesList.random()
            if(option != correct && !countryOptions.contains(option)) {
                countryOptions.add(option)
                count++;
            }
        } while (count < 3)
        countryOptions.add(correct)
    }

    fun fillButtons() {
        //randomizo el orden y setteo las opciones en los botones
        countryOptions.shuffle()

        //asigno cada uno de los 4 elementos a un id de boton

        //findViewById<Button>(R.id.b1).setText(findFullName(countryOptions.get(0)))
        //findViewById<Button>(R.id.b2).setText(findFullName(countryOptions.get(1)))
        //findViewById<Button>(R.id.b3).setText(findFullName(countryOptions.get(2)))
        //findViewById<Button>(R.id.b4).setText(findFullName(countryOptions.get(3)))
        //estaria bueno ver si hace falta vaciar la lista

        /*findViewById<Button>(R.id.b1).setText(checkFullName(countryOptions.get(0)))
        findViewById<Button>(R.id.b2).setText(checkFullName(countryOptions.get(1)))
        findViewById<Button>(R.id.b3).setText(checkFullName(countryOptions.get(2)))
        findViewById<Button>(R.id.b4).setText(checkFullName(countryOptions.get(3)))*/
        var b0 = findViewById<Button>(R.id.b1)
        var b1 = findViewById<Button>(R.id.b2)
        var b2 = findViewById<Button>(R.id.b3)
        var b3 = findViewById<Button>(R.id.b4)

        setCountryFullName(countryOptions.get(0), b0)
        setCountryFullName(countryOptions.get(1), b1)
        setCountryFullName(countryOptions.get(2), b2)
        setCountryFullName(countryOptions.get(3), b3)
    }

    /*
    fun findFullName(countryCode: String): String {
        //var countryData = "https://restcountries.com/vs/alpha/$countryCode"
        //print(countryData)

        var response = Retrofit.Builder().baseUrl("https://restcountries.com/v2/alpha/$countryCode/").build()
        print(response)
        return response.toString()
    }
    */


    /*fun checkFullName(countryCode: String): String {
        val url = "https://restcountries.com/v2/alpha/"
        //val url = "https://restcountries.com/v2/alpha/$countryCode/"
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create<APIService>(APIService::class.java)

        print("RESP: " + service.getFullName(countryCode))

        var returnedVal: Response<Country>
        var finalName: String = ""
        service.getFullName(countryCode).enqueue(object: Callback<Country> {
            override fun onResponse(call: Call<Country>, response: Response<Country>) {
                returnedVal = response
                finalName = response.body()?.name ?: "notn"
                print("SUCCEESSSSSSSSSSSSSSSSSS: " + response)
            }
            override fun onFailure(call: Call<Country>, t: Throwable) {
                print("FAILUUUUUUUUUUUUUUUUURE: " + t)
            }
        })
        print(finalName)
        return finalName
    }*/

    fun setCountryFullName(countryCode: String, button: Button) {
        //val url = "https://restcountries.com/v2/alpha/$countryCode/"
        val retrofit = Retrofit.Builder()
            .baseUrl(url_bycode)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create<APIService>(APIService::class.java)

        var finalName: String = ""

        service.getFullName(countryCode).enqueue(object: Callback<Country> {
            override fun onResponse(call: Call<Country>, response: Response<Country>) {
                //finalName = response.body()?.name ?: "notn"
                print("SUCCEESSSSSSSSSSSSSSSSSS: " + response)
                button.setText(response.body()?.name ?: "notn")

            }
            override fun onFailure(call: Call<Country>, t: Throwable) {
                print("FAILUUUUUUUUUUUUUUUUURE: " + t)
            }
        })
    }

    fun getFullNameAndCheckAnswer(countryCode: String, button: Button) {
        val retrofit = Retrofit.Builder()
            .baseUrl(url_byname)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create<APIService>(APIService::class.java)

        var finalName: String = ""

        service.getFullName(countryCode).enqueue(object: Callback<Country> {
            override fun onResponse(call: Call<Country>, response: Response<Country>) {

                if(flagSelected.uppercase().equals(response.body()?.alpha2Code ?: "")) {
                    print("suma punto")
                }
                print("SUCCEESSSSSSSSSSSSSSSSSS: " + response)
            }
            override fun onFailure(call: Call<Country>, t: Throwable) {
                print("FAILUUUUUUUUUUUUUUUUURE: " + t)
            }
        })
    }

    //en pausa
    fun requestBuilder(url: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(url_bycode)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


/*
    fun findSpecificValue(response: Response): String {
        //val body = response.body().toString() // This permanently consumes the output, you should store the result

        //var json = JSONObject(body) // toString() is not the response body, it is a debug representation of the response body
        //return json.getString("name").toString()

        val myObj = JSONObject(response.toString()).getString("name")
        return myObj
    }*/

    fun checkAnswer(view: View) {
        //Toast.makeText( AppCompatActivity(),"Hi I am Toast", Toast.LENGTH_LONG).show();
        print(view)

        //llamada a la api /name/fullname ( mtext) para que obtenga el codigo alpha


        //if(alpha obtenido es igual a flagSelected) -> suma 1 punto
        //si no, no suma y se baraja de nuevo

    }

    fun sumPoints() {

    }
}