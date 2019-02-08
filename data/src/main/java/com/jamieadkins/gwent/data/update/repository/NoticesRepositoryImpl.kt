package com.jamieadkins.gwent.data.update.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.reflect.TypeToken
import com.jamieadkins.gwent.data.update.model.FirebaseNotice
import com.jamieadkins.gwent.domain.update.model.Notice
import com.jamieadkins.gwent.domain.update.repository.NoticesRepository
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class NoticesRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) : NoticesRepository {

    override fun getNotices(): Observable<List<Notice>> {
        return Observable.create<List<Notice>> { emitter ->
            val noticesRef = firestore.collection("notices")
                .document("en")
                .collection("notices")

            val listener = noticesRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    emitter.onError(e)
                }

                val notices = snapshot?.documents?.mapNotNull { doc ->
                    val data = doc.toObject(FirebaseNotice::class.java)
                    data?.let {
                        Notice(
                            it.id,
                            it.title,
                            it.body,
                            it.enabled
                        )
                    }
                }

                emitter.onNext(notices ?: emptyList())
            }

            emitter.setCancellable { listener.remove() }
        }
            .doOnError { Timber.e(it) }
            .onErrorReturnItem(emptyList())
    }

    inline fun <reified T> genericType() = object : TypeToken<T>() {}.type
}