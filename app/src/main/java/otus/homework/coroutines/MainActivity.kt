package otus.homework.coroutines

import CatsViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

   private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = CatsViewModel(diContainer.catService, diContainer.pictureService)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        view.setOnClickListener {
            viewModel.getData()
        }
        setContentView(view)
        lifecycleScope.launch {
            viewModel.State.collect {
                view.populate(it)
            }
        }
    }
}