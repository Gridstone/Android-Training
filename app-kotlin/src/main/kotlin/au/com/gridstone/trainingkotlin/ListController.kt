package au.com.gridstone.trainingkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import au.com.gridstone.trainingkotlin.ListState.ERROR
import au.com.gridstone.trainingkotlin.ListState.LOADING
import au.com.gridstone.trainingkotlin.ListState.RESULTS
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

enum class ListState {
  LOADING,
  RESULTS,
  ERROR
}

class ListController : Controller() {

  private lateinit var swipeRefreshLayout: SwipeRefreshLayout

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup
  ): View {
    val view = inflater.inflate(R.layout.controller_list, container, false)
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

      view.findViewById<RecyclerView>(R.id.my_recycler_view)
          ?.let { recyclerView ->
            print(recyclerView.layoutManager)
            print(recyclerView.adapter)
            recyclerView.apply {
              setHasFixedSize(true)
              layoutManager = viewManager
              adapter = viewAdapter
            }
          }
    }

    swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
    swipeRefreshLayout.setOnRefreshListener {
      loadData()
    }

    return view
  }

  override fun onAttach(view: View) {
    super.onAttach(view)
    loadData()
  }

  private fun showState(listState: ListState) {

    // Assume that whenever we are showing a state, we no longer want to show the refresh indicator
    swipeRefreshLayout.isRefreshing = false

    val progressBar: ProgressBar? = view?.findViewById(R.id.list_progress_bar)
    val recyclerView: RecyclerView? = view?.findViewById(R.id.my_recycler_view)
    val errorTextView: TextView? = view?.findViewById(R.id.errorTextView)
    when (listState) {
      LOADING -> {
        progressBar?.isVisible = true
        recyclerView?.isVisible = false
        errorTextView?.isVisible = false
      }
      RESULTS -> {
        progressBar?.isVisible = false
        recyclerView?.isVisible = true
        errorTextView?.isVisible = false
      }
      ERROR -> {
        progressBar?.isVisible = false
        recyclerView?.isVisible = false
        errorTextView?.isVisible = true
      }
    }
  }

  private fun populateResults(data: List<PokemonSummary>) {
    view?.findViewById<RecyclerView>(R.id.my_recycler_view)
        ?.let { recyclerView ->
          (recyclerView.adapter as MyRecyclerViewAdapter).set(data)
          recyclerView.adapter?.notifyDataSetChanged()
          showState(RESULTS)
        }
  }

  private fun loadData() {
    APIManager.listObservable.subscribe(object : Observer<PokemonBaseResponse> {
      override fun onSubscribe(d: Disposable) {}

      override fun onNext(t: PokemonBaseResponse) {
        populateResults(t.results)
      }

      override fun onError(e: Throwable) {
        showState(ERROR)
        view?.findViewById<TextView>(R.id.errorTextView)
            ?.text = e.localizedMessage
      }

      override fun onComplete() {}
    })
  }
}