package au.com.gridstone.trainingkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_loading_main)

        val values: MutableList<String> = ArrayList()

        val call = APIManager.service.getImages()

        call.enqueue(object : Callback<ImageDataResponse> {
            override fun onResponse(call: Call<ImageDataResponse>, response: Response<ImageDataResponse>) {
                response.body()?.getData()?.forEach {imageData ->
                    values.add(imageData.getTitle())
                }
                populateView(values)
            }

            override fun onFailure(call: Call<ImageDataResponse>, t: Throwable) {
                print(t.message)
            }
        })
    }

    fun populateView(data: List<String>) {
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)

        viewAdapter = MyRecyclerViewAdapter(data.toTypedArray(), this)

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
