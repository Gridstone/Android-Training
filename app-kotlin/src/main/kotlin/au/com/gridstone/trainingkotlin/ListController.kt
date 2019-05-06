package au.com.gridstone.trainingkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

sealed class PokemonListState {
  object Loading : PokemonListState()
  data class Content(val list: List<PokemonSummary>) : PokemonListState()
  data class Error(val message: String) : PokemonListState()
}

class ListController : Controller() {

  private lateinit var swipeRefreshLayout: SwipeRefreshLayout
  private var disposable: Disposable = Disposables.disposed()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup
  ): View {
    val view = inflater.inflate(R.layout.view_list, container, false)
    router.activity?.applicationContext?.let { context ->
      val viewAdapter = MyRecyclerViewAdapter()
      viewAdapter.set(ArrayList())

      viewAdapter.selections.subscribe { id ->
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
      APIManager.refresh()
    }

    return view
  }

  override fun onAttach(view: View) {
    loadData()
  }

  override fun onDetach(view: View) {
    disposable.dispose()
  }

  private fun loadData() {
    disposable = APIManager.pokemonListResults
        .map { result ->
          when (result) {
            is PokemonListResult.Loading -> PokemonListState.Loading
            is PokemonListResult.Content -> PokemonListState.Content(result.list)
            is PokemonListResult.Error -> PokemonListState.Error(result.message)
          }
        }
        .subscribe { state ->
          (view as ListView).display(state)
        }
  }
}