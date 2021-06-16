package com.marknkamau.justjava.ui.addAddress

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.marknjunge.core.data.model.Address
import com.marknkamau.justjava.R
import com.marknkamau.justjava.databinding.ActivityAddAddressBinding
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.utils.getStatusBarHeight
import com.marknkamau.justjava.utils.trimmedText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class AddAddressActivity : BaseActivity(), OnMapReadyCallback {

    companion object {
        const val ADDRESS_KEY = "address"
        private const val PERMISSIONS_REQUEST = 99
    }

    private lateinit var binding: ActivityAddAddressBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var target: LatLng
    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Address"

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val placesAutoComplete =
            supportFragmentManager.findFragmentById(R.id.placeAutoCompleteFragment) as AutocompleteSupportFragment

        val locationBias = RectangularBounds.newInstance(LatLng(-1.451148, 36.592911), LatLng(-1.147189, 37.108095))
        placesAutoComplete.setLocationBias(locationBias)
        placesAutoComplete.setPlaceFields(listOf(Place.Field.LAT_LNG, Place.Field.NAME))
        placesAutoComplete.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng!!
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
                binding.etAddress.setText(place.name)
                target = latLng
            }

            override fun onError(status: Status) {
                Timber.d(status.statusMessage)
            }
        })

        val statusBarHeight = getStatusBarHeight()
        binding.placeAutoCompleteCard.layoutParams =
            (binding.placeAutoCompleteCard.layoutParams as ViewGroup.MarginLayoutParams).apply {
                topMargin += statusBarHeight
            }

        binding.btnAddAddress.setOnClickListener {
            if (isValid()) {
                val deliveryInstructions = if (binding.etDeliveryInstructions.trimmedText.isEmpty()) null else {
                    binding.etDeliveryInstructions.trimmedText
                }
                val address = Address(0L, binding.etAddress.trimmedText, deliveryInstructions, target.asString())
                val intent = Intent()
                intent.putExtra(ADDRESS_KEY, address)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        val nrb = LatLng(-1.286481, 36.817297)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nrb, 14f))

        googleMap.setOnCameraIdleListener {
            target = googleMap.cameraPosition.target
        }

        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            getLastLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.permissionsGranted()) {
                getLastLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        uiCoroutineScope.launch {
            val location: Location? = fusedLocationClient.lastLocation.await()
            location?.let {
                val userLoc = LatLng(it.latitude, it.longitude)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 14f))
            }
        }
    }

    private fun isValid(): Boolean {
        var valid = true

        if (binding.etAddress.trimmedText.isEmpty()) {
            binding.tilAddress.error = "Required"
            valid = false
        }

        return valid
    }

    private fun LatLng.asString() = "$latitude,$longitude"

    private fun Context.hasPermission(permission: String) =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private fun IntArray.permissionsGranted() = this.isNotEmpty() && this[0] == PackageManager.PERMISSION_GRANTED
}
