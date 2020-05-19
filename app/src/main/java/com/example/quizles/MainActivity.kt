package com.example.quizles

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.PointF
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.set

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var cameraButton: Button?=null
    private var openPhotoGallery:Button?= null
    private var imgTaken: ImageView?=null

    val CAMERA_REQUEST_CODE= 1000
    val PHOTO_REQUEST_CODE=2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)



        var cameraButton = findViewById<Button>(R.id.btnOpenCamera)
        openPhotoGallery= findViewById<Button>(R.id.btnOpenPhotoGallery)
        imgTaken=findViewById<ImageView>(R.id.imgTaken)


        cameraButton?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this,"cameraButton was clicked ", Toast.LENGTH_SHORT).show()

            val cameraIntent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        })

        openPhotoGallery?.setOnClickListener(View.OnClickListener {
            val photoIntent= Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(photoIntent, PHOTO_REQUEST_CODE)
        })
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==CAMERA_REQUEST_CODE){

            if(resultCode== Activity.RESULT_OK){

                var imageData= data?.getExtras()?.get("data") as Bitmap
                imgTaken?.setImageBitmap(imageData)
            }
        }

        if (resultCode==PHOTO_REQUEST_CODE){

            if(resultCode==Activity.RESULT_OK){

                val selectedPhotoUri = data?.data
                try {
                    selectedPhotoUri?.let {
                        if(Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                selectedPhotoUri
                            )
                            imgTaken?.setImageBitmap(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            imgTaken?.setImageBitmap(bitmap)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun button1isClicked(buttonView: View){
        Toast.makeText(this,"Button 1 us Clicked ", Toast.LENGTH_SHORT).show()
    }

    fun button2isClicked(buttonView: View){
        Toast.makeText(this,"Button 2 us Clicked ", Toast.LENGTH_SHORT).show()
    }

    fun button3isClicked(buttonView: View){
        Toast.makeText(this,"Button 3 us Clicked ", Toast.LENGTH_SHORT).show()
    }

    fun button4isClicked(buttonView: View){
        Toast.makeText(this,"Button 4 us Clicked ", Toast.LENGTH_SHORT).show()
    }

    inner class DownloadingPlantTask: AsyncTask<String, Int, List<Plant>>(){
        //can access the background thread and not the UI
        override fun doInBackground(vararg params: String?): List<Plant>? {

            return null
        }

        override fun onPostExecute(result: List<Plant>?) {
            super.onPostExecute(result)
            //can access the UI and not the background thread
        }
    }
}