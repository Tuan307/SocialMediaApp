package com.base.app.ui.main.fragment.explore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.city.CityModel
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.data.repositories.UserRepository
import com.base.app.data.repositories.city.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/28/2023
 */
@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: UserRepository,
    private val cityRepository: CityRepository
) : BaseViewModel() {

    private var _nearByUserResponse: MutableLiveData<List<DatingUser>> = MutableLiveData()
    val nearByUserResponse: LiveData<List<DatingUser>>
        get() = _nearByUserResponse

    private var _allCityResponse: MutableLiveData<List<CityModel>> = MutableLiveData()
    val allCityResponse: LiveData<List<CityModel>>
        get() = _allCityResponse

    fun getAllNearByUsers(lat: Double, lng: Double, limit: Int) {
        viewModelScope.launch {
            val result = repository.getAllNearbyUsers(firebaseUser?.uid.toString(), lat, lng, limit)
            _nearByUserResponse.value = result
        }
    }

    fun getAllCities() {
        viewModelScope.launch {
            val result = cityRepository.getAllCities()
            _allCityResponse.value = result.data.orEmpty()
        }
    }
}