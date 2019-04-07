package au.com.gridstone.trainingkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.util.*


class ImageDetailsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_image)

        val imageData = intent.getSerializableExtra("serialized_imageData") as ImageData

        val detailTextOverlayView = findViewById<View>(R.id.detailTextOverlayView)
        detailTextOverlayView.background.mutate().alpha = 200

        val imageView = findViewById<ImageView>(R.id.imageDetailsImageView)
        Picasso.get().load(imageData.getImageURL()).into(imageView)

        val titleTextView = findViewById<TextView>(R.id.imageDetailsTitleTextView)
        titleTextView.text = imageData.getTitle()

        val widthTextView = findViewById<TextView>(R.id.imageWidthValueTextView)
        widthTextView.text = imageData.getWidth().toString()

        val heightTextView = findViewById<TextView>(R.id.imageHeightValueTextView)
        heightTextView.text = imageData.getHeight().toString()

        val viewCountTextView = findViewById<TextView>(R.id.viewCountValueTextView)
        viewCountTextView.text = imageData.getViewCount().toString()

        val dateTimeTextView = findViewById<TextView>(R.id.timeAgoValueTextView)
        imageData.getDateTime()?.let { imageTimeinSeconds ->
            val currentTimeInSeconds = System.currentTimeMillis() / 1000
            val seconds = currentTimeInSeconds - imageTimeinSeconds
            val minutes = seconds / 60
            val hours = minutes / 60
            dateTimeTextView.text = "$hours hours ago"
        }
    }
}