package com.sun.android.ex13

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class FetchBook(private val lifecycleCoroutineScope: LifecycleCoroutineScope) {
    fun fetch(searchString: String, _onPreExecute: () -> Unit, _onPostExecute: (String?, String?) -> Unit) {
        lifecycleCoroutineScope.executeAsyncTask(
            onPreExecute = {},
            doInBackground = { NetworkUtils.getBookInfo(searchString) },
            onPostExecute = { result ->
                try {
                    var title: String? = null
                    var authors: String? = null

                    result?.let {
                        val jsonObject = JSONObject(it)
                        val itemsArray = jsonObject.getJSONArray("items")

                        for (i in 0 until itemsArray.length()) {
                            val book = itemsArray.getJSONObject(i)
                            val volumeInfo = book.optJSONObject(VOLUME_INFO)

                            title = volumeInfo?.getString(TITLE)
                            authors = volumeInfo?.getString(AUTHORS)

                            if (authors != null && title != null) {
                                break
                            }
                        }

                    }

                    _onPostExecute(title, authors)
                } catch (e: Exception) {
                    _onPostExecute(null, null)
                    e.printStackTrace()
                }
            }
        )
    }

    private fun <R> CoroutineScope.executeAsyncTask(
        onPreExecute: () -> Unit,
        doInBackground: () -> R,
        onPostExecute: (R) -> Unit
    ) = launch {
        onPreExecute()
        val result = withContext(Dispatchers.IO) {
            doInBackground()
        }
        onPostExecute(result)
    }

    companion object {
        private val VOLUME_INFO = "volumeInfo"
        private val TITLE = "title"
        private val AUTHORS = "authors"
    }
}

