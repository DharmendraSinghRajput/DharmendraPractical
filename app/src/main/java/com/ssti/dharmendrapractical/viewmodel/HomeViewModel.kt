package com.ssti.dharmendrapractical.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssti.dharmendrapractical.data.model.CartsResponse
import com.ssti.dharmendrapractical.data.repository.HomeRepository
import com.ssti.dharmendrapractical.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _cartList = MutableStateFlow<Resource<CartsResponse>>(Resource.Idle())
    val cartList: StateFlow<Resource<CartsResponse>> = _cartList

    fun fetchCartList() {
        repository.getCartHomeList()
            .onEach { result -> _cartList.value = result }
            .launchIn(viewModelScope)
    }
}
