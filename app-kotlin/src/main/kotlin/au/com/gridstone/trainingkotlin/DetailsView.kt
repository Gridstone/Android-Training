package au.com.gridstone.trainingkotlin

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import au.com.gridstone.robocop.utils.bindView
import au.com.gridstone.trainingkotlin.PokemonDetailsState.Content
import au.com.gridstone.trainingkotlin.PokemonDetailsState.Error
import au.com.gridstone.trainingkotlin.PokemonDetailsState.Loading
import com.squareup.picasso.Picasso

class DetailsView(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {

  private val progressBar: ProgressBar by bindView(R.id.details_progress_bar)
  private val errorTextView: TextView by bindView(R.id.detailsErrorTextView)
  private val detailsSection: ConstraintLayout by bindView(R.id.detailsSection)
  private val titleView: TextView by bindView(R.id.imageDetailsTitleTextView)
  private val attackValueTextView: TextView by bindView(R.id.attackValueTextView)
  private val defenceValueTextView: TextView by bindView(R.id.defenseValueTextView)
  private val speedValueTextView: TextView by bindView(R.id.speedValueTextView)
  private val specialAttackValueTextView: TextView by bindView(R.id.specialAttackValueTextView)
  private val specialDefenseValueTextView: TextView by bindView(R.id.specialDefenseValueTextView)
  private val hpValueTextView: TextView by bindView(R.id.hpValueTextView)
  private val detailTextOverlayView: View by bindView(R.id.detailTextOverlayView)
  private val imageView: ImageView by bindView(R.id.imageDetailsImageView)

  fun display(state: PokemonDetailsState) {

    fun display(displayable: PokemonDetailsDisplayable) {
      titleView.text = displayable.title
      attackValueTextView.text = displayable.attackValue
      defenceValueTextView.text = displayable.defenseValue
      speedValueTextView.text = displayable.speedValue
      specialAttackValueTextView.text = displayable.specialAttackValue
      specialDefenseValueTextView.text = displayable.specialDefenseValue
      hpValueTextView.text = displayable.hpValue
      Picasso.get()
          .load(displayable.imageURL)
          .into(imageView)
      progressBar.isVisible = false
      detailsSection.isVisible = true
    }

    when (state) {
      is Loading -> {
        progressBar.isVisible = true
        detailsSection.isVisible = false
        errorTextView.isVisible = false
      }
      is Content -> {
        progressBar.isVisible = false
        detailsSection.isVisible = true
        errorTextView.isVisible = false
        display(state.displayable)
      }
      is Error -> {
        progressBar.isVisible = false
        detailsSection.isVisible = false
        errorTextView.isVisible = true
        errorTextView.text = state.message
      }
    }
  }
}