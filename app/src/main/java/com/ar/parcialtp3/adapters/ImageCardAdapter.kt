import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ar.parcialtp3.R
import com.ar.parcialtp3.holders.ImageCardHolder
import com.bumptech.glide.Glide

class ImageCardAdapter(private val photoUrls: List<String>) : RecyclerView.Adapter<ImageCardHolder>() {
    private val selectedPhotos = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageCardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_card, parent, false)
        return ImageCardHolder(view)
    }

    override fun onBindViewHolder(holder: ImageCardHolder, position: Int) {
        val photoUrl = photoUrls[position]
        Glide.with(holder.itemView.context)
            .load(photoUrl)
            .into(holder.imageView)

        // Handle photo selection
        holder.itemView.setOnClickListener {
            if (selectedPhotos.contains(photoUrl)) {
                selectedPhotos.remove(photoUrl)
                // Deselect photo
                holder.imageView.alpha = 1.0f
            } else if (selectedPhotos.size < 5) {
                selectedPhotos.add(photoUrl)
                // Select photo
                holder.imageView.alpha = 0.5f
            }
        }
    }

    override fun getItemCount(): Int {
        return photoUrls.size
    }

    // Get the selected photos
    fun getSelectedPhotos(): Set<String> {
        return selectedPhotos
    }

}
