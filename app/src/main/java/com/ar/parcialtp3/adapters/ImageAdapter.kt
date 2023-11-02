package com.ar.parcialtp3.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ar.parcialtp3.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class ImageAdapter(private val imageList: ArrayList<String>, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgCarrouselContainer)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_container, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, @SuppressLint("RecyclerView") position: Int) {
        // Load image from URL using Glide
        Glide.with(holder.itemView.context)
            .asBitmap()
            .load(imageList[position])
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // Add the loaded Bitmap to the imageList
                    // Note: You might want to resize the bitmap if it's too large
                    val resizedBitmap = resizeBitmap(resource, targetImageWidth, targetImageHeight)
                    holder.imageView.setImageBitmap(resizedBitmap)
                    if (position == imageList.size - 1) {
                        viewPager2.post(runnable)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Do nothing here
                }
            })
    }

    // Define the target image width and height you desire
    private val targetImageWidth = 500
    private val targetImageHeight = 500

    // Resize the Bitmap to the specified width and height
    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }
}