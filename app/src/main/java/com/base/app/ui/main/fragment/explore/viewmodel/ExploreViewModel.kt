package com.base.app.ui.main.fragment.explore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/28/2023
 */
@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: UserRepository
) : BaseViewModel() {

    private var _nearByUserResponse: MutableLiveData<List<DatingUser>> = MutableLiveData()
    val nearByUserResponse: LiveData<List<DatingUser>>
        get() = _nearByUserResponse

    fun getAllNearByUsers(lat: Double, lng: Double, limit: Int) {
        viewModelScope.launch {
            val result = repository.getAllNearbyUsers(firebaseUser?.uid.toString(), lat, lng, limit)
            _nearByUserResponse.value = result
        }
    }
}