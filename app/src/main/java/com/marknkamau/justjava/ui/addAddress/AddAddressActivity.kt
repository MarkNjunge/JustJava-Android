package com.marknkamau.justjava.ui.addAddress

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.marknjunge.core.data.model.Address
import com.marknkamau.justjava.R
import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_add_address.*
import timber.log.Timber

class AddAddressActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val ADDRESS_KEY = "address"
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var target: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        supportActionBar?.title = "Add Address"

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val placesAutoComplete = placeAutoCompleteFragment as AutocompleteSupportFragment
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
        googleMap.uiSettings.isMyLocationButtonEnabled = true

        googleMap.setOnCameraIdleListener {
            target = googleMap.cameraPosition.target
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
}
