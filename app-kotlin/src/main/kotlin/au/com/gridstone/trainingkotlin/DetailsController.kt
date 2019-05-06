package au.com.gridstone.trainingkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import android.os.Bundle
import androidx.core.os.bundleOf
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import java.lang.IllegalArgumentException

private const val POKEMON_ID = "pokemon_id"

sealed class PokemonDetailsState {
  object Loading : PokemonDetailsState()
  data class Content(val displayable: PokemonDetailsDisplayable) : PokemonDetailsState()
  data class Error(val message: String) : PokemonDetailsState()
}

class DetailsController(args: Bundle) : Controller(args) {

  constructor(id: Int) : this(bundleOf(POKEMON_ID to id))

  private val id = args.getInt(POKEMON_ID)

  private var disposable: Disposable = Disposables.disposed()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup
  ): View {
    return inflater.inflate(R.layout.view_details, container, false)
  }

  override fun onAttach(view: View) {
    if (view !is DetailsView) throw IllegalArgumentException("View must be DetailsView")

    disposable = APIManager.detailsObservable(id)
        .map { result ->
          when (result) {
            is PokemonDetailsResult.Loading -> PokemonDetailsState.Loading
            is PokemonDetailsResult.Content -> PokemonDetailsState.Content(
                PokemonDetailsDisplayable(result.pokemon)
            )
            is PokemonDetailsResult.Error -> PokemonDetailsState.Error(result.message)
          }
        }
        .subscribe { state ->
          view.display(state)
        }
  }

  override fun onDetach(view: View) {
    disposable.dispose()
  }
}