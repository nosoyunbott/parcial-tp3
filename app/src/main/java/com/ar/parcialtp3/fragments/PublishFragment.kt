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
import com.ar.parcialtp3.R

class PublishFragment : Fragment() {

    lateinit var v: View
    lateinit var edtAge: EditText
    lateinit var edtName: EditText
    lateinit var edtDescription: EditText
    lateinit var edtObservation: EditText
    lateinit var edtWeight: EditText
    lateinit var edtPhotos: EditText
    lateinit var radioButtonMale: RadioButton
    lateinit var radioButtonFemale: RadioButton
    lateinit var btnPubish: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_publish, container, false)
        edtAge = v.findViewById(R.id.edtAge)
        edtName = v.findViewById(R.id.edtName)
        edtDescription = v.findViewById(R.id.edtDescription)
        edtObservation = v.findViewById(R.id.edtObservation)
        edtWeight = v.findViewById(R.id.edtWeight)
        edtPhotos = v.findViewById(R.id.edtPhotos)
        radioButtonMale = v.findViewById(R.id.radioButtonMale)
        radioButtonFemale = v.findViewById(R.id.radioButtonFemale)
        btnPubish = v.findViewById(R.id.btnPublish)
        return v
    }

    override fun onStart() {
        super.onStart()
        btnPubish.setOnClickListener{
            val inputText = edtPhotos.text.toString()
            val listOfStrings = inputText.split(",").map { it.trim() }

            if (listOfStrings.size <= 5) {
                Log.d("photos", listOfStrings[2])
            } else {

            }

        }

    }
}