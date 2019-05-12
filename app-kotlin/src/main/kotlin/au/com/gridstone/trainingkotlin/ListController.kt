package au.com.gridstone.trainingkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import io.reactivex.disposables.CompositeDisposable
import java.lang.IllegalArgumentException

sealed class PokemonListState {
  object Loading : PokemonListState()
  data class Content(val list: List<PokemonDisplayable>) : PokemonListState()
  data class Error(val message: String) : PokemonListState()
}

class ListController : Controller() {

  private var disposables = CompositeDisposable()

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup
  ): View = inflater.inflate(R.layout.view_list, container, false)


  override fun onAttach(view: View) {

    if (view !is ListView) throw IllegalArgumentException("View must be ListView")

    disposables.add(view.events.subscribe { selection ->
      when (selection) {
        is ListViewEvent.Selection -> {
          val transaction: RouterTransaction = RouterTransaction.with(
              DetailsController(selection.index))
              .pushChangeHandler(FadeChangeHandler())
              .popChangeHandler(FadeChangeHandler())
          router.pushController(transaction)
        }
        is ListViewEvent.Refresh -> APIManager.refresh()
      }
    })

    disposables.add(APIManager.pokemonListResults
                        .compose(pokemonListResultsToListState())
                        .subscribe { state ->
                          view.display(state)
                        })
  }

  override fun onDetach(view: View) {
    disposables.clear()
  }
}