package natife.zaharchyk.giphyapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import natife.zaharchyk.giphyapp.data.model.GifObject
import natife.zaharchyk.giphyapp.databinding.ItemGifBinding

class GifAdapter(
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<GifAdapter.GifViewHolder>() {

    private val items = mutableListOf<GifObject>()

    fun submitList(list: List<GifObject>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val binding = ItemGifBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GifViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class GifViewHolder(
        private val binding: ItemGifBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GifObject) {
            val gifUrl = item.images.original.url

            Glide.with(binding.imageView)
                .asGif()
                .load(gifUrl)
                .into(binding.imageView)

            binding.root.setOnClickListener {
                onClick(gifUrl)
            }
        }
    }
}