package au.com.gridstone.training_kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import au.com.gridstone.training_kotlin.model.Model
import au.com.gridstone.training_kotlin.model.RecyclerAdapter

/**
 * Created by rowan on 13/7/17.
 */
class ListActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    val linearLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val recyclerView = findViewById(R.id.postList) as RecyclerView
        this.recyclerView = recyclerView
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = RecyclerAdapter()
    }
}