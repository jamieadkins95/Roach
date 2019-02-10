package com.jamieadkins.gwent.data.update.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.jamieadkins.gwent.data.BuildConfig
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class PatchRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    fun getLatestPatchId(): Single<String> {
        val patchRef = firestore.collection("patch").document(BuildConfig.CARD_DATA_VERSION)
        return Single.create<String> { emitter ->
            patchRef.get()
                .addOnSuccessListener { document ->
                    val patch = document.data?.get("patch")
                    if (patch != null) {
                        emitter.onSuccess(patch.toString())
                    } else {
                        emitter.onError(NullPointerException("patch is null"))
                    }
                }
                .addOnFailureListener { exception -> emitter.onError(exception) }
        }
            .doOnError { Timber.e(it) }
    }
}