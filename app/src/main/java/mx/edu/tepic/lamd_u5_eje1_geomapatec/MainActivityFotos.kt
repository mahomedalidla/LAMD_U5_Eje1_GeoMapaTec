package mx.edu.tepic.lamd_u5_eje1_geomapatec

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main_fotos.*
import mx.edu.tepic.lamd_u5_eje1_geomapatec.databinding.ActivityMainFotosBinding
import java.io.File

class MainActivityFotos : AppCompatActivity() {
    lateinit var binding: ActivityMainFotosBinding

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMainFotosBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)


        var nombre = ""
        var extra = intent.extras
        nombre=extra!!.get("data").toString()

        txtbuscarnombre.setEnabled(false);

        btnregresar.setOnClickListener {
            finish()
        }

        binding.btnsearch.setOnClickListener {
            txtbuscarnombre.setText(nombre.replace(" ","").toString())
            txtetiqueta.setText("Galeria de: "+nombre)
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Cargando Imagen")
            progressDialog.setCancelable(false)
            progressDialog.show()


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

            //img1
            val imageNombre1 = binding.txtbuscarnombre.text.toString()+1
            val storageRef1 = FirebaseStorage.getInstance().reference.child("ImagenesLoma/$imageNombre1.jpg")

            val localfile1 = File.createTempFile("tempImage", "jpg")
            storageRef1.getFile(localfile1).addOnSuccessListener {

                if (progressDialog.isShowing)
                    progressDialog.dismiss()

                val bitmap = BitmapFactory.decodeFile(localfile1.absolutePath)

                binding.imageView2.setImageBitmap(bitmap)


            }.addOnFailureListener {

                if (progressDialog.isShowing)
                    progressDialog.dismiss()
                Toast.makeText(this, "Fallo la carga de imagen", Toast.LENGTH_LONG).show()


            }
            ///img3
            val imageNombre2 = binding.txtbuscarnombre.text.toString()+2
            val storageRef2 = FirebaseStorage.getInstance().reference.child("ImagenesLoma/$imageNombre2.jpg")

            val localfile2 = File.createTempFile("tempImage", "jpg")
            storageRef2.getFile(localfile2).addOnSuccessListener {

                if (progressDialog.isShowing)
                    progressDialog.dismiss()

                val bitmap = BitmapFactory.decodeFile(localfile2.absolutePath)

                binding.imageView3.setImageBitmap(bitmap)


            }.addOnFailureListener {

                if (progressDialog.isShowing)
                    progressDialog.dismiss()
                Toast.makeText(this, "Fallo la carga de imagen", Toast.LENGTH_LONG).show()


            }
        }
        btnsearch.performClick()


    }
}