package mx.softia.multiplica.Actividades

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import mx.softia.multiplica.R
import mx.softia.multiplica.databinding.ActivityMenuNavegacionBinding


class MenuNavegacion : AppCompatActivity() {

    private lateinit var binding: ActivityMenuNavegacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuNavegacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_menu_navegacion)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navegacion_inicio, R.id.navegacion_productos, R.id.navegacion_servicios
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }





    //para anular la pila de navegacion con el backPress ... y caer en Sesion..
    override fun onBackPressed() {}

}