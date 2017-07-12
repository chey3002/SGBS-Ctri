package logica;

import interfaces.Productos;
import interfaces.ModificacionFactory;
import interfaces.Eliminacion;
import interfaces.Creacion;
import java.io.IOException;
import vistas.Desarrollador;

public class FacadeBD {

    public void CargarArchivo(Object[] args){
        String nombreTabla = (String) args[1];
        CargarArchivo flujo = new CargarArchivo();
        Object[] arg = {args[1], args[2], args[3], args[4], args[5], args[6]};
        flujo.operation(arg);
        Desarrollador.estadoOperacion = "Se ha inclorporado la tabla " + nombreTabla + "";
    }
    
    public void CrearTabla(Object[] args) {
        String nombreTabla = (String) args[1];
        Productos factory = new Tabla();
        Creacion crear = factory.creacionProceso();
        Object[] arg = {args[1], args[2], args[3], args[4],args[5],args[6]};
        crear.operation(arg);
        Desarrollador.estadoOperacion = "Se ha creado la tabla: " + nombreTabla + " con exito";
    }

    public void ModificarTabla(Object[] args) {
        String nombreTabla = (String) args[1];
        
        Productos factory = new Tabla();
        ModificacionFactory modificar = factory.modificacionProceso();
        Object[] arg = {args[1], args[2], args[3]};
        modificar.operation(arg);
        Desarrollador.estadoOperacion = "Se ha modificado la tabla: " + nombreTabla + " con exito";
    }

    public void EliminarTabla(Object[] args) {
        String nombreTabla = (String) args[1];
        Productos factory = new Tabla();
        Eliminacion eliminar = factory.eliminacionProceso();
        Object[] arg = {args[1]};
        eliminar.operation(arg);
        Desarrollador.estadoOperacion = "Se ha eliminado la tabla: " + nombreTabla + " con exito";
    }

    public void CrearRegistro(Object[] args) {
        String nombreTabla = (String) args[1];
        Productos factory = new Registro();
        Creacion crear = factory.creacionProceso();
        Object[] arg = {args[1], args[2]};
        crear.operation(arg);
        Desarrollador.estadoOperacion = "Se agrego el registro a la tabla: " + nombreTabla + " con exito";
    }

    public void ActualizarRegistro(Object[] args) {
        String nombreTabla = (String) args[1];
        Productos factory = new Registro();
        ModificacionFactory modificar = factory.modificacionProceso();
        Object[] arg = {args[1], args[2], args[3], args[4], args[5]};
        System.out.println("Entrara en mod reg");
        modificar.operation(arg);
        Desarrollador.estadoOperacion = "Se ha actualizado un registro de la tabla: " + nombreTabla + " con exito";
    }

    public void EliminarRegistro(Object[] args) {
        String nombreTabla = (String) args[1];
        Productos factory = new Registro();
        Eliminacion eliminar = factory.eliminacionProceso();
        Object[] arg = {args[1], args[2], args[3]};
        eliminar.operation(arg);
        Desarrollador.estadoOperacion = "Se ha eliminado un registro de la tabla: " + nombreTabla + " con exito";
    }

    public void Select(Object[] args) {
        String nombreTabla = (String) args[1];
        Object[] arg = {args[1], args[2], args[3], args[4], args[5], args[6]};
        try { new SeleccionarTabla(arg).Visualizar();
        } catch (IOException ex) { throw new Error(ex.getMessage());}
        Desarrollador.estadoOperacion = "Se ha seleccionado registros de la tabla: " + nombreTabla + " con exito";
    }

    public void Join(Object[] args) {
        String nombreTabla1 = (String) args[1];
        String nombreTabla2 = (String) args[2];
        Object[] arg = {args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]};
        new UnirTablas(arg).Visualizar();
        Desarrollador.estadoOperacion = "Se ha unido las tablas " + nombreTabla1 + " y " + nombreTabla2 + " con exito";
    }
}
