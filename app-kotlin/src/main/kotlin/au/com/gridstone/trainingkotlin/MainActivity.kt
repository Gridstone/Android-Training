package au.com.gridstone.trainingkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

  private lateinit var linearLayoutManager: LinearLayoutManager

  override fun onCreate(savedInstanceState: Bundle?) {

    linearLayoutManager = LinearLayoutManager(this)
    var recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view)
    recyclerView.layoutManager = linearLayoutManager

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}
