package marcos.lopez.appcrudsmarcos

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import modelo.claseConexion
import java.util.UUID

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1- Mando a llamar a todos los elementos de la vista
        val txtTicketTittle = findViewById<TextView>(R.id.txtTicketTittle)
        val txtTicketDescription = findViewById<TextView>(R.id.txtTicketDescription)
        val txtTicketAuthor = findViewById<TextView>(R.id.txtTicketAuthor)
        val txtAuthorEmail = findViewById<TextView>(R.id.txtAuthorEmail)
        val txtInitiationDate = findViewById<TextView>(R.id.txtInitiationDate)
        val txtFinalizationDate = findViewById<TextView>(R.id.txtFinalizationDate)
        val btnSendTicket = findViewById<Button>(R.id.btnSendTicket)

        //2- Programo el boton de enviar ticket
        btnSendTicket.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                //3- Creo un objeto de la clase conexion

                val objConexion = claseConexion().cadenaConexion()

                //4- Creo una variable que contenga un prepare statement

                val addTicket = objConexion?.prepareStatement("inset into tbTickets (numeroTicket, tituloTicket, descripcionTicket, Autor, correo_Autor, FechaCreacionTicket, EstadoTicket,  FechaFinTicket) values (?,?,?,?,?,?,?,?)")!!
                addTicket.setString(1, UUID.randomUUID().toString())
                addTicket.setString(2,txtTicketTittle.text.toString())
                addTicket.setString(3, txtTicketDescription.text.toString())
                addTicket.setString(4, txtTicketAuthor.text.toString())
                addTicket.setString(5, txtAuthorEmail.text.toString())
                addTicket.setString(6, txtInitiationDate.text.toString())
                addTicket.setString(7, "Activo")
                addTicket.setString(8, txtFinalizationDate.text.toString())
                addTicket.executeUpdate()
            }
        }
    }
}