package au.com.gridstone.trainingkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

  private lateinit var recyclerView: RecyclerView
  private lateinit var viewAdapter: RecyclerView.Adapter<*>
  private lateinit var viewManager: RecyclerView.LayoutManager

  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    viewManager = LinearLayoutManager(this)
    viewAdapter = MyRecyclerViewAdapter(
            arrayOf("1", "2", "3","1", "2", "3","1", "2", "3","1", "2", "3","1", "2", "3","1", "2", "3","1", "2", "3","1", "2", "3","1", "2", "3")
    )

    recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
      // use this setting to improve performance if you know that changes
      // in content do not change the layout size of the RecyclerView
      setHasFixedSize(true)

      // use a linear layout manager
      layoutManager = viewManager

      // specify an viewAdapter (see also next example)
      adapter = viewAdapter

    }
  }
}
