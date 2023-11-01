package com.ar.parcialtp3.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.ar.parcialtp3.R

class StartFragment : Fragment() {

    lateinit var v: View

    lateinit var edtName: EditText
    lateinit var edtPhone: EditText
    lateinit var edtImage: EditText
    lateinit var btnLogin: Button

    //Shared
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_start, container, false)
        edtName = v.findViewById(R.id.edtName)
        edtPhone = v.findViewById(R.id.edtPhone)
        edtImage = v.findViewById(R.id.edtImage)
        btnLogin = v.findViewById(R.id.btnLogin)
        sharedPreferences = requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        return v
    }

    override fun onStart() {
        super.onStart()

        val editor = sharedPreferences.edit()

        btnLogin.setOnClickListener {
            if ((validate(edtName.toString()) && isNumeric(edtName.toString())) && (validate(
                    edtPhone.toString()
                ) && isOnlyLetters(edtPhone.toString())) && validate(
                    edtImage.toString()
                )
            ) {
                editor.putString("username", edtName.toString())
                editor.putString("phone", edtPhone.toString())
                editor.putString("image", edtImage.toString())
                editor.apply()

            }
        }


    }

    private fun validate(validation: String): Boolean {
        return validation.isNotBlank() && validation.isNotEmpty()
    }

    private fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }

    private fun isOnlyLetters(toCheck: String): Boolean {
        val regex = "^[A-Za-z]*$".toRegex()
        return toCheck.matches(regex)
    }
}