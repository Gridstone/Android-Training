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
  private val timeAgoView: TextView by bindView(R.id.timeAgoValueTextView)
  private val widthValueView: TextView by bindView(R.id.imageWidthValueTextView)
  private val heightValueView: TextView by bindView(R.id.imageHeightValueTextView)
  private val viewCountValueView: TextView by bindView(R.id.viewCountValueTextView)
  private val detailTextOverlayView: View by bindView(R.id.detailTextOverlayView)
  private val imageView: ImageView by bindView(R.id.imageDetailsImageView)

  fun display(imageData: ImageData) {
    titleView.text = imageData.title
    imageData.dateTime
        ?.let { imageTimeInSeconds ->
          val currentTimeInSeconds = System.currentTimeMillis() / 1000
          val seconds = currentTimeInSeconds - imageTimeInSeconds
          val minutes = seconds / 60
          val hours = minutes / 60
          timeAgoView.text = "$hours hours ago"
        }
    widthValueView.text = "${imageData.width} px"
    heightValueView.text = "${imageData.height} px"
    viewCountValueView.text = imageData.views.toString()
    detailTextOverlayView
        .background.mutate()
        .alpha = 200
    Picasso.get()
        .load(imageData.imageUrl)
        .into(imageView)
  }
}