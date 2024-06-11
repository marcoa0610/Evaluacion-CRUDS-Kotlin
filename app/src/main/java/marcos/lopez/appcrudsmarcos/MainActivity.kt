package marcos.lopez.appcrudsmarcos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modelo.claseConexion

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtUserLogin = findViewById<TextView>(R.id.txtUserLogin)
        val txtPasswordLogin = findViewById<TextView>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val Menu = Intent(this, Menu::class.java )
            GlobalScope.launch(Dispatchers.IO){
                val objConexion = claseConexion().cadenaConexion()
                val comprobarUsuario = objConexion?.prepareStatement("SELECT * FROM tbUsuarios WHERE Usuario = ? AND Contrasena = ?")!!
                comprobarUsuario.setString(1, txtUserLogin.text.toString())
                comprobarUsuario.setString(2, txtPasswordLogin.text.toString())
                val resultado = comprobarUsuario.executeQuery()
                if (resultado.next()){
                    startActivity(Menu)
                } else{
                    println("Usuario no encontrado, verifique sus credenciales")
                }


            }
        }
        btnRegister.setOnClickListener {
            val Register = Intent (this@MainActivity, activity_register::class.java)
            startActivity(Register)
    }
    }}