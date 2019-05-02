package au.com.gridstone.trainingkotlin

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import au.com.gridstone.robocop.utils.bindView
import au.com.gridstone.trainingkotlin.PokemonListState.Content
import au.com.gridstone.trainingkotlin.PokemonListState.Error
import au.com.gridstone.trainingkotlin.PokemonListState.Loading
import org.w3c.dom.Text

class ListView(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {

  private val progressBar: ProgressBar by bindView(R.id.list_progress_bar)
  private val errorTextView: TextView by bindView(R.id.errorTextView)
  private val swipeRefreshLayout: SwipeRefreshLayout by bindView(R.id.swipe_refresh_layout)
  private val recyclerView: RecyclerView by bindView(R.id.my_recycler_view)

  fun display(state: PokemonListState) {
    // Assume that whenever we are showing a state, we no longer want to show the refresh indicator
    swipeRefreshLayout.isRefreshing = false

    when (state) {
      is Loading -> {
        progressBar?.isVisible = true
        recyclerView?.isVisible = false
        errorTextView?.isVisible = false
      }
      is Content -> {
        (recyclerView.adapter as MyRecyclerViewAdapter).set(state.list)
        recyclerView.adapter?.notifyDataSetChanged()
        progressBar?.isVisible = false
        recyclerView?.isVisible = true
        errorTextView?.isVisible = false
      }
      is Error -> {
        errorTextView.text = state.message
        progressBar?.isVisible = false
        recyclerView?.isVisible = false
        errorTextView?.isVisible = true
      }
    }
  }

}