package marcos.lopez.appcrudsmarcos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.claseConexion
import java.util.UUID

class activity_register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtUserRegister = findViewById<TextView>(R.id.txtUserRegister)
        val txtPasswordRegister = findViewById<TextView>(R.id.txtPasswordRegister)
        val txtEmailRegister = findViewById<TextView>(R.id.txtEmailRegister)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegisterr)

        btnRegistrarse.setOnClickListener {
            val Login = Intent (this, MainActivity::class.java)
            GlobalScope.launch(Dispatchers.IO){
            val objConexion = claseConexion().cadenaConexion()

            val registrarUsuario = objConexion?.prepareStatement("Insert into tbUsuarios(UUID_Usuario, Usuario, Contrasena, Correo) VALUES (?,?,?,?)")!!
            registrarUsuario.setString(1, UUID.randomUUID().toString())
            registrarUsuario.setString(2, txtUserRegister.text.toString())
            registrarUsuario.setString(3, txtPasswordRegister.text.toString())
            registrarUsuario.setString(4, txtEmailRegister.text.toString())
            registrarUsuario.executeUpdate()
                withContext(Dispatchers.Main){
                    Toast.makeText(this@activity_register, "Usuario creado", Toast.LENGTH_SHORT).show()
                    startActivity(Login)
                }


         }
            }
         }
    }
