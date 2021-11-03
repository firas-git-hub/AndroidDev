package com.example.randomdice

import Model.CameraServices
import Model.GalleryServices
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView


class GalCamActivity : AppCompatActivity() {

    val cameraPermissionRQ = 103
    val galleryPermissionRQ = 104
    private lateinit var cameraOptionButton: Button
    private lateinit var galleryOptionButton: Button
    private lateinit var selectImgButt: Button
    private lateinit var chosenImageView: ImageView
    private var lastPressedButton: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gal_cam)

        cameraOptionButton = findViewById(R.id.cameraOptButt)
        galleryOptionButton = findViewById(R.id.galleryOptButt)
        selectImgButt = findViewById(R.id.selectImgButt)
        chosenImageView = findViewById(R.id.chosenImageView)

        setClickListeners()

    }

    private fun setClickListeners(){
        cameraOptionButton.setOnClickListener{
            if(CameraServices.checkPermission(this)){
                CameraServices.loadCamera(this, cameraPermissionRQ)
                lastPressedButton = "Camera"
            } else {
                CameraServices.requestPermision(this, cameraPermissionRQ)
            }
        }

        galleryOptionButton.setOnClickListener{
            if(GalleryServices.checkPermission(this)){
                GalleryServices.loadGallery(this, galleryPermissionRQ)
                lastPressedButton = "Gallery"
            } else {
                GalleryServices.requestPermision(this, galleryPermissionRQ)
            }
        }

        selectImgButt.setOnClickListener{
            selectedImg.imgDrawable = chosenImageView.drawable
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == cameraPermissionRQ && resultCode == RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            chosenImageView.setImageBitmap(imageBitmap)
        }
        if(requestCode == galleryPermissionRQ && resultCode == RESULT_OK){
            val imageUri = data?.data
            chosenImageView.setImageURI(imageUri)
        }
    }

    companion object selectedImg{
        var imgDrawable: Drawable? = null
    }
}

