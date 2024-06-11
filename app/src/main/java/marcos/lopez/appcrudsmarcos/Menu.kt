package marcos.lopez.appcrudsmarcos

import RecyclerViewHelpers.Adaptador
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.claseConexion
import modelo.dataClassTickets
import java.util.UUID
import java.util.Calendar

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
        val txtTicketTittle = findViewById<EditText>(R.id.txtTicketTittle)
        val txtTicketDescription = findViewById<EditText>(R.id.txtTicketDescription)
        val txtTicketAuthor = findViewById<EditText>(R.id.txtTicketAuthor)
        val txtAuthorEmail = findViewById<EditText>(R.id.txtAuthorEmail)
        val txtInitiationDate = findViewById<EditText>(R.id.txtInitiationDate)
        val txtFinalizationDate = findViewById<EditText>(R.id.txtFinalizationDate)
        val btnSendTicket = findViewById<Button>(R.id.btnSendTicket)
        val rcvTickets = findViewById<RecyclerView>(R.id.rcvTickets)


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

            rcvTickets.layoutManager = LinearLayoutManager(this)

            //Mostrar datos
            fun obtenerDatos(): List<dataClassTickets>{
            val objConexion = claseConexion().cadenaConexion()

            val statement =objConexion?.createStatement()
            val resulSet = statement?.executeQuery("select * from tbTickets")!!

            val tickets = mutableListOf<dataClassTickets>()


            while (resulSet.next()){
            val numeroTicket = resulSet.getString("NumeroTicket")
            val tituloTicket = resulSet.getString("TituloTicket")
            val descripcionTicket = resulSet.getString("descripcionTicket")
            val autor = resulSet.getString("autor")
            val correoAutor = resulSet.getString("correoAutor")
            val fechaCreacion = resulSet.getString("fechaCreacion")
            val estadoTicket = resulSet.getString("estadoTicket")
            val fechaFin = resulSet.getString("fechaFin")

                val ticket =dataClassTickets(numeroTicket, tituloTicket, descripcionTicket, autor, correoAutor, fechaCreacion, estadoTicket, fechaFin)
                tickets.add(ticket)
            }
            return tickets
            }
            //Asignar el adaptador al recyclerView
            CoroutineScope(Dispatchers.IO).launch {
                val bdTickets = obtenerDatos()
                withContext(Dispatchers.Main){
                    val adapter = Adaptador(bdTickets)
                    rcvTickets.adapter =adapter
                }
            }
        }
    }


}