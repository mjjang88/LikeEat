package com.fund.likeeat.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fund.likeeat.data.AppDatabase
import com.fund.likeeat.data.Review
import com.fund.likeeat.utilities.PLACE_DATA_FILENAME
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.coroutineScope

class ReviewTempDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(PLACE_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val reviewType = object : TypeToken<List<Review>>() {}.type
                    val reviewList: List<Review> = Gson().fromJson(jsonReader, reviewType)

                    val database = AppDatabase.getInstance(applicationContext)
                    database.reviewDao().insertAll(reviewList)

                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private val TAG = ReviewTempDatabaseWorker::class.java.simpleName
    }
}