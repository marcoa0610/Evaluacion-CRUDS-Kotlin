CREATE TABLE tbTickets(
    numeroTicket VARCHAR2 (50),
    tituloTicket VARCHAR2 (30),
    descripcionTicket VARCHAR2 (300),
    autorTicket VARCHAR2 (50),
    emailContactoAutor Varchar2 (50),
    fechaCreacionTicket DATE,
    EstadoTicket Varchar2 (10),
    fechaFinTicket Date
);

Create Table tbUsuarios(
    Usuario varchar2 (50),
    Contrasena varchar2 (265)
);