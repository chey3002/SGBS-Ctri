package logica;

import interfaces.Eliminacion;
import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class EliminacionTabla extends Eliminacion {

    String nombreTabla;

    @Override
    protected void IdentificarParametros(Object[] args) {
        this.nombreTabla = (String) args[0];
    }

    @Override
    protected void RealizarOperacion() {
        File file = new File(this.fileInformacion);
        File eliminar = new File(file.getParent() + "\\" + this.nombreTabla + ".csv");
        if (eliminar.exists()) {
            eliminar.delete();
        }
    }

    @Override
    protected void ActualizarINFORMACION() {

        try {

            //Si el archivo Informacion.csv existe, leer y escribir su contenido
            if (new File(this.fileInformacion).exists()) {
                File file = new File(this.fileInformacion);

                //Renombrar el archivo
                CsvReader FX = new CsvReader(file.getParent() + "\\" + "Informacion.csv");

                file.setWritable(true);
                file.renameTo(new File(file.getParent() + "\\" + "fileAux.csv"));
                CsvReader flujoEntrada = new CsvReader(file.getParent() + "\\" + "fileAux.csv");

                //Lectura y escritura
                PrintWriter flujoSalida = new PrintWriter(this.fileInformacion);//Crear el archivo luego de superar la condicion
                while (flujoEntrada.readRecord()) {
                    if (!this.nombreTabla.equals(flujoEntrada.get(0))) {
                        flujoSalida.println(flujoEntrada.getRawRecord());
                    }
                }

                flujoEntrada.close();
                flujoSalida.close();

                //Borrar el archivo auxiliar
                new File(file.getParent() + "\\" + "fileAux.csv").delete();
            }
        } catch (FileNotFoundException ex) {
            throw new Error("Error al cargar los archivos");
        } catch (IOException ex) {
            throw new Error("Error al cargar los archivos");
        }

        File file = new File(this.fileInformacion);
        File eliminar = new File(file.getParent() + "\\" + this.nombreTabla + ".csv");
        //Si el archivo esta vacio, eliminar dicho archivo
        if (eliminar.length() == 0) {
            eliminar.delete();
        }
    }
}
