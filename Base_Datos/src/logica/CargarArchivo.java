package logica;

public class CargarArchivo extends CreacionTabla{
    
    /* 
        Emplea la misma logica que al crear una tabla
        Pero se evita el método RealizarOperación porque
        esta operación es controlada desde otra clase 
        espeficicamente al cargar datos.
    
    */
    
    @Override
    protected void RealizarOperacion() {
        /* NO HACE NADA */
    }
    
}
