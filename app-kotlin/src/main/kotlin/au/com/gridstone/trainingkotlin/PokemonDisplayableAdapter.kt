package au.com.gridstone.trainingkotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.PublishRelay
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import au.com.gridstone.robocop.utils.bindView
import com.jakewharton.rxbinding3.view.clicks

class PokemonDisplayableAdapter :
    RecyclerView.Adapter<PokemonDisplayableAdapter.PokemonDisplayableViewHolder>() {

  private val tapRelay: PublishRelay<ListViewEvent.Selection> = PublishRelay.create()
  val selections: Observable<ListViewEvent.Selection> = tapRelay

  private lateinit var myDataSet: List<PokemonDisplayable>

  fun set(data: List<PokemonDisplayable>) {
    myDataSet = data
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): PokemonDisplayableViewHolder {
    val row = LayoutInflater.from(parent.context)
        .inflate(R.layout.recyclerview_row, parent, false) as FrameLayout
    return PokemonDisplayableViewHolder(row)
  }

  override fun onBindViewHolder(
      holder: PokemonDisplayableViewHolder,
      position: Int
  ) {
    holder.bindTo(myDataSet[position])
  }

  override fun getItemCount() = myDataSet.size

  inner class PokemonDisplayableViewHolder(view: FrameLayout) : RecyclerView.ViewHolder(view) {
    private val titleTextView: TextView by bindView(R.id.imageListTitle)
    private val imageView: ImageView by bindView(R.id.imageListImageView)

    private var id: Int? = null

    init {
      view.clicks()
          .map { ListViewEvent.Selection(id!!) }
          .subscribe(tapRelay)
    }

    fun bindTo(displayable: PokemonDisplayable) {
      id = displayable.id
      titleTextView.text = displayable.title
      Picasso.get()
          .load(displayable.imageURL)
          .into(imageView)
    }
  }
}