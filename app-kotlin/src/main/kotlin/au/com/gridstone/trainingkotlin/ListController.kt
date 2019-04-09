package au.com.gridstone.trainingkotlin

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
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
            populateResults(imageData)
        }
    }

    private fun populateResults(data: List<ImageData>) {
        router.activity?.applicationContext?.let { context ->
            val viewAdapter = MyRecyclerViewAdapter(data.toTypedArray(), this)

            val viewManager = LinearLayoutManager(context)

            view?.findViewById<RecyclerView>(R.id.my_recycler_view)?.let { recyclerView ->
                recyclerView.isVisible = true
                view?.findViewById<ProgressBar>(R.id.list_progress_bar)?.isVisible = false
                recyclerView .apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
            }
        }
    }

    private fun loadData() {
        val call = APIManager.service.getImages()

        call.enqueue(object : Callback<ImageDataResponse> {
            override fun onResponse(call: Call<ImageDataResponse>, response: Response<ImageDataResponse>) {
                response.body()?.getData()?.let { data ->
                    val filteredResults = filteredResults(data)
                    populateResults(filteredResults)
                    APIManager.cachedImageData = filteredResults
                }
            }

            override fun onFailure(call: Call<ImageDataResponse>, t: Throwable) { }
        })
    }

    private fun filteredResults(results: List<ImageData>): List<ImageData> {
        return results.filter { !it.getIsAlbum() }.filter { it.getType() == "image/jpeg" || it.getType() == "image/png"  }
    }
}