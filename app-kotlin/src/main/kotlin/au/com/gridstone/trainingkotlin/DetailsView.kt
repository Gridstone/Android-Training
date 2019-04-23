package au.com.gridstone.trainingkotlin

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import au.com.gridstone.robocop.utils.bindView
import com.squareup.picasso.Picasso

class DetailsView(
  context: Context,
  attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

  private val titleView: TextView by bindView(R.id.imageDetailsTitleTextView)
  private val attackValueTextView: TextView by bindView(R.id.attackValueTextView)
  private val defenceValueTextView: TextView by bindView(R.id.defenseValueTextView)
  private val speedValueTextView: TextView by bindView(R.id.speedValueTextView)
  private val specialAttackValueTextView: TextView by bindView(R.id.specialAttackValueTextView)
  private val specialDefenseValueTextView: TextView by bindView(R.id.specialDefenseValueTextView)
  private val hpValueTextView: TextView by bindView(R.id.hpValueTextView)
  private val detailTextOverlayView: View by bindView(R.id.detailTextOverlayView)
  private val imageView: ImageView by bindView(R.id.imageDetailsImageView)

  fun display(pokemon: Pokemon) {
    val displayable = PokemonDetailsDisplayable(pokemon)
    titleView.text = displayable.title
    attackValueTextView.text = displayable.attackValue
    defenceValueTextView.text = displayable.defenseValue
    speedValueTextView.text = displayable.speedValue
    specialAttackValueTextView.text = displayable.specialAttackValue
    specialDefenseValueTextView.text = displayable.specialDefenseValue
    hpValueTextView.text = displayable.hpValue
    detailTextOverlayView
        .background.mutate()
        .alpha = 200
    Picasso.get()
        .load(displayable.imageURL)
        .into(imageView)
   }
}