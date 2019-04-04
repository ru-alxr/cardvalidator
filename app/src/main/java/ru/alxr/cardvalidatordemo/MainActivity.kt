package ru.alxr.cardvalidatordemo

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.alxr.cardvalidator.Result
import ru.alxr.cardvalidator.ValidatorBuilder
import java.util.concurrent.Future

class MainActivity : AppCompatActivity() {

    private var future: Future<Unit>? = null
    private lateinit var resultView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<View>(R.id.button)
        val input: EditText = findViewById(R.id.edit_text_view)
        resultView = findViewById(R.id.result_view)
        view?.setOnClickListener {
            resultView.text = ""
            future?.cancel(true)
            val source = input.text.toString()
            if (source.trim().isEmpty()) return@setOnClickListener
            future = ValidatorBuilder()
                .setSource(input.text.toString())
                .setSuccessListener(this::onSuccess)
                .setFailListener(this::onFail)
                .get()
        }

    }

    private fun onFail(throwable: Throwable) {
        resultView.text = throwable.message
    }

    private fun onSuccess(result: Result) {
        val text = "Card is ${if (result.isValid) "valid" else "invalid"}"
        resultView.text = text
        resultView.append("\n")
        if (result.cardInfo != null) {
            resultView.append(result.cardInfo.toString())
        } else {
            resultView.append("Card info is unavailable")
        }
        result.firstCheckError?.apply {
            resultView.append("\n")
            resultView.append("First check (Local) error ${this.localizedMessage}")
        }
        result.secondCheckError?.apply {
            resultView.append("\n")
            resultView.append("Second check (Remote) error ${this.localizedMessage}")
        }
    }

}