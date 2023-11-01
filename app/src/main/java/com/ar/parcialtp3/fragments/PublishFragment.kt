package com.ar.parcialtp3.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.ar.parcialtp3.R

class PublishFragment : Fragment() {

    lateinit var v: View

    //Edit Text
    lateinit var edtAge: EditText
    lateinit var edtName: EditText
    lateinit var edtDescription: EditText
    lateinit var edtObservation: EditText
    lateinit var edtWeight: EditText
    lateinit var edtPhotos: EditText
    lateinit var radioButtonMale: RadioButton
    lateinit var radioButtonFemale: RadioButton
    lateinit var btnPubish: Button
    lateinit var btnAddPhoto: Button
    lateinit var photosList: MutableList<String>

    //Text View
    lateinit var txtPhotoErr: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_publish, container, false)
        //Edit Text initialization
        edtAge = v.findViewById(R.id.edtAge)
        edtName = v.findViewById(R.id.edtName)
        edtDescription = v.findViewById(R.id.edtDescription)
        edtObservation = v.findViewById(R.id.edtObservation)
        edtWeight = v.findViewById(R.id.edtWeight)
        edtPhotos = v.findViewById(R.id.edtPhotos)
        radioButtonMale = v.findViewById(R.id.radioButtonMale)
        radioButtonFemale = v.findViewById(R.id.radioButtonFemale)
        btnPubish = v.findViewById(R.id.btnPublish)
        btnAddPhoto = v.findViewById(R.id.btnAddPhoto)

        //TextView initialization
        return v
    }

    override fun onStart() {
        super.onStart()
        photosList = mutableListOf()
        btnAddPhoto.setOnClickListener{
            if(photosList.size < 5){
                photosList.add(edtPhotos.text.toString())
            }else{
                btnAddPhoto.isEnabled=false
                btnAddPhoto.isClickable=false
                Toast.makeText(
                    context,
                    "Has excedido el límite de selección de imágenes",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}