package com.base.app.ui.splash

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Html
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.data.prefs.AppPreferencesHelper
import com.base.app.databinding.ActivityWelcomeBinding
import com.base.app.ui.login.LoginActivity
import com.base.app.ui.main.MainActivity
import com.base.app.ui.register.RegisterActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {
    private val REQUEST_FINE_LOCATION = 101
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel by viewModels<WelcomeViewModel>()
    override fun getContentLayout(): Int {
        return R.layout.activity_welcome
    }

    override fun initView() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@WelcomeActivity)
        getCurrentLocation()
        binding.apply {
            txtStartRegister.text = Html.fromHtml(getString(R.string.str_start_register))
        }
    }

    override fun initListener() {
        binding.apply {
            btnLogin.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
                finish()
            }
            txtStartRegister.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, RegisterActivity::class.java))
                finish()
            }
        }
    }

    override fun observerLiveData() = with(viewModel) {
        checkUserResponse.observe(this@WelcomeActivity) {
            if (it) {
                viewModel.getUserProfile()
            }
        }
        userResponse.observe(this@WelcomeActivity) {
            if (it != null) {
                val realSave = AppPreferencesHelper(this@WelcomeActivity)
                realSave.save("lat", it.latitude.toString())
                realSave.save("lng", it.longitude.toString())
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        startActivity(Intent(this@WelcomeActivity, ChooseInterestActivity::class.java))
                        finish()
                    },
                    1000
                )
            }
        }
    }

    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this@WelcomeActivity) { task ->
                    val location: Location? = task.result
                    Log.d(
                        "CheckLocation",
                        "${location?.latitude.toString()} and ${location?.longitude.toString()}"
                    )
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
        viewModel.checkUser()
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this@WelcomeActivity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this@WelcomeActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@WelcomeActivity, arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_FINE_LOCATION
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_FINE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast(applicationContext, "Granted")
                getCurrentLocation()
            } else {
                showToast(applicationContext, "Declined")
            }
        }
    }
}