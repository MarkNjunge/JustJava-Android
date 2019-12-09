package com.marknkamau.justjava.ui.addAddress

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.marknjunge.core.data.model.Address
import com.marknkamau.justjava.BuildConfig
import com.marknkamau.justjava.R
import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_add_address.*

class AddAddressActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        const val ADDRESS_KEY = "address"
    }

    private lateinit var target: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        Mapbox.getInstance(this, BuildConfig.mapboxToken)
        Mapbox.getTelemetry()?.disableTelemetrySession()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        supportActionBar?.title = "Add Address"

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

        mapView.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS)
        mapboxMap.cameraPosition = CameraPosition.Builder().target(LatLng(-1.286481, 36.817297)).build()
        mapboxMap.addOnCameraIdleListener {
            target = mapboxMap.cameraPosition.target
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    public override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
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
