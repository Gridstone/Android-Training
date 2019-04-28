package au.com.gridstone.trainingkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class ListController : Controller() {

  private lateinit var swipeRefreshLayout: SwipeRefreshLayout
  private var savedPokemonList: List<PokemonSummary>? = null

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

  private fun populateResults(data: List<PokemonSummary>) {
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

    savedPokemonList?.let { pokenon ->
      populateResults(pokenon)
      swipeRefreshLayout.isRefreshing = false
    } ?: run {
      APIManager.getPokemonList(
          object : Observer<PokemonBaseResponse> {
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(t: PokemonBaseResponse) {
              populateResults(t.results)
              savedPokemonList = t.results
              swipeRefreshLayout.isRefreshing = false
            }

            override fun onError(e: Throwable) {}

            override fun onComplete() {}
          }
      )
    }
  }
}