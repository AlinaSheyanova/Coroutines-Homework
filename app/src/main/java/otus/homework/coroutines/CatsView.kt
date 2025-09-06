package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.data.Info
import otus.homework.coroutines.data.Result

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    override fun setOnClickListener(p0: View.OnClickListener?) {
        findViewById<Button>(R.id.button).setOnClickListener(p0)
    }

    override fun populate(data: Info) {
        findViewById<TextView>(R.id.fact_textView).text = data.fact
        if (data.pictureUrl.isNotEmpty()) {
            Picasso.get().load(data.pictureUrl).into(findViewById<ImageView>(R.id.imageView))
        }
    }

    override fun populate(data: Result) {
        when (data) {
            is Result.Success -> populate(data.data)
            is Result.Error -> showErrorMessage(data.message)
        }
    }

    override fun showErrorMessage(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }
}

interface ICatsView {
    fun showErrorMessage(message: String?)
    fun populate(data: Result)
    fun populate(data: Info)
}