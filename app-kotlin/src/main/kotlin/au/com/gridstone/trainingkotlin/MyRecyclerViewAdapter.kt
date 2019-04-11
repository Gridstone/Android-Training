package au.com.gridstone.trainingkotlin

import android.graphics.ImageDecoder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.squareup.picasso.Picasso

class MyRecyclerViewAdapter(
  controller: Controller
) :
    RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>() {

  private var controller: Controller = controller

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
            controller.router.pushController(
                RouterTransaction.with(DetailsController(id))
                    .pushChangeHandler(FadeChangeHandler()).popChangeHandler(FadeChangeHandler())
            )
          }

    }
  }

  override fun getItemCount() = myDataset.size
}