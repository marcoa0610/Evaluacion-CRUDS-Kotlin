package RecyclerViewHelpers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import marcos.lopez.appcrudsmarcos.R
import modelo.claseConexion
import modelo.dataClassTickets

class Adaptador(private var Datos: List<dataClassTickets>) : RecyclerView.Adapter<ViewHolder>() {
    fun actualizarLista(nuevaLista: List<dataClassTickets>) {
        Datos = nuevaLista
        notifyDataSetChanged() // Notificar al adaptador sobre los cambios
    }





    /////////////////// TODO: Eliminar datos
    fun eliminarDatos(tituloTicket: String, posicion: Int){
        //Actualizo la lista de datos y notifico al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            //1- Creamos un objeto de la clase conexion
            val objConexion = claseConexion().cadenaConexion()

            //2- Crear una variable que contenga un PrepareStatement
            val deleteTicket = objConexion?.prepareStatement("delete from tbTickets where tituloTicket = ?")!!
            deleteTicket.setString(1, tituloTicket)
            deleteTicket.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()
        }
        Datos = listaDatos.toList()
        // Notificar al adaptador sobre los cambios
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    //////////////////////TODO: Editar datos
    fun actualizarDato(nuevoTitulo: String, numeroTicket: String){
        GlobalScope.launch(Dispatchers.IO){

            //1- Creo un objeto de la clase de conexion
            val objConexion = claseConexion().cadenaConexion()

            //2- creo una variable que contenga un PrepareStatement
            val updateTicket = objConexion?.prepareStatement("update tbTickets set tituloTicket = ? where numeroTicket = ?")!!
            updateTicket.setString(1, nuevoTitulo)
            updateTicket.setString(2, numeroTicket)
            updateTicket.executeUpdate()


        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_card, parent, false)

        return ViewHolder(vista)
    }


    override fun getItemCount() = Datos.size




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = Datos[position]
        holder.textView.text = ticket.tituloTicket

        //todo: clic al icono de eliminar
        holder.imgBorrar.setOnClickListener {

            //Creamos un Alert Dialog
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar el ticket?")

            //Botones
            builder.setPositiveButton("Si") { dialog, which ->
                eliminarDatos(ticket.tituloTicket, position)
            }

            builder.setNegativeButton("No"){dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

        }

        //Todo: icono de editar
        holder.imgEditar.setOnClickListener{
            //Creamos un Alert Dialog
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Actualizar")
            builder.setMessage("¿Desea actualizar la mascota?")

            //Agregarle un cuadro de texto para
            //que el usuario escriba el nuevo nombre
            val cuadroTexto = EditText(context)
            cuadroTexto.setHint(mascota.tituloTicket)
            builder.setView(cuadroTexto)

            //Botones
            builder.setPositiveButton("Actualizar") { dialog, which ->
                actualizarDato(cuadroTexto.text.toString(), ticket.numeroTicket)
            }

            builder.setNegativeButton("Cancelar"){dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        //Todo: Clic a la card completa
        //Vamos a ir a otra pantalla donde me mostrará todos los datos
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            //Cambiar de pantalla a la pantalla de detalle
            val pantallaDetalle = Intent(context, detalle_mascota::class.java)
            //enviar a la otra pantalla todos mis valores
            pantallaDetalle.putExtra("MascotaUUID", mascota.uuid)
            pantallaDetalle.putExtra("nombre", mascota.nombreMascota)
            pantallaDetalle.putExtra("peso", mascota.peso)
            pantallaDetalle.putExtra("edad", mascota.edad)
            context.startActivity(pantallaDetalle)
        }




    }

}
