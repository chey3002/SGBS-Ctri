package interfaces;

import interfaces.ModificacionFactory;
import interfaces.Eliminacion;
import interfaces.Creacion;

public interface Productos {

    public Creacion creacionProceso();

    public ModificacionFactory modificacionProceso();

    public Eliminacion eliminacionProceso();
}
