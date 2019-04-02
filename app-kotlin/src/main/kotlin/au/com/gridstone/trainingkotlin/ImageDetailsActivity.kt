package au.com.gridstone.trainingkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout

class ImageDetailsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_image)

        val detailTextOverlayView = findViewById<View>(R.id.detailTextOverlayView)
        detailTextOverlayView.background.mutate().alpha = 200

    }
}