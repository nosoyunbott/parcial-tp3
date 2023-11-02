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
import android.widget.ImageView
import android.widget.TextView
import com.ar.parcialtp3.MainActivity
import com.ar.parcialtp3.R
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {

    lateinit var v: View
    lateinit var imgProfile: ImageView
    lateinit var txtProfileName: TextView
    lateinit var btnProfileSubmit: Button
    lateinit var edtSubmitImage: EditText

    //Shared
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false)
        imgProfile = v.findViewById(R.id.imgProfile)
        txtProfileName = v.findViewById(R.id.txtProfileName)
        btnProfileSubmit = v.findViewById(R.id.btnProfileSubmit)
        edtSubmitImage = v.findViewById(R.id.edtProfileSubmitImage)

        return v
    }

    override fun onStart() {
        super.onStart()

        //Shared
        sharedPreferences =
            requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        val imageUrl = sharedPreferences.getString("image", "")
        Glide.with(this).load(imageUrl).into(imgProfile)
        val username = sharedPreferences.getString("username", "")
        txtProfileName.text = username

        btnProfileSubmit.setOnClickListener {
            if (edtSubmitImage.text.toString().isNotEmpty() && edtSubmitImage.text.toString()
                    .isNotBlank() && startsWithHttps(edtSubmitImage.text.toString())
            ) {
                editor.putString("image", edtSubmitImage.text.toString())
                editor.apply()
                val imageUrl = sharedPreferences.getString("image", "")
                Glide.with(this).load(imageUrl).into(imgProfile)

                //Update header

                val parentActivity: MainActivity? = activity as? MainActivity
                if (parentActivity != null) {
                    Glide.with(this).load(imageUrl).into(parentActivity.imgHeader)
                }
            }
        }



    }

    private fun startsWithHttps(toCheck: String): Boolean {
        return toCheck.startsWith("https://")
    }
}