package interfaces;

public abstract class ModificacionFactory {

    protected final String fileInformacion = "BaseDatos\\Informacion.csv";

    public final void operation(Object[] args) {
        this.IdentificarParametros(args);
        this.RealizarOperacion();
        this.ActualizarINFORMACION();
    }

    protected abstract void IdentificarParametros(Object[] args);

    protected abstract void RealizarOperacion();

    protected abstract void ActualizarINFORMACION();
}
