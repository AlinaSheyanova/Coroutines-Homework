package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext


class CatsPresenter(
    private val catsService: CatsService,
    private val pictureService: PictureService
) {

    private var _catsView: ICatsView? = null

    class PresenterScope : CoroutineScope {

        private var _job: Job = SupervisorJob()

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + _job + CoroutineName("CatsCoroutine")


        suspend fun Cancel() {
            _job.cancelAndJoin()
        }
    }

    private val _presenterScope = PresenterScope()

    private suspend fun requestNextFact() : String
    {
        val responseFact = catsService.getCatFact()
        if (responseFact.isSuccessful && responseFact.body() != null) {
            return responseFact.body()?.text ?: ""
        }
        return ""
    }

    private suspend fun requestNextPicture() : String {
        val responsePicture = pictureService.getPicture()
        if (responsePicture.isSuccessful && responsePicture.body() != null) {
            return responsePicture.body()?.first()?.url ?: ""
        }
        return ""
    }


    fun onInitComplete() = _presenterScope.launch {
        try {
            val fact = async { requestNextFact() }
            val pict = async { requestNextPicture() }
            _catsView?.populate(Info(fact.await(), pict.await()))
        } catch (e: SocketTimeoutException) {
            _catsView?.showErrorMessage("Не удалось получить ответ от сервером")
        } catch (e: Exception) {
            CrashMonitor.trackWarning(e.message.toString())
            _catsView?.showErrorMessage(e.message)
        }
    }


    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        runBlocking {
            _presenterScope.Cancel()
        }
    }
}
