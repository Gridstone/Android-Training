package au.com.gridstone.training_kotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_loading)

    val runnable = Runnable {
      Thread.sleep(2000)
      val intent = Intent(this, ListActivity::class.java)
      startActivity(intent)
    }

    Thread(runnable).start()
  }
}
