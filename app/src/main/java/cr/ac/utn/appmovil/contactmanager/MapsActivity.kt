package cr.ac.utn.appmovil.contactmanager

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
//import cr.ac.utn.appmovil.contactmanager.databinding.ActivityMapsBinding

class MapsActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val carrizal = LatLng(9.979394, -84.727565)
        val newyork = LatLng(40.762718, -73.980307)
        val radius: Double = 5000f.toDouble()
        mMap.addMarker(MarkerOptions().position(carrizal).title("Marker in Barranca"))
        mMap.addMarker(MarkerOptions().position(newyork).title("Marker in New York"))
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

    }
}