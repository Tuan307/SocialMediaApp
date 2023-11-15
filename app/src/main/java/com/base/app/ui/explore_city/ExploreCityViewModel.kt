package com.base.app.ui.explore_city

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.city.CityModel
import com.base.app.data.repositories.city.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 11/9/2023
 */
@HiltViewModel
class ExploreCityViewModel @Inject constructor(
    private val cityRepository: CityRepository
) : BaseViewModel() {
    private var _allCityResponse: MutableLiveData<List<CityModel>> = MutableLiveData()
    val allCityResponse: LiveData<List<CityModel>>
        get() = _allCityResponse

    private var _searchCityResponse: MutableLiveData<List<CityModel>> = MutableLiveData()
    val searchCityResponse: LiveData<List<CityModel>>
        get() = _searchCityResponse

    fun getAllCities() {
        viewModelScope.launch(handler) {
            val result = cityRepository.getAllCities()
            _allCityResponse.value = result.data.orEmpty()
        }
    }

    fun searchForCity(keyword: String) {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val result = cityRepository.searchForCity(keyword)
            _allCityResponse.value = result.data.orEmpty()
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                registerJobFinish()
            }, 1200)
        }
    }
}