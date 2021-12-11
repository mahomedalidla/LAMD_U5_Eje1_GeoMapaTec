package mx.edu.tepic.lamd_u5_eje1_geomapatec

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main_fotos.*
import mx.edu.tepic.lamd_u5_eje1_geomapatec.databinding.ActivityMainBinding
import mx.edu.tepic.lamd_u5_eje1_geomapatec.databinding.ActivityMainFotosBinding
import java.io.File

class MainActivityFotos : AppCompatActivity() {
    lateinit var binding: ActivityMainFotosBinding

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMainFotosBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
       /*
        binding = ActivityMainFotosBinding.inflate(LayoutInflater.from(this))
        setContentView(R.layout.activity_main_fotos)

        */

/*
        var nombre = ""
        var extra = intent.extras
        nombre=extra!!.get("data").toString()

        //Toast.makeText(this,"$nombre",Toast.LENGTH_LONG).show()

        txtetiqueta.setText("Baca".toString())

        Toast.makeText(this,"$txtetiqueta",Toast.LENGTH_LONG).show()
*/
        binding.btnsearch.setOnClickListener {

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Cargando Imagen")
            progressDialog.setCancelable(false)
            progressDialog.show()

            Toast.makeText(this,"$txtbuscarnombre",Toast.LENGTH_LONG).show()

            //Pruebas
            val imageNombre = binding.txtbuscarnombre.text.toString()
            val storageRef = FirebaseStorage.getInstance().reference.child("ImagenesLoma/$imageNombre.jpg")

            val localfile = File.createTempFile("tempImage", "jpg")
            storageRef.getFile(localfile).addOnSuccessListener {

                if (progressDialog.isShowing)
                    progressDialog.dismiss()

                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.imageView.setImageBitmap(bitmap)

            }.addOnFailureListener {

                if (progressDialog.isShowing)
                    progressDialog.dismiss()
                Toast.makeText(this, "Fallo la carga de imagen", Toast.LENGTH_LONG).show()


            }
        }


    }
}