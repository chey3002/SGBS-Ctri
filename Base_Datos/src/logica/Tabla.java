package logica;

import interfaces.Productos;
import interfaces.ModificacionFactory;
import interfaces.Eliminacion;
import interfaces.Creacion;

public class Tabla implements Productos {

    @Override
    public Creacion creacionProceso() {
        return new CreacionTabla();
    }

    @Override
    public ModificacionFactory modificacionProceso() {
        return new ModificacionTabla();
    }

    @Override
    public Eliminacion eliminacionProceso() {
        return new EliminacionTabla();
    }

}
