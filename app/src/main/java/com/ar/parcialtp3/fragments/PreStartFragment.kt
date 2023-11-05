package com.ar.parcialtp3.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.ar.parcialtp3.R


class PreStartFragment : Fragment() {

    lateinit var v: View
    lateinit var btn: Button
    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_pre_start, container, false)
        btn = v.findViewById(R.id.getStartedBtn)
        /* viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
             override fun onPageSelected(position: Int) {
                 super.onPageSelected(position)
                 handler.removeCallbacks(runnable)
                 handler.postDelayed(runnable, 10000)
             }
         })
 */

        return v
    }

    private val runnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
}
    override fun onStart() {
        super.onStart()
        btn.setOnClickListener {
            val action = PreStartFragmentDirections.actionPreStartFragmentToStartFragment()
            v.findNavController().navigate(action)
        }
    }
}