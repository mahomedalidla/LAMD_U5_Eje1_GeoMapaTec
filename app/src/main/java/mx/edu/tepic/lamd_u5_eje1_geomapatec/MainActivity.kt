package mx.edu.tepic.lamd_u5_eje1_geomapatec


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_main.*
import mx.edu.tepic.lamd_u5_eje1_geomapatec.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var baseRemota = FirebaseFirestore.getInstance()
    var posicion = ArrayList<Data>()
    var dataLista = ArrayList<String>()
    var listaID = ArrayList<String>()
    lateinit var locacion: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }

        //Metodo para cargar lista con click
        cargarLista()

        //Oyente
        locacion = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var oyente = Oyente(this)
        locacion.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 0,
            01f, oyente
        )
    }


    //Cargar lista de ubicaciones desde BD
    fun cargarLista() {
        baseRemota.collection("tecnologico")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    mensaje("Error: " + firebaseFirestoreException.message)
                    return@addSnapshotListener
                }//if
                var resultado = ""
                var listaCadena = ""
                posicion.clear()
                dataLista.clear()
                listaID.clear()
                for (document in querySnapshot!!) {
                    var data = Data()
                    data.nombre = document.getString("nombre").toString()
                    data.posicion1 = document.getGeoPoint("posicion1")!!
                    data.posicion2 = document.getGeoPoint("posicion2")!!

                    resultado += data.toString() + "\n" + "\n"
                    listaCadena = data.toString()
                    posicion.add(data)
                    dataLista.add(listaCadena)
                    listaID.add(document.id)
                }//for
                if (dataLista.size == 0) {
                    dataLista.add("No hay data")
                }//if
                var adaptador =
                    ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataLista)
                lista.adapter = adaptador
            }//add snap

        lista.setOnItemClickListener { parent, view, position, id ->
            if (listaID.size == 0) {
                return@setOnItemClickListener
            }//if
            AlertDialog.Builder(this).setTitle("Atención")
                .setMessage("¿Qué desea hacer con\n${dataLista[position]}?")
                .setPositiveButton("Ver en el mapa") { d, w ->
                    abrirMapa(listaID[position])
                }
                .setNeutralButton("Ver imagen") { d, w ->
                    abrirImg(listaID[position])
                }
                .setNegativeButton("Cancelar") { d, w -> }
                .show()
        }//lista
    }//cargar Lista

    //*******************Funciones Extras************************

    fun mensaje(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    fun abrirMapa(idLugar: String) {
        baseRemota.collection("tecnologico")
            .document(idLugar)
            .get()
            .addOnSuccessListener {
                var v = Intent(this, MapsActivity::class.java)
                v.putExtra("id", idLugar)
                v.putExtra("data", it.getString("nombre"))
                startActivity(v)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error no hay conexión de red", Toast.LENGTH_LONG)
                    .show()
            }
    }


    fun abrirImg(idLugar: String) {
        baseRemota.collection("tecnologico")
            .document(idLugar)
            .get()
            .addOnSuccessListener {
                var v = Intent(this, MainActivityFotos::class.java)
                v.putExtra("id", idLugar)
                v.putExtra("data", it.getString("nombre"))
                startActivity(v)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error no hay conexión de red", Toast.LENGTH_LONG)
                    .show()
            }
    }

    @SuppressLint("MissingPermission")
    private fun miUbicacion() {
        LocationServices.getFusedLocationProviderClient(this)
            .lastLocation.addOnSuccessListener {
                var geoPosicion = GeoPoint(it.latitude, it.longitude)
                textView2.setText("${it.latitude}, ${it.longitude}")
                for (item in posicion) {
                    if (item.estoyEn(geoPosicion)) {
                        AlertDialog.Builder(this)
                            .setMessage("Usted se encuentra en: " + item.nombre)
                            .setTitle("ATENCION")
                            .setPositiveButton("OK") { p, q -> }
                            .show()
                    }//if
                }//for
            }//success
            .addOnFailureListener {
                textView2.setText("ERROR AL OBTENER UBICACIÓN")
            }//fail
    }
}//Class


class Oyente(puntero: MainActivity) : LocationListener {
    var p = puntero

    override fun onLocationChanged(location: Location) {
        p.textView2.setText("${location.latitude}, ${location.longitude}")
        var geoposiciongps = GeoPoint(location.latitude, location.longitude)


        for (item in p.posicion) {

            if (item.estoyEn(geoposiciongps)) {
                p.textView3.setText("${item.nombre}")
            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

}