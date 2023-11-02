package com.ar.parcialtp3.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.ar.parcialtp3.R

class SettingsFragment : Fragment() {

    lateinit var v: View
    lateinit var sharedPrefs: SharedPreferences
    lateinit var switch: Switch
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_settings, container, false)
        sharedPrefs = requireActivity().getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        switch = v.findViewById(R.id.switchSettingsDarkMode)
        return v
    }

    override fun onStart() {
        super.onStart()

        val editor = sharedPrefs.edit()
        editor.putBoolean("night", false)
        editor.apply()
        val nightMode = sharedPrefs.getBoolean("night", false)

        if(nightMode) {
            switch.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        switch.setOnCheckedChangeListener { buttonView, isChecked ->

            if(!isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("night", false)
                editor.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("night", true)
                editor.apply()
            }

        }

    }
}