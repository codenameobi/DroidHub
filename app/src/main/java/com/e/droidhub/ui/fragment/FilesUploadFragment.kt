package com.e.droidhub.ui.fragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.e.droidhub.databinding.FragmentFilesUploadBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class FilesUploadFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentFilesUploadBinding? = null
    private val binding get() = _binding!!

    var filePath: Uri? =null
    lateinit var storageReference: StorageReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    companion object {
        private val PICK_IMAGE_CODE = 1000
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_CODE && resultCode== RESULT_OK && data!=null){
            filePath = data.data!!
            Log.d("successX", "File Path: " + filePath)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storageReference = Firebase.storage.getReference()
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFilesUploadBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnFileChooser.setOnClickListener{
            //button setOnClickListener to pickup images
            Log.d("log","file chooser button clicked")
            fileChooser()
        }

        binding.btnUpload.setOnClickListener {
            uploadFile()
        }
    }

    private fun fileChooser() {
        Log.d("successX","file chooser button clicked")
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"),PICK_IMAGE_CODE)
    }

    private fun uploadFile() {
        val fileName = binding!!.fileName.text.toString()
        if(filePath != null){

            val fileRef = storageReference?.child("images/" + fileName)
            val uploadTask = fileRef.putFile(filePath!!)

            // Register observers to listen for when the download is done or if it fails
            uploadTask!!.addOnFailureListener {
                Toast.makeText(context, "Task Failed" + it, Toast.LENGTH_SHORT).show()
                Log.d("successX","upload failed")
            }.addOnSuccessListener { taskSnapshot ->
                Log.d("successX","upload success")
                val result: Task<Uri> = taskSnapshot.storage.downloadUrl
                result.addOnSuccessListener { uri ->
                    val downloadUri = uri.toString()

                    addItemtoFireStore(fileName,downloadUri)
                }
                Toast.makeText(context, "Task Succeeded" , Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun addItemtoFireStore(fileName: String, downloadUri: String) {
        Log.d("DroidHub", "Entered Adding Item to Firestore")
        val userId = auth.uid.toString()
        var docs = hashMapOf(
                "downloadURL" to downloadUri,
                "filename" to fileName
        )
        //val collectionPath = "users/"+userId+"/documents"
        Log.d("DroidHub", "docs" + docs)
        try {
            db.collection("users").document(userId)
                    .collection("documents").document()
                    .set(docs, SetOptions.merge())
                    .addOnSuccessListener { documentReference ->
                        Log.d("DroidHub", "DocumentSnapshot added with ID: ${documentReference}")
                    }
                    .addOnFailureListener { e ->
                        Log.d("DroidHub", "Error adding document", e)
                    }
        }
        catch (e: Exception){
            Log.d("DroidHub", "Oops!" + e)
        }
    }
}