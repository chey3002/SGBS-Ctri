package logica;

import interfaces.Productos;
import interfaces.ModificacionFactory;
import interfaces.Eliminacion;
import interfaces.Creacion;

public class Registro implements Productos {

    @Override
    public Creacion creacionProceso() {
        return new CreacionRegistro();
    }

    @Override
    public ModificacionFactory modificacionProceso() {
        return new ModificacionRegistro();
    }

    @Override
    public Eliminacion eliminacionProceso() {
        return new EliminacionRegistro();
    }
}
