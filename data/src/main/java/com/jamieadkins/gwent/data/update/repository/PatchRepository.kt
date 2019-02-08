package com.jamieadkins.gwent.data.update.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.jamieadkins.gwent.data.update.model.FirebasePatchResult
import com.nytimes.android.external.store3.base.impl.BarCode
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import com.jamieadkins.gwent.data.*
import timber.log.Timber
import java.lang.NullPointerException
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