"# cardvalidator"

Usage

```

class MainActivity : AppCompatActivity() {

    private var future: Future<Unit>? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        
        ...
    
        view?.setOnClickListener {
            future?.cancel(true)
            val source = ...
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
        
        ...

    }

}

```