package au.com.gridstone.trainingkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import android.os.Bundle
import androidx.core.os.bundleOf
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.lang.IllegalArgumentException

private const val POKEMON_ID = "pokemon_id"

class DetailsController(args: Bundle) : Controller(args) {

  constructor(id: Int) : this(bundleOf(POKEMON_ID to id))

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup
  ): View {
    return inflater.inflate(R.layout.view_details, container, false)
  }

  override fun onAttach(view: View) {
    super.onAttach(view)

    if (view !is DetailsView) throw IllegalArgumentException()

    val id = args.getInt(POKEMON_ID)

    APIManager.getPokemon(id,
        object : Observer<Pokemon> {
          override fun onSubscribe(d: Disposable) {}
          override fun onNext(t: Pokemon) {
            view.display(t)
          }

          override fun onError(e: Throwable) {}

          override fun onComplete() {}
        })
  }
}