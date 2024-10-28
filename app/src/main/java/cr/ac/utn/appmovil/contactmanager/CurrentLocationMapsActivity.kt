package cr.ac.utn.appmovil.contactmanager

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.jar.Manifest

class CurrentLocationMapsActivity : AppCompatActivity(), OnMyLocationButtonClickListener, OnMyLocationClickListener, OnMapReadyCallback,
    OnRequestPermissionsResultCallback, OnMarkerClickListener{

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private var permissionDenied = false
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_current_location_maps_activity)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap ?: return
        //mMap.isMyLocationEnabled = true
        googleMap.setOnMyLocationButtonClickListener (this)
        googleMap.setOnMyLocationClickListener(this)
        enableMyLocation()
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation(){
        if (!::mMap.isInitialized) return

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            val carrizal = LatLng(9.979394, -84.727565)
            val newyork = LatLng(40.762718, -73.980307)
            val radius: Double = 5000f.toDouble()
            mMap.addMarker(MarkerOptions().position(carrizal).title("Marker in Barranca")).tag=0
            mMap.addMarker(MarkerOptions().position(newyork).title("Marker in New York")).tag=1
            val circle = CircleOptions()
            circle.center(newyork)
            circle.radius(radius)
            //circle.strokeColor(Color.BLUE)
            //circle.fillColor(Color.RED)
            circle.strokeWidth(2f)
            mMap.addCircle(circle)
            mMap.setTrafficEnabled(true);
            //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newyork, 15f))
            mMap.setIndoorEnabled(true)
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
            mMap.uiSettings.isCompassEnabled= true
            mMap.uiSettings.isZoomControlsEnabled= true
            mMap.uiSettings.isIndoorLevelPickerEnabled= true
            mMap.uiSettings.isMapToolbarEnabled= true
            mMap.setOnMarkerClickListener(this)
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clickled", Toast.LENGTH_LONG).show()
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n${location}", Toast.LENGTH_LONG).show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation()
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        // Retrieve the data from the marker.
        val clickCount = marker.tag as? Int

        // Check if a click count was set, then display the click count.
        clickCount?.let {
            val newClickCount = it + 1
            marker.tag = newClickCount
            Toast.makeText(
                this,
                "${marker.title} has been clicked $newClickCount times.",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false
    }

    /*override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }*/

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    /*private fun showMissingPermissionError() {
        newInstance(true).show(supportFragmentManager, "dialog")
    }*/
}