package com.jonasrosendo.tvmaze.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class StateViewModel<T>(initialValue: T) : ViewModel() {
    private val _state = MutableStateFlow(initialValue)
    protected val state = _state

    fun bindState() = state.asStateFlow()
}