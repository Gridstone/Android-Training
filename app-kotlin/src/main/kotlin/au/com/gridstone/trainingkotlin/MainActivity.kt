package au.com.gridstone.trainingkotlin

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var savedResults: List<ImageData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val x: Array<ImageData>? = savedInstanceState?.getSerializable("savedResults") as? Array<ImageData>
        x?.let { savedResults ->
            populateView(savedResults.toList())
            this.savedResults = savedResults.toList()
            val position = savedInstanceState.getInt("scrollPosition")
            val recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view)
            recyclerView.layoutManager?.scrollToPosition(position)
            recyclerView.layoutManager?.scrollToPosition(position)
        } ?: run {
            loadData()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable("savedResults", savedResults?.toTypedArray())
        val recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view)
        val position = (recyclerView.getLayoutManager() as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        outState?.putInt("scrollPosition", position)
    }


    fun populateView(data: List<ImageData>) {
        setContentView(R.layout.activity_main)

        val viewAdapter = MyRecyclerViewAdapter(data.toTypedArray(), this)

        val viewManager = LinearLayoutManager(this)

        findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    fun loadData() {
        setContentView(R.layout.activity_loading_main)

        val call = APIManager.service.getImages()

        call.enqueue(object : Callback<ImageDataResponse> {
            override fun onResponse(call: Call<ImageDataResponse>, response: Response<ImageDataResponse>) {
                response.body()?.getData()?.let { data ->
                    val filteredResults = filteredResults(data)
                    savedResults = filteredResults
                    populateView(filteredResults)
                } ?: run {
                    setContentView(R.layout.activity_error_main)
                }
            }

            override fun onFailure(call: Call<ImageDataResponse>, t: Throwable) {
                setContentView(R.layout.activity_error_main)
            }
        })
    }

    fun filteredResults(results: List<ImageData>): List<ImageData> {
        return results.filter { !it.getIsAlbum() }.filter { it.getType() == "image/jpeg" || it.getType() == "image/png"  }
    }
}
