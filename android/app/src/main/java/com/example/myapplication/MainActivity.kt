package com.example.myapplication

import android.Manifest
import android.R
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.MainActivity.Prompt.*
import com.example.myapplication.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private val CALENDAR_PERMISSION_REQUEST_CODE = 1001 // Unique code for calendar permission
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerViewAdapter: GuardianDataAdapter
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var dataList:MutableList<GuardianData>
    private lateinit var response: Response<ApiResponse>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        dataList = mutableListOf(
            GuardianData("Hi I am your AI assistant and my name is Guardian Angel.\nFeel free to schedule a task, send an email, play music or summarize text.", DataType.RESPONSE),
            GuardianData("All you need to do is use the chat or mic button below and ask your desire.", DataType.RESPONSE),
            )

        recyclerViewAdapter = GuardianDataAdapter(dataList)
        binding.myRecyclerView.apply {
            adapter = recyclerViewAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        binding.nextArrow.setOnClickListener {
                if (binding.bottomTextView.text.isNotEmpty()) {
//            add the text view data to arraylist and update the adapter
                val newTextData = GuardianData(binding.bottomTextView.text.toString(), DataType.REQUEST)
                sendRequest(newTextData)
        }
            else Toast.makeText(this@MainActivity,"Text field is empty",Toast.LENGTH_SHORT).show()
        }


        binding.micImageView.setOnClickListener {
            startSpeechToText()
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
                Log.d("asas","asdds")
            }

            override fun onBeginningOfSpeech() {
                Log.d("asas","asdds")
            }

            override fun onRmsChanged(p0: Float) {
                Log.d("asas","asdds")
            }

            override fun onBufferReceived(p0: ByteArray?) {
                Log.d("asas","asdds")
            }

            override fun onEndOfSpeech() {
                Log.d("asas","asdds")
            }

            override fun onError(p0: Int) {
                Log.d("asas","asdds")
            }
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val recognizedText = matches[0] // Assuming the first result is the most accurate
                    // Do something with the recognized text, e.g., add it to your data list
                    val newTextData = GuardianData(recognizedText, DataType.REQUEST)
                    sendRequest(newTextData)
                }
            }

            override fun onPartialResults(p0: Bundle?) {
                TODO("Not yet implemented")
            }

            override fun onEvent(p0: Int, p1: Bundle?) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun startSpeechToText() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_REQUEST_CODE
            )
        } else {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something!")
            showRecording()
            speechRecognizer.startListening(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }

    companion object {
        private const val RECORD_AUDIO_REQUEST_CODE = 101
    }
    fun showLoader(){
        val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        binding.bottomView.gone()
        binding.loader.visible()
    }

    fun showRecording(){
        binding.bottomView.gone()
        binding.recording.visible()
    }
    fun hideRecording(){
        binding.recording.gone()
        binding.bottomView.visible()
    }

    fun hideLoader(){
        binding.loader.gone()
        binding.bottomView.visible()
    }

    fun sendRequest(promptData:GuardianData){
        //Toast.makeText(this,processTextData(newTextData.text),Toast.LENGTH_SHORT).show()
        dataList.add(promptData)
        recyclerViewAdapter.notifyDataSetChanged()
        hideRecording()
        binding.myRecyclerView.smoothScrollToPosition(dataList.size - 1)
        binding.bottomTextView.text.clear()
        showLoader()
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Set log level as needed
        }

// Create OkHttpClient and add the logging interceptor
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(30000, TimeUnit.SECONDS) // Adjust timeout values as needed
            .readTimeout(30000, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        val service = retrofit.create(ApiService::class.java)
        val call = service.sendPromptRequest(PromptRequest(promptData.text))

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, res: Response<ApiResponse>) {
                if (res.isSuccessful) {
                    response = res

                    if (response.body()?.text!=null){
                        handleResponse(TEXT)
                    }
                    else if (response.body()?.email!=null){
                        handleResponse(EMAIL)
                    }
                    else if (response.body()?.task!=null){
                        handleCalendarEvents()
                    }
                    else if (response.body()?.song!=null){
                        handleSpotify()
                    }

                } else {
                    Toast.makeText(this@MainActivity,"The API call Failed",Toast.LENGTH_SHORT).show()
                }
                hideLoader()
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                // Handle failure (e.g., network issues)
                hideLoader()
            }
        })
    }

    private fun handleSpotify() {
        val songUri = response.body()!!.song+":play"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(songUri))
//        intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://" + packageName))
//        intent.putExtra("play", true)

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Handle if the Spotify app is not installed
            // Open the URL in a browser or display a message to the user
            e.printStackTrace()
        }
        handleResponse(SONG)

    }

    private fun handleResponse(type:Prompt) {
        val responseText = when (type) {
            TEXT -> "Your summarized text is as follows : " + response.body()?.text
            EMAIL -> "Email has been sent successfully!!"
            TASK -> "Tasks have been scheduled successfully!!"
            SONG -> "The song has been opened in spotify application"
        }
        val newResponseData = GuardianData(responseText, DataType.RESPONSE)
        dataList.add(newResponseData)
        recyclerViewAdapter.notifyDataSetChanged()
        binding.myRecyclerView.smoothScrollToPosition(dataList.size - 1)
    }


    private fun handleCalendarEvents() {
        val calendarPermission = Manifest.permission.WRITE_CALENDAR
        val permissionCheck = ContextCompat.checkSelfPermission(this, calendarPermission)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this, arrayOf(calendarPermission), CALENDAR_PERMISSION_REQUEST_CODE)
        } else {
            // Permission already granted, proceed with handling calendar events
            handleCalendarAfterPermissionGranted()
        }
    }
    private fun handleCalendarAfterPermissionGranted() {
        response.body()?.task?.events?.let {
            CalendarManager(this@MainActivity).scheduleEvents(it)
        }
        handleResponse(TASK)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CALENDAR_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handleCalendarAfterPermissionGranted()
                } else {
                    // Permission denied, handle accordingly (e.g., inform user or disable functionality)
                }
            }
        }
    }
    enum class Prompt {
        TEXT,EMAIL,TASK,SONG
    }

}