package au.com.gridstone.trainingkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler

class ListController : Controller() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup
  ): View {
    return inflater.inflate(R.layout.controller_list, container, false)
  }

  override fun onAttach(view: View) {
    super.onAttach(view)

    router.activity?.applicationContext?.let { context ->
      val viewAdapter = MyRecyclerViewAdapter()
      viewAdapter.set(ArrayList())
      viewAdapter.tapHandler = { id ->
        val transaction: RouterTransaction = RouterTransaction.with(DetailsController(id))
            .pushChangeHandler(FadeChangeHandler())
            .popChangeHandler(FadeChangeHandler())
        router.pushController(transaction)
      }

      val viewManager = LinearLayoutManager(context)

      view?.findViewById<RecyclerView>(R.id.my_recycler_view)
          ?.let { recyclerView ->
            recyclerView.apply {
              setHasFixedSize(true)
              layoutManager = viewManager
              adapter = viewAdapter
            }
          }
    }

    loadData()
  }

  private fun populateResults(data: List<ImageData>) {
    view?.findViewById<RecyclerView>(R.id.my_recycler_view)
        ?.let { recyclerView ->
          (recyclerView.adapter as MyRecyclerViewAdapter).set(data)
          recyclerView.adapter?.notifyDataSetChanged()
          recyclerView.isVisible = true
          view?.findViewById<ProgressBar>(R.id.list_progress_bar)
              ?.isVisible = false

        }
  }

  private fun loadData() {
    APIManager.getImages(useCached = true) { imageData -> populateResults(imageData) }
  }
}