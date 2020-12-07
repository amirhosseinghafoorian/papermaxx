package com.a.papermaxx.general

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.a.papermaxx.R
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_media_upload.*
import java.io.IOException


class MediaUploadFragment2 : Fragment() {

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

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == imageRequestCode && resultCode == RESULT_OK && data != null && data.data != null) {
            filePathUri = data.data!!
            try {
                Glide.with(requireContext()).load(filePathUri).into(image_view)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}