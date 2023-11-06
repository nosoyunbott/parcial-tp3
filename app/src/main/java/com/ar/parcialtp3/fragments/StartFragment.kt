package com.ar.parcialtp3.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.findNavController
import com.ar.parcialtp3.R
import com.ar.parcialtp3.utils.SharedPrefUtils

class StartFragment : Fragment() {

    lateinit var v: View

    lateinit var edtName: EditText
    lateinit var edtPhone: EditText
    lateinit var edtImage: EditText
    lateinit var btnLogin: Button

    //Shared
    lateinit var sharedPrefUtils: SharedPrefUtils

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

        }
    }

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
        sharedPrefUtils = SharedPrefUtils(requireContext())
        return v
    }

    override fun onStart() {
        super.onStart()

        sharedPrefUtils.resetFavourites()

        btnLogin.setOnClickListener {
            if ((validate(edtName.text.toString()) && isOnlyLetters(edtName.text.toString())) && (validate(
                    edtPhone.text.toString()
                ) && isNumeric(edtPhone.text.toString())) && validate(
                    edtImage.text.toString()
                )
            ) {
                sharedPrefUtils.saveUserToSharedPref(
                    edtName.text.toString(),
                    edtPhone.text.toString(),
                    edtImage.text.toString()
                )

                Toast.makeText(requireContext(), "Bienvenido, ${edtName.text}!", Toast.LENGTH_SHORT)

                val action = StartFragmentDirections.actionStartFragmentToMainActivity()
                v.findNavController().navigate(action)

            } else {
                Toast.makeText(requireContext(), "Hubo un error!", Toast.LENGTH_SHORT).show()
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