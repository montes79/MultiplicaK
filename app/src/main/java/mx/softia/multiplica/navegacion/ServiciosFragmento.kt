package mx.softia.multiplica.navegacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import mx.softia.multiplica.R
import mx.softia.multiplica.databinding.FragmentServiciosBinding

class ServiciosFragmento : Fragment() {


    private var _binding: FragmentServiciosBinding? = null
    lateinit var correo:String
    lateinit var nombreCompleto:String
    lateinit var idusuarioFBA:String
    lateinit var textViewTitulo: TextView
    lateinit var textViewNombre: TextView
    lateinit var textViewCorreo: TextView


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentServiciosBinding.inflate(inflater, container, false)
        val root: View = binding.root
        activity?.title=getString(R.string.titulo_servicios)
        textViewTitulo= binding.txtEncabezado
        textViewNombre= binding.txtNombre
        textViewCorreo= binding.txtCorreo
        comprobarSiExisteSesionUsuario()
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}