package au.com.gridstone.trainingkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.Controller

class ListController : Controller() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup
  ): View {
    return inflater.inflate(R.layout.controller_list, container, false)
  }

  override fun onAttach(view: View) {
    super.onAttach(view)
    loadData()
  }

  private fun populateResults(data: List<ImageData>) {
    router.activity?.applicationContext?.let { context ->
      val viewAdapter = MyRecyclerViewAdapter(data.toTypedArray(), this)

      val viewManager = LinearLayoutManager(context)

      view?.findViewById<RecyclerView>(R.id.my_recycler_view)
          ?.let { recyclerView ->
            recyclerView.isVisible = true
            view?.findViewById<ProgressBar>(R.id.list_progress_bar)
                ?.isVisible = false
            recyclerView.apply {
              setHasFixedSize(true)
              layoutManager = viewManager
              adapter = viewAdapter
            }
          }
    }
  }

  private fun loadData() {
    APIManager.getImages(useCached = true) { imageData -> populateResults(imageData) }
  }
}