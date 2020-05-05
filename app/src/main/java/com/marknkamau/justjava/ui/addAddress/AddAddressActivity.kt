package com.marknkamau.justjava.ui.addAddress

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
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
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_add_address.*
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

    private lateinit var googleMap: GoogleMap
    private lateinit var target: LatLng
    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        supportActionBar?.title = "Add Address"

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val placesAutoComplete = placeAutoCompleteFragment as AutocompleteSupportFragment
        val locationBias = RectangularBounds.newInstance(LatLng(-1.451148, 36.592911), LatLng(-1.147189, 37.108095))
        placesAutoComplete.setLocationBias(locationBias)
        placesAutoComplete.setPlaceFields(listOf(Place.Field.LAT_LNG, Place.Field.NAME))
        placesAutoComplete.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 14f))
                etAddress.setText(place.name)
                target = place.latLng!!
            }

            override fun onError(status: Status) {
                Timber.d(status.statusMessage)
            }
        })

        btnAddAddress.setOnClickListener {
            if (isValid()) {
                val deliveryInstructions =
                    if (etDeliveryInstructions.trimmedText.isEmpty()) null else etDeliveryInstructions.trimmedText
                val address = Address(0L, etAddress.trimmedText, deliveryInstructions, target.asString())
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
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.permissionsGranted()) {
                getLastLocation()
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = true
            }
        }
    }

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

        if (etAddress.trimmedText.isEmpty()) {
            tilAddress.error = "Required"
            valid = false
        }

        return valid
    }

    private fun LatLng.asString() = "$latitude,$longitude"

    private fun Context.hasPermission(permission: String) =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private fun IntArray.permissionsGranted() = this.isNotEmpty() && this[0] == PackageManager.PERMISSION_GRANTED
}
