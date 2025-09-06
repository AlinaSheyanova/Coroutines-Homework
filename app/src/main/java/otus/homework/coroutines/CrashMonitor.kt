package otus.homework.coroutines

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackError(message: String) {
        println("Error tracked: $message")
    }
}