package com.sun.android.ex22.data.repository.source

interface OnResultListener<T> {
    fun onSuccess(data: T)
    fun onError(exception: String?)
}
