import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.services.CatsService
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.data.Info
import otus.homework.coroutines.services.PictureService
import otus.homework.coroutines.data.Result
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsViewModel(
    private val catsService: CatsService,
    private val pictureService: PictureService
): ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is CancellationException -> throw throwable
            is SocketTimeoutException -> state.value =  Result.Error("Не удалось получить ответ от сервера")

            else -> {
                val msg: String = throwable.message.toString()
                state.value = Result.Error(msg)
                CrashMonitor.trackError(msg)
            }
        }
    }


    private val state = MutableStateFlow<Result>(Result.Success(Info("","")))

    val State: StateFlow<Result>
        get() {
            return state
        }

    init {
        getData()
    }

    private suspend fun requestNextFact(): String {
        val responseFact = catsService.getCatFact()
        if (responseFact.isSuccessful && responseFact.body() != null) {
            return responseFact.body()?.text ?: ""
        }
        return ""
    }

    private suspend fun requestNextPicture(): String {
        val responsePicture = pictureService.getPicture()
        if (responsePicture.isSuccessful && responsePicture.body() != null) {
            return responsePicture.body()?.first()?.url ?: ""
        }
        return ""
    }


    fun getData() {
        viewModelScope.launch(exceptionHandler) {
            coroutineScope {
                val fact = async { requestNextFact() }
                val pict = async { requestNextPicture() }
                state.value = Result.Success(Info(fact.await(), pict.await()))
            }
        }
    }
}
