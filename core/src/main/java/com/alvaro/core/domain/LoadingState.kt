package com.alvaro.core.domain

sealed class LoadingState{
    object Idle : LoadingState()
    object Loading : LoadingState()
}