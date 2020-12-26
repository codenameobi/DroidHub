package com.e.droidhub.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.e.droidhub.R
import com.e.droidhub.databinding.FragmentFilesUploadBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class FilesUploadFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentFilesUploadBinding? = null
    private val binding get() = _binding

    var filePath: Uri? =null
    lateinit var storageReference: StorageReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    companion object {
        private val PICK_IMAGE_CODE = 1000
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_files_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnFileChooser?.setOnClickListener{
            //button setOnClickListener to pickup images
            fileChooser()
        }

        binding?.btnUpload?.setOnClickListener {
            uploadFile()
        }
    }

    private fun fileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"),PICK_IMAGE_CODE)
    }

    private fun uploadFile() {
        val fileName = binding!!.fileName.text.toString()
        val fileRef = storageReference?.child("images/" + fileName)
        val uploadTask = filePath?.let { fileRef?.putFile(it) }

        // Register observers to listen for when the download is done or if it fails
        uploadTask!!.addOnFailureListener {
            Toast.makeText(context, "Task Failed" + it, Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            val result: Task<Uri> = taskSnapshot.storage.downloadUrl
            result.addOnSuccessListener { uri ->
                val downloadUri = uri.toString()

                addItemtoFireStore(fileName,downloadUri)
            }
            Toast.makeText(context, "Task Succeeded" , Toast.LENGTH_SHORT).show()
        }
    }

    private fun addItemtoFireStore(fileName: String, downloadUri: String){

    }
}