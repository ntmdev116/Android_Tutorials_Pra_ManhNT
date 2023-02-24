package com.sun.android.ex13

import android.net.Uri
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object NetworkUtils {
    private val BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?"

    private val QUERY_PARAM = "q"

    private val MAX_RESULTS = "maxResults"

    private val PRINT_TYPE = "printType"

    fun getBookInfo(queryString: String?): String? {
        var urlConnection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        var bookJSONString: String? = null
        try {
            val builtURI: Uri = Uri.parse(BOOK_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, queryString)
                .appendQueryParameter(MAX_RESULTS, "10")
                .appendQueryParameter(PRINT_TYPE, "books")
                .build()

            val requestURL = URL(builtURI.toString())

            urlConnection = requestURL.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connect()

            val inputStream = urlConnection.inputStream

            reader = BufferedReader(InputStreamReader(inputStream))

            val builder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
                builder.append("\n")
            }
            if (builder.isEmpty()) {
                return null
            }

            bookJSONString = builder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            urlConnection?.disconnect()

            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        return bookJSONString
    }
}
