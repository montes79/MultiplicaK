package mx.softia.multiplica


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.*
import mx.softia.multiplica.Actividades.MenuNavegacion
import java.lang.Exception

class SesionActividad : AppCompatActivity() {

    private var instanciaFBA=FirebaseAuth.getInstance()
    private var usuarioFBA: FirebaseUser? =null

    private val analisis=FirebaseAnalytics.getInstance(this)


    private var btnIngresar:Button? = null
    private var btnRegistraVista:Button? = null

    private lateinit var contexto: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_sesion)
        setTheme(R.style.Theme_Design)
        title=getString(R.string.titulo_pantalla_ingreso)
        usuarioFBA=null
        contexto=this

        val bundle=Bundle()
        bundle.putString("mensaje","Integración de Firebase :) completa")
        analisis.logEvent("PantallaInicial",bundle) //mas tarde vemos el dato en analytics

        btnIngresar=findViewById(R.id.btnIngresar)
        btnRegistraVista=findViewById(R.id.btnRegistraVista)
        ocultarFormularios(false,true)

        preparaListenerFormularioInicioSesion()
        preparaListenerFormularioRegistro()
        comprobarSiExisteSesionUsuario()




    }



    private fun ocultarFormularios(frmInicioSesion:Boolean, frmRegistroUsuario: Boolean) {

        findViewById<LinearLayout>(R.id.frmInicioSesion).visibility=View.VISIBLE
        findViewById<LinearLayout>(R.id.frmRegistro).visibility=View.VISIBLE

        if(frmInicioSesion)
            findViewById<LinearLayout>(R.id.frmInicioSesion).visibility=View.GONE

        if(frmRegistroUsuario)
            findViewById<LinearLayout>(R.id.frmRegistro).visibility=View.GONE
    }



    private fun preparaListenerFormularioInicioSesion(){

        btnIngresar?.setOnClickListener{ vistaBtn->
            val correo=findViewById<EditText>(R.id.editCorreo).text.trim()
            val contrasena=findViewById<EditText>(R.id.editContrasena).text.trim()
            val bandera1=correo.isNotEmpty()
            val bandera2=contrasena.isNotEmpty()

            if(!bandera1){ //Correo
                notificarUsuario(getString(R.string.texto_dialogo_title),getString(R.string.text_correo_vacia))
                return@setOnClickListener
            }

            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(correo.toString()).matches()) { //Patron de correo valido
                notificarUsuario(getString(R.string.texto_dialogo_title),getString(R.string.texto_dialogo_error_correo))
                return@setOnClickListener
            }

            if(!bandera2){ //Contraseña
                notificarUsuario(getString(R.string.texto_dialogo_title),getString(R.string.text_contrasena_vacia))
                return@setOnClickListener
            }


                try {
                    instanciaFBA.signInWithEmailAndPassword(correo.toString(),contrasena.toString()).addOnCompleteListener{
                        if(it.isSuccessful) {
                            usuarioFBA=instanciaFBA.currentUser
                            //TODO Preparar la navegacion hacia donde esten los pedidos y/o servicios
                            println(getString(R.string.texto_debug_sout)+it.result?.toString()+" ${it.isSuccessful} ")
                            //notificarUsuario(getString(R.string.texto_ok_autenticacion),getString(R.string.texto_dialogo_sesion_OK))
                            notificaSnack(getString(R.string.texto_ok_autenticacion),getString(R.string.texto_dialogo_sesion_OK),vistaBtn)
                            navegarMenuPantallas()
                        }
                        else {
                            val mensaje=it.exception?.message?:getString(R.string.texto_sin_mensaje)
                            notificarUsuario(getString(R.string.texto_error_autenticacion), mensaje)
                        }
                    }//OnCompleteListener
                }
                catch(e: Exception){
                    notificarUsuario(getString(R.string.texto_error_autenticacion), e.toString())
                }




        }

        btnRegistraVista?.setOnClickListener(){
            ocultarFormularios(true,false)
        }


    }


    private fun preparaListenerFormularioRegistro() {

        val btnRegistrar=findViewById<Button>(R.id.btnRegistrar)
        val btnCancelar=findViewById<Button>(R.id.btnCancelar)

        btnRegistrar?.setOnClickListener {btnIt->
            val nombre = findViewById<EditText>(R.id.editNombre).text.trim()
            val apellido = findViewById<EditText>(R.id.editApellido).text.trim()
            val correo = findViewById<EditText>(R.id.editCorreoR).text.trim()
            val contrasena = findViewById<EditText>(R.id.editContrasena1).text.trim()
            val confirmarContrasena = findViewById<EditText>(R.id.editContrasena2).text.trim()

            val nombreCompleto=nombre.toString().plus(" ").plus(apellido)


            val bandera1=nombre.isNotEmpty()
            val bandera2=apellido.isNotEmpty()
            val bandera3=correo.isNotEmpty()
            val bandera4=contrasena.isNotEmpty()
            val bandera5= (contrasena == confirmarContrasena)

            if(!bandera1){ //Nombre
                notificarUsuario(getString(R.string.texto_dialogo_title),getString(R.string.texto_dialogo_error_nombre))
                return@setOnClickListener
            }

            if(!bandera2){ //Apellido
                notificarUsuario(getString(R.string.texto_dialogo_title),getString(R.string.texto_dialogo_error_apellido))
                return@setOnClickListener
            }

            if(!bandera3) { //Correo
                notificarUsuario(getString(R.string.texto_dialogo_title),getString(R.string.text_correo_vacia))
                return@setOnClickListener
            }

            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(correo.toString()).matches()) { //Patron de correo valido
                notificarUsuario(getString(R.string.texto_dialogo_title),getString(R.string.texto_dialogo_error_correo))
                return@setOnClickListener
            }


            if(!bandera4){ //Contraseña vacia
                notificarUsuario(getString(R.string.texto_dialogo_title),getString(R.string.text_contrasena_vacia))
                return@setOnClickListener
            }

            if(!bandera5){ //Sin coincidencias de las contraseñas
                notificarUsuario(getString(R.string.texto_dialogo_title),getString(R.string.texto_dialogo_error_coincidencia_contrasena))
                return@setOnClickListener
            }

                try {

                    instanciaFBA.createUserWithEmailAndPassword(
                        correo.toString(),
                        contrasena.toString()
                    ).addOnCompleteListener { it1 ->

                        if (it1.isSuccessful) {

                            println(getString(R.string.texto_debug_sout) + it1.result?.toString() + " ${it1.isSuccessful} ")
                            usuarioFBA=instanciaFBA.currentUser
                            val profileUpdates: UserProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(nombreCompleto).build()
                             // TODO faltaria agregar la foto, si le damos desde el principio la opcion de tomarse una foto

                            usuarioFBA?.updateProfile(profileUpdates)?.addOnCompleteListener{it2 ->
                                if (it2.isSuccessful) {
                                    //TODO Preparar la navegacion, inicialmente avisar del registro OK
                                    //println(getString(R.string.texto_debug_sout) + it2.result?.toString() + " ${it2.isSuccessful} ")
                                    //notificarUsuario(getString(R.string.texto_ok_autenticacion), getString(R.string.texto_dialogo_registro_OK))
                                    notificaSnack(getString(R.string.texto_ok_autenticacion), getString(R.string.texto_dialogo_registro_OK),btnIt)
                                    navegarMenuPantallas()
                                }
                                else{
                                    val mensaje=it2.exception?.message?:getString(R.string.texto_sin_mensaje)
                                    notificarUsuario(getString(R.string.texto_error_autenticacion), mensaje)
                                }
                            }

                        } else {
                            val mensaje=it1.exception?.message?:getString(R.string.texto_sin_mensaje)
                            notificarUsuario(getString(R.string.texto_error_autenticacion), mensaje)

                        }
                    }//OnCompleteListener
                }  catch(e: Exception){
                    notificarUsuario(getString(R.string.texto_error_autenticacion), e.toString())
                }



        }

        btnCancelar?.setOnClickListener{
            resetFormularios()
            ocultarFormularios(false,true)
        }

    }











    private fun resetFormularios() {
        //Formulario ingreso

        findViewById<EditText>(R.id.editCorreo).text.clear()
        findViewById<EditText>(R.id.editContrasena).text.clear()

        //Formulario registro
        findViewById<EditText>(R.id.editNombre).text.clear()
        findViewById<EditText>(R.id.editApellido).text.clear()
        findViewById<EditText>(R.id.editCorreoR).text.clear()
        findViewById<EditText>(R.id.editContrasena1).text.clear()
        findViewById<EditText>(R.id.editContrasena2).text.clear()
    }




    private fun notificarUsuario(titulo:String, mensaje: String?) {
        val builder=AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setPositiveButton(getString(R.string.texto_dialogo_ok),null)
        val dialogo=builder.create()
        dialogo.show()
    }


    private fun notificaSnack(titulo:String, mensaje: String?,vista:View) {
        val snack = Snackbar.make(contexto,vista,mensaje.toString(),Snackbar.LENGTH_LONG)
        snack.show()
    }


    private fun comprobarSiExisteSesionUsuario(){
        val preferenciasUsuario=getSharedPreferences(getString(R.string.cadena_shared_preferences),
            MODE_PRIVATE)

        val correo=preferenciasUsuario.getString(getString(R.string.parametro_sesion_correo),null)
        val idusuarioFBA=preferenciasUsuario.getString(getString(R.string.parametro_sesion_userid),null)

        if(correo!=null) {
            usuarioFBA = instanciaFBA.currentUser
            if (idusuarioFBA != null && idusuarioFBA==usuarioFBA?.uid) {
                ocultarFormularios(true, true)
                navegarMenuPantallas()
            }
            else{
                ocultarFormularios(false, true)
            }
        }

    }

    private fun navegarMenuPantallas() {

        resetFormularios()
        almacenaSesionUsuario()
        usuarioFBA = instanciaFBA.currentUser
        val intentoNavegacion= Intent(this,MenuNavegacion::class.java).apply {
            putExtra(getString(R.string.parametro_sesion_correo),usuarioFBA?.email)
            putExtra(getString(R.string.parametro_sesion_nombrecompleto),usuarioFBA?.displayName)
            putExtra(getString(R.string.parametro_sesion_userid),usuarioFBA?.uid)
        }

        startActivity(intentoNavegacion)
    }


    private fun almacenaSesionUsuario() {
        val preferenciasUsuario=getSharedPreferences(getString(R.string.cadena_shared_preferences),
            MODE_PRIVATE).edit()

        preferenciasUsuario.putString(getString(R.string.parametro_sesion_correo),usuarioFBA?.email)
        preferenciasUsuario.putString(getString(R.string.parametro_sesion_nombrecompleto),usuarioFBA?.displayName)
        preferenciasUsuario.putString(getString(R.string.parametro_sesion_userid),usuarioFBA?.uid)
        preferenciasUsuario.apply()
    }



    override fun onStart() {
        super.onStart()
        ocultarFormularios(false,true)
    }


    //para anular la pila de navegacion con el backPress ... y caer en Sesion..
    override fun onBackPressed() {}

}