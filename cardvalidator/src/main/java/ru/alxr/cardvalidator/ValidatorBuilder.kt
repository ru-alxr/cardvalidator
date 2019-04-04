package ru.alxr.cardvalidator

import android.os.Handler
import android.os.Looper
import ru.alxr.cardvalidator.impl.DefaultCardInfoParser
import ru.alxr.cardvalidator.impl.DefaultValidator
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

class ValidatorBuilder(
    private val handler: Handler = Handler(Looper.getMainLooper()),
    private val parser: ICardInfoParser = DefaultCardInfoParser(),
    private val validator: IValidator = DefaultValidator(parser)
) {

    private lateinit var source: String
    private var mSuccessListener: (Result) -> Unit = {}
    private var mFailListener: (Throwable) -> Unit = {}
    private lateinit var future: Future<Unit>

    fun setSource(_source: String): ValidatorBuilder {
        source = _source
        return this
    }

    fun setSuccessListener(listener: (Result) -> Unit): ValidatorBuilder {
        mSuccessListener = listener
        return this
    }

    fun setFailListener(listener: (Throwable) -> Unit): ValidatorBuilder {
        mFailListener = listener
        return this
    }

    fun get(): Future<Unit> {
        val callable: Callable<Unit> = Callable {
            try {
                val result = validator.validate(source)
                post(Runnable { mSuccessListener.invoke(result) })
            } catch (e: Exception) {
                post(Runnable { mFailListener.invoke(e) })
            }
            Unit
        }
        val futureTask: FutureTask<Unit> = FutureTask(callable)
        Executors.newSingleThreadExecutor().submit(futureTask)
        future = futureTask
        return futureTask
    }

    private fun post(runnable: Runnable) {
        if (!::future.isInitialized) return
        if (future.isCancelled) return
        handler.post(runnable)
    }

}