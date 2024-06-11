package marcos.lopez.appcrudsmarcos

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import modelo.claseConexion
import java.util.UUID
import java.util.Calendar

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtTicketCard)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1- Mando a llamar a todos los elementos de la vista
        val txtTicketTittle = findViewById<EditText>(R.id.txtTicketTittle)
        val txtTicketDescription = findViewById<EditText>(R.id.txtTicketDescription)
        val txtTicketAuthor = findViewById<EditText>(R.id.txtTicketAuthor)
        val txtAuthorEmail = findViewById<EditText>(R.id.txtAuthorEmail)
        val txtInitiationDate = findViewById<EditText>(R.id.txtInitiationDate)
        val txtFinalizationDate = findViewById<EditText>(R.id.txtFinalizationDate)
        val btnSendTicket = findViewById<Button>(R.id.btnSendTicket)


        txtInitiationDate.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada =
                        "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    txtInitiationDate.setText(fechaSeleccionada)
                },
                anio, mes, dia
            )
            datePickerDialog.show()
        }


        txtFinalizationDate.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada =
                        "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    txtFinalizationDate.setText(fechaSeleccionada)
                },
                anio, mes, dia
            )
            datePickerDialog.show()
        }

        //2- Programo el boton de enviar ticket
        btnSendTicket.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                //3- Creo un objeto de la clase conexion

                val objConexion = claseConexion().cadenaConexion()

                //4- Creo una variable que contenga un prepare statement

                val addTicket = objConexion?.prepareStatement("insert into tbTickets (numeroTicket, tituloTicket, descripcionTicket, Autor, correoAutor, FechaCreacion, EstadoTicket,FechaFin) values (?,?,?,?,?,?,?,?)")!!
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