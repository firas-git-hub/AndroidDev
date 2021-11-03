package Model

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

object GalleryServices {
    fun checkPermission(activity: AppCompatActivity): Boolean{
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermision(activity: AppCompatActivity, requestCode: Int){
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            requestCode
        )
    }

    fun loadGallery(activity: AppCompatActivity, requestCode: Int){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null)
    }
}