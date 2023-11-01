package com.ar.parcialtp3.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.ar.parcialtp3.R

class PublishFragment : Fragment() {

    lateinit var v: View
    lateinit var edtAge: EditText
    lateinit var edtName: EditText
    lateinit var edtDescription: EditText
    lateinit var edtObservation: EditText
    lateinit var edtWeight: EditText
    lateinit var edtPhotos: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_publish, container, false)
        edtAge = v.findViewById(R.id.edtAge)
        edtName = v.findViewById(R.id.edtName)
        return v
    }
}