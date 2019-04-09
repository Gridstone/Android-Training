package au.com.gridstone.trainingkotlin

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.Controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListController: Controller() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_list, container, false)
    }

    override fun onActivityStarted(activity: Activity) {
        super.onActivityStarted(activity)
        loadData()
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        APIManager.cachedImageData?.let { imageData ->
            populateView(imageData)
        }
    }

    fun populateView(data: List<ImageData>) {
        router.activity?.applicationContext?.let { context ->
            val viewAdapter = MyRecyclerViewAdapter(data.toTypedArray(), this)

            val viewManager = LinearLayoutManager(context)

            view?.findViewById<RecyclerView>(R.id.my_recycler_view)?.let { recyclerView ->
                recyclerView .apply {
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
    }

    fun loadData() {
//        setContentView(R.layout.activity_loading_main)

        val call = APIManager.service.getImages()

        call.enqueue(object : Callback<ImageDataResponse> {
            override fun onResponse(call: Call<ImageDataResponse>, response: Response<ImageDataResponse>) {
                response.body()?.getData()?.let { data ->
                    val filteredResults = filteredResults(data)
                    populateView(filteredResults)
                    APIManager.cachedImageData = filteredResults
                } ?: run {
//                    setContentView(R.layout.activity_error_main)
                }
            }

            override fun onFailure(call: Call<ImageDataResponse>, t: Throwable) {
//                setContentView(R.layout.activity_error_main)
            }
        })
    }

    fun filteredResults(results: List<ImageData>): List<ImageData> {
        return results.filter { !it.getIsAlbum() }.filter { it.getType() == "image/jpeg" || it.getType() == "image/png"  }
    }
}