package mx.softia.multiplica.navegacion

import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import mx.softia.multiplica.Actividades.MenuNavegacion
import mx.softia.multiplica.R
import mx.softia.multiplica.SesionActividad
import mx.softia.multiplica.databinding.FragmentInicioBinding


//import mx.softia.multiplica.Actividades.R
//import mx.softia.multiplica.Actividades.databinding.FragmentHomeBinding

class InicioFragmento : Fragment() {


    private var _binding: FragmentInicioBinding? = null

    private val binding get() = _binding!!

    lateinit var correo:String
    lateinit var nombreCompleto:String
    lateinit var idusuarioFBA:String
    lateinit var textViewTitulo: TextView
    lateinit var textViewNombre: TextView
    lateinit var textViewCorreo: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val root: View = binding.root
        activity?.title=getString(R.string.titulo_bienvenido)

        textViewTitulo= binding.txtEncabezado
        textViewNombre= binding.txtNombre
        textViewCorreo= binding.txtCorreo
        comprobarSiExisteSesionUsuario()

        val btnSalir=binding.btnSalir
        btnSalir.setOnClickListener{
            cuestionarCierreSesion()
        }


        return root
    }




    private fun comprobarSiExisteSesionUsuario() {
        val preferenciasUsuario=activity?.getSharedPreferences(getString(R.string.cadena_shared_preferences),
            AppCompatActivity.MODE_PRIVATE
        )

        correo= preferenciasUsuario?.getString(getString(R.string.parametro_sesion_correo),null).toString()
        nombreCompleto= preferenciasUsuario?.getString(getString(R.string.parametro_sesion_nombrecompleto),null).toString()
        idusuarioFBA= preferenciasUsuario?.getString(getString(R.string.parametro_sesion_userid),null).toString()

        if(nombreCompleto!=null){
            textViewNombre.text=getString(R.string.txt_saludo).plus(getString(R.string.cadenavacia)).plus(nombreCompleto)
            textViewCorreo.text= correo
        }
    }



    private fun cuestionarCierreSesion(){
        lateinit var dialogo: AlertDialog
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.txt_btn_cerrar_sesion))
        builder.setMessage(getString(R.string.txt_confirmar_salida))
        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    destruirSesionLocalFireBase()
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                    dialogo.dismiss()
                }

            }
        }

        builder.setPositiveButton("YES",dialogClickListener)
        builder.setNegativeButton("NO",dialogClickListener)
        dialogo = builder.create()
        dialogo.show()
    }


    private fun  destruirSesionLocalFireBase()
    {
        val preferenciasUsuario=activity?.getSharedPreferences(getString(R.string.cadena_shared_preferences),MODE_PRIVATE)?.edit()
        preferenciasUsuario?.clear()
        preferenciasUsuario?.apply()
        FirebaseAuth.getInstance().signOut()
        val intentoInicial= Intent(context, SesionActividad::class.java).apply {

        }
        startActivity(intentoInicial)

    }



override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}