package com.jamieadkins.gwent.data.update.repository

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.res.Resources
import android.os.Build
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.firebase.messaging.FirebaseMessaging
import com.jamieadkins.gwent.data.BuildConfig
import com.jamieadkins.gwent.data.R
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import io.reactivex.Completable
import io.reactivex.Observable
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class NotificationsRepository @Inject constructor(
    private val preferences: RxSharedPreferences,
    private val resources: Resources,
    private val notificationManager: NotificationManager
) : UpdateRepository {

    override fun isUpdateAvailable(): Observable<Boolean> {
        // No work to do.
        return Observable.just(false)
    }

    override fun performUpdate(): Completable {
        // No work to do.
        return Completable.complete()
    }

    override fun hasDoneFirstTimeSetup(): Observable<Boolean> {
        return preferences.getBoolean(KEY_NEWS_NOTIFCATIONS_SETUP).asObservable()
    }

    override fun performFirstTimeSetup(): Completable {
        return hasDoneFirstTimeSetup()
            .first(false)
            .flatMapCompletable {  doneSetup ->
                if (!doneSetup) {
                    Completable.fromCallable {
                        setupNotificationChannel()
                        setupNewsNotifications()
                        setupPatchNotifications()
                    }
                } else {
                    Completable.complete()
                }
            }
    }

    private fun setupNewsNotifications() {
        val language = Locale.getDefault().language
        val newsLanguage = resources.getStringArray(R.array.locales_news).firstOrNull { it.contains(language) } ?: "en"
        preferences.getString(resources.getString(R.string.pref_news_notifications_key)).set(newsLanguage)
        preferences.getBoolean(KEY_NEWS_NOTIFCATIONS_SETUP).set(true)

        unsubscribeFromAllNews(resources)

        val topic = "news-$newsLanguage"
        Timber.i("Subscribing to $topic")
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { Timber.i("Subscribed to $topic") }
        if (BuildConfig.DEBUG) {
            Timber.i("Subscribing to $DEBUG_NEWS_TOPIC")
            FirebaseMessaging.getInstance().subscribeToTopic(DEBUG_NEWS_TOPIC)
                .addOnCompleteListener { Timber.i("Subscribed to $DEBUG_NEWS_TOPIC") }
        }
    }

    private fun unsubscribeFromAllNews(resources: Resources) {
        for (key in resources.getStringArray(R.array.locales_news)) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("news-$key")
        }
        if (BuildConfig.DEBUG) {
            Timber.i("Unsubscribing from $DEBUG_NEWS_TOPIC")
            FirebaseMessaging.getInstance().unsubscribeFromTopic(DEBUG_NEWS_TOPIC)
        }
    }

    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = resources.getString(R.string.notification_channel_id)
            val channelName = resources.getString(R.string.notification_channel_name)
            notificationManager.createNotificationChannel(NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW))
        }
    }

    private fun setupPatchNotifications() {
        val topic = "patch-" + BuildConfig.CARD_DATA_VERSION
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
        val key = resources.getString(R.string.pref_patch_notifications_topic_key)
        preferences.getString(key).set(topic)
    }

    private companion object {
        const val KEY_NEWS_NOTIFCATIONS_SETUP = "com.jamieadkins.gwent.notifications.news.setup"
        const val DEBUG_NEWS_TOPIC = "news-debug"
    }
}