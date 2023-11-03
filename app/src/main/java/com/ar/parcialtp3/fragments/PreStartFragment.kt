package com.ar.parcialtp3.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.ar.parcialtp3.R


class PreStartFragment : Fragment() {

    lateinit var v: View
    lateinit var btn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_pre_start, container, false)
        btn = v.findViewById(R.id.getStartedBtn)
        return v
    }

    override fun onStart() {
        super.onStart()
        btn.setOnClickListener {
        val action = PreStartFragmentDirections.actionPreStartFragmentToStartFragment()
            v.findNavController().navigate(action)
        }
    }
}