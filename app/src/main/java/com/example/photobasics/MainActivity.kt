package com.example.photobasics

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAKE_PIC_CODE: Int = 1

    lateinit var currentPhotoPath: String
    lateinit var fileUri: Uri

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun requireContext(): Context {
        return this
    }

    override fun onPostResume() {
        super.onPostResume()
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->

            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {

                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(Date())

                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let { storageDir ->

                    File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).also {
                        Log.i("MainActivity", "storageDir=" + storageDir.canonicalPath)

                        fileUri = FileProvider.getUriForFile(requireContext(), "com.mypackage.fileprovider", it)
                        Log.i("MainActivity", "fileUri=" + fileUri)
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                        startActivityForResult(takePictureIntent, TAKE_PIC_CODE)
                    }
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == TAKE_PIC_CODE && resultCode == RESULT_OK) {
            Log.i("MainActivity", "requestCode=" + requestCode + " resultCode=" + resultCode)
//        }
    }
}
