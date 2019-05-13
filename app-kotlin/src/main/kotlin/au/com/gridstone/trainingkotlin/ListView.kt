package au.com.gridstone.trainingkotlin

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import au.com.gridstone.robocop.utils.bindView
import au.com.gridstone.trainingkotlin.PokemonListState.Content
import au.com.gridstone.trainingkotlin.PokemonListState.Error
import au.com.gridstone.trainingkotlin.PokemonListState.Loading
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.touches
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

sealed class ListViewEvent {
  object Refresh : ListViewEvent()
  data class Selection(val index: Int) : ListViewEvent()
}

class ListView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

  private val progressBar: ProgressBar by bindView(R.id.list_progress_bar)
  private val errorTextView: TextView by bindView(R.id.errorTextView)
  private val swipeRefreshLayout: SwipeRefreshLayout by bindView(R.id.swipe_refresh_layout)
  private val recyclerView: RecyclerView by bindView(R.id.my_recycler_view)
  private val retryButton: Button by bindView(R.id.listViewRetryButton)

  private var eventsRelay: PublishRelay<ListViewEvent> = PublishRelay.create()

  val events: Observable<ListViewEvent> = eventsRelay

  override fun onFinishInflate() {
    super.onFinishInflate()
    val viewAdapter = PokemonDisplayableAdapter()
    viewAdapter.set(ArrayList())

    viewAdapter.selections.subscribe(eventsRelay)

    swipeRefreshLayout.refreshes().map { ListViewEvent.Refresh }.subscribe(eventsRelay)

    retryButton.clicks().map { ListViewEvent.Refresh }.subscribe(eventsRelay)


    val viewManager = LinearLayoutManager(context)

    recyclerView.apply {
      setHasFixedSize(true)
      layoutManager = viewManager
      adapter = viewAdapter
    }
  }

  fun display(state: PokemonListState) {
    // Assume that whenever we are showing a state, we no longer want to show the refresh indicator
    swipeRefreshLayout.isRefreshing = false

    when (state) {
      is Loading -> {
        progressBar.isVisible = true
        recyclerView.isVisible = false
        errorTextView.isVisible = false
        retryButton.isVisible = false
        swipeRefreshLayout.isVisible = false
      }
      is Content -> {
        (recyclerView.adapter as PokemonDisplayableAdapter).set(state.list)
        recyclerView.adapter?.notifyDataSetChanged()
        progressBar.isVisible = false
        recyclerView.isVisible = true
        errorTextView.isVisible = false
        retryButton.isVisible = false
        swipeRefreshLayout.isVisible = true
      }
      is Error -> {
        errorTextView.text = state.message
        progressBar.isVisible = false
        swipeRefreshLayout.isVisible = false
        errorTextView.isVisible = true
        retryButton.isVisible = true
        swipeRefreshLayout.isVisible = false
      }
    }
  }

}