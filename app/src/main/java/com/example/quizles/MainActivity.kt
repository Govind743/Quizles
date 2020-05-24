package com.example.quizles

/**
 * Created by Govind Yadav on 17-05-2020.
 */


import Model.DownloadingObject
import Model.ParsePlantUtility
import Model.Plant
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import kotlinx.coroutines.*
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.GlobalScope
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var cameraButton: Button? = null
    private var openPhotoGallery: Button? = null
    private var imgTaken: ImageView? = null

    //Instance variables
    var correctAnswerIndex: Int= 0
    var correctPlant: Plant? = null
    var AnsweredCorrectly : Int= 0
    var AnsweredIncorrectly: Int= 0

    val CAMERA_REQUEST_CODE = 1000
    val PHOTO_REQUEST_CODE = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)





        var cameraButton = findViewById<Button>(R.id.btnOpenCamera)
        openPhotoGallery = findViewById<Button>(R.id.btnOpenPhotoGallery)
        imgTaken = findViewById<ImageView>(R.id.imgTaken)


        cameraButton?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "cameraButton was clicked ", Toast.LENGTH_SHORT).show()

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        })

        openPhotoGallery?.setOnClickListener(View.OnClickListener {
            val photoIntent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(photoIntent, PHOTO_REQUEST_CODE)
        })

        btnNextPlant.setOnClickListener(View.OnClickListener {

            try {
                val innerClassObject = DownloadingPlantTask()
                innerClassObject.execute()
            } catch (e: java.lang.Exception){
                e.printStackTrace()
            }

            button1.setBackgroundColor(Color.LTGRAY)
            button2.setBackgroundColor(Color.LTGRAY)
            button3.setBackgroundColor(Color.LTGRAY)
            button4.setBackgroundColor(Color.LTGRAY)

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {

            var imageData = data?.getExtras()?.get("data") as Bitmap
            imgTaken?.setImageBitmap(imageData)
        }

        if (requestCode == PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {

            val selectedPhotoUri = data.data
            try {
                selectedPhotoUri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            this.contentResolver, selectedPhotoUri
                        )
                        imgTaken?.setImageBitmap(bitmap)
                    } else {
                        val source =
                            ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        imgTaken?.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun imgViewClicked(view: View) {
        val randomNumber = (Math.random() * 6).toInt() + 1
        Log.i("TAG", "The random number is: $randomNumber")

        when (randomNumber) {
            1 -> btnOpenCamera.setBackgroundColor(Color.RED)

            2 -> btnOpenCamera.setBackgroundColor(Color.GREEN)

            3 -> btnOpenCamera.setBackgroundColor(Color.BLUE)

            4 -> btnOpenCamera.setBackgroundColor(Color.BLACK)

            5 -> btnOpenCamera.setBackgroundColor(Color.MAGENTA)

            6 -> btnOpenCamera.setBackgroundColor(Color.YELLOW)


        }
    }


    fun button1isClicked(buttonView: View) {
        RightOrWrong(0)
    }

    fun button2isClicked(buttonView: View) {
        RightOrWrong(1)
    }

    fun button3isClicked(buttonView: View) {
        RightOrWrong(2)
    }

    fun button4isClicked(buttonView: View) {
        RightOrWrong(3)
    }


    inner class DownloadingPlantTask : AsyncTask<String, Int, List<Plant>>() {

        override fun doInBackground(vararg params: String?): List<Plant>? {


            // Can access background thread. Not user interface thread

            val parsePlant = ParsePlantUtility()
            
            return parsePlant.parseJSONDatafromLink()
        }



        override fun onPostExecute(result: List<Plant>?) {
            super.onPostExecute(result)
            //Can access the UI thread and not the background thread.

            var numberofPlants = result?.size ?: 0

            //this condition will access the index of plantObject randomly
            if (numberofPlants> 0){

                var randomPlantIndexForButton1 : Int= (Math.random() * result!!.size).toInt()
                var randomPlantIndexForButton2 : Int= (Math.random() * result!!.size).toInt()
                var randomPlantIndexForButton3 : Int= (Math.random() * result!!.size).toInt()
                var randomPlantIndexForButton4 : Int= (Math.random() * result!!.size).toInt()

                //this will get a index and assign that object to the Buttons

                var allrandomPlants = ArrayList<Plant>()

                allrandomPlants.add(result.get(randomPlantIndexForButton1))
                allrandomPlants.add(result.get(randomPlantIndexForButton2))
                allrandomPlants.add(result.get(randomPlantIndexForButton3))
                allrandomPlants.add(result.get(randomPlantIndexForButton4))

                button1.text = result.get(randomPlantIndexForButton1).toString()
                button2.text = result.get(randomPlantIndexForButton2).toString()
                button3.text = result.get(randomPlantIndexForButton3).toString()
                button4.text = result.get(randomPlantIndexForButton4).toString()

                correctAnswerIndex= (Math.random()* allrandomPlants.size).toInt()
                correctPlant= allrandomPlants.get(correctAnswerIndex)

                val downloadingImagetask = DownloadingImagetask()
                downloadingImagetask.execute(allrandomPlants.get(correctAnswerIndex).picture_name)
            }
        }
    }



    //Check for Internet Connection

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> return false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    private fun RightOrWrong(userguess: Int){

        when(correctAnswerIndex){
            0 -> button1.setBackgroundColor(Color.GREEN)
            1 -> button2.setBackgroundColor(Color.GREEN)
            2 -> button3.setBackgroundColor(Color.GREEN)
            3 -> button4.setBackgroundColor(Color.GREEN)
        }

        if (userguess == correctAnswerIndex){
            txtState.setText("Right Answer!!")

            AnsweredCorrectly++
            txtRight.setText("$AnsweredCorrectly")

        }
        else{
            var correctPlantName = correctPlant.toString()
            txtState.setText("Wrong!! Answer is :  $correctPlantName")

            AnsweredIncorrectly++
            txtWrong.setText("$AnsweredIncorrectly")

        }
    }

    //This class creates a thread for Downloading the plant photos
    inner class DownloadingImagetask : AsyncTask<String, Int, Bitmap?>(){

        override fun doInBackground(vararg pictureName: String?): Bitmap? {

            try {
                val downloadObject = DownloadingObject()
                val plantBitmap: Bitmap? = downloadObject.downloadPlantPicture(pictureName[0])

                return plantBitmap
            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)

            imgTaken?.setImageBitmap(result)
        }
    }
}
