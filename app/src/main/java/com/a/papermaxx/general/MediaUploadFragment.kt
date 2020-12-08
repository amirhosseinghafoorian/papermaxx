package com.a.papermaxx.general

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.a.papermaxx.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_media_upload.*
import java.io.IOException


class MediaUploadFragment : Fragment() {

    private lateinit var storageReference: StorageReference
    private lateinit var databaseReference: DatabaseReference
    private var imageRequestCode = 7
    private lateinit var filePathUri: Uri
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_media_upload, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storageReference = FirebaseStorage.getInstance().getReference("Images")
        databaseReference = FirebaseDatabase.getInstance().getReference("Images")
        progressDialog = ProgressDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnbrowse.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Image"),
                imageRequestCode
            )
        }

        btnupload.setOnClickListener {
            uploadImage()
        }

    }

    private fun uploadImage() {
        if (filePathUri != null) {
            progressDialog.setTitle("Image is Uploading...")
            progressDialog.show()
            val storageReference2: StorageReference = storageReference.child(
                System.currentTimeMillis().toString() + "." + getFileExtension(filePathUri)
            )
            storageReference2.putFile(filePathUri)
                .addOnSuccessListener { taskSnapshot ->
                    val tempImageName = txtdata.text.toString().trim()
                    progressDialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Image Uploaded Successfully ",
                        Toast.LENGTH_LONG
                    ).show()
                    val imageUploadInfo =
                        UploadInfo2(tempImageName, taskSnapshot.uploadSessionUri.toString())
                    val imageUploadId: String = databaseReference.push().key.toString()
                    databaseReference.child(imageUploadId).setValue(imageUploadInfo)
                }
        } else {
            Toast.makeText(
                requireContext(),
                "Please Select Image or Add Image Name",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == imageRequestCode && resultCode == RESULT_OK && data != null && data.data != null) {
            filePathUri = data.data!!
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, filePathUri)
                image_view.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(requireContext().contentResolver.getType(uri))
    }

    data class UploadInfo2(
        var name: String?,
        var url: String?
    )

}