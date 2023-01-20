package com.alvaro.core.domain

sealed class DataState<T> {

    data class Response<T>(val uiComponent: UIComponent) : DataState<T>()

    data class Data<T>(val data: T) : DataState<T>()
    class ResponseError<T> : DataState<T>()
    class ResponseSuccess<T> : DataState<T>()

}
