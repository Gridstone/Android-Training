package au.com.gridstone.trainingkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import android.os.Bundle
import androidx.core.os.bundleOf
import java.lang.IllegalArgumentException

private const val KEY_IMAGE_ID = "image_id"

class DetailsController(args: Bundle) : Controller(args) {

  constructor(id: String) : this(bundleOf(KEY_IMAGE_ID to id))

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup
  ): View {
    return inflater.inflate(R.layout.view_details, container, false)
  }

  override fun onAttach(view: View) {
    super.onAttach(view)

    if (view !is DetailsView) throw IllegalArgumentException()

    args.getString(KEY_IMAGE_ID)
        ?.let { id ->
          APIManager.getCachedImageForID(id)
              ?.let { imageData ->
                view.display(imageData)
              }
        }
  }
}