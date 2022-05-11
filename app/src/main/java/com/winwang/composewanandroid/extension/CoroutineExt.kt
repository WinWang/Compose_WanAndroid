import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> withUI(noinline block: suspend CoroutineScope.() -> T): T {
    return withContext(Dispatchers.Main, block)
}

suspend inline fun <T> withIO(noinline block: suspend CoroutineScope.() -> T): T {
    return withContext(Dispatchers.IO, block)
}