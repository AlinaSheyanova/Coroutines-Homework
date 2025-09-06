package otus.homework.coroutines

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(message: String) {
        println("Warning tracked: $message")
    }

    fun trackError(message: String) {
        println("Error tracked: $message")
    }
}