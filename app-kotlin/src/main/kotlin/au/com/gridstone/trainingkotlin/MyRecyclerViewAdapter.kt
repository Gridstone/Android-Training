package au.com.gridstone.trainingkotlin

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyRecyclerViewAdapter :
    RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>() {

  var tapHandler: ((imageId: String) -> Unit)? = null

  private lateinit var myDataset: List<ImageData>

  class MyViewHolder(val view: FrameLayout) : RecyclerView.ViewHolder(view)

  fun set(data: List<ImageData>) {
    myDataset = data
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): MyRecyclerViewAdapter.MyViewHolder {
    val row = LayoutInflater.from(parent.context)
        .inflate(R.layout.recyclerview_row, parent, false) as FrameLayout
    return MyViewHolder(row)
  }

  override fun onBindViewHolder(
    holder: MyViewHolder,
    position: Int
  ) {
    val imageData = myDataset[position]
    val textView = holder.view.findViewById<TextView>(R.id.imageListTitle)
    textView.text = imageData.title
    val imageView = holder.view.findViewById<ImageView>(R.id.imageListImageView)
    Picasso.get()
        .load(imageData.imageUrl)
        .into(imageView)
    textView.background.mutate()
        .alpha = 200
    textView.background.alpha = 200
    textView.setGravity(Gravity.CENTER_VERTICAL);

    holder.view.setOnClickListener {
      imageData.id
          ?.let { id ->
            tapHandler?.invoke(id)
          }
    }
  }

  override fun getItemCount() = myDataset.size
}