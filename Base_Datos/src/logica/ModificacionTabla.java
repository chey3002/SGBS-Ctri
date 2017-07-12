package logica;

import interfaces.ModificacionFactory;
import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ModificacionTabla extends ModificacionFactory {

    String nombreTabla;
    String nombreCampo;
    String nuevoCampo;

    private int posCampo = -1;
    private ArrayList<String> valCampos = new ArrayList<>();

    @Override
    protected void IdentificarParametros(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.nombreCampo = (String) args[1];
        this.nuevoCampo = (String) args[2];
    }

    @Override
    protected void RealizarOperacion() {
        try {

            File mod = new File("BaseDatos\\" + this.nombreTabla + ".csv");
            if (mod.exists()) {

                //Renombrar el archivo
                mod.renameTo(new File("BaseDatos\\" + this.nombreTabla + "AuxTabla.csv"));
                CsvReader flujoEntrada = new CsvReader("BaseDatos\\" + this.nombreTabla + "AuxTabla.csv");

                //Busqueda de la posicion del campo
                this.posCampo = -1;
                flujoEntrada.readRecord();

                for (int i = 0; i < flujoEntrada.getColumnCount(); i++) {
                    if (flujoEntrada.get(i).equals(this.nombreCampo)) {
                        this.posCampo = i;
                        this.valCampos.add(this.nuevoCampo);
                    } else {
                        this.valCampos.add(flujoEntrada.get(i));
                    }
                }

                if (this.posCampo == -1)
                    throw new Error("Error: No se ha encontrado el campo a modificar");
          
                //Cambiar la cabecera del archivo

                PrintWriter flujoSalida = new PrintWriter("BaseDatos\\" + this.nombreTabla + ".csv");//Crear el archivo luego de superar la condicion
                flujoSalida.println(this.valCampos.toString().replace("[", "").replace("]", "").replace(" ", ""));

                //El siguiente proceso es por si el archivo contiene registros, es decir una tabla con registros
                //Lectura y escritura
                while (flujoEntrada.readRecord())
                    flujoSalida.println(flujoEntrada.getRawRecord());

                flujoEntrada.close();
                flujoSalida.close();

                //Borrar el archivo auxiliar
                new File("BaseDatos\\" + this.nombreTabla + "AuxTabla.csv").delete();
            }
        } catch (FileNotFoundException ex) {
            throw new Error("Error al cargar los archivos");
        } catch (IOException ex) {
            throw new Error("Error al cargar los archivos");
        }
    }

    @Override
    protected void ActualizarINFORMACION() {
        try {
            //Si el archivo Informacion.csv existe, leer y escribir su contenido
            if (new File(this.fileInformacion).exists()) {
                File file = new File(this.fileInformacion);
                //Renombrar el archivo
                file.renameTo(new File(file.getParent() + "\\" + "fileAux.csv"));
                CsvReader flujoEntrada = new CsvReader(file.getParent() + "\\" + "fileAux.csv");

                //Lectura y escritura
                PrintWriter flujoSalida = new PrintWriter(this.fileInformacion);//Crear el archivo luego de superar la condicion
                while (flujoEntrada.readRecord()) {
                    if (!this.nombreTabla.equals(flujoEntrada.get(0))) {
                        flujoSalida.println(flujoEntrada.getRawRecord());
                    } else {
                        for (int i = 0; i < 4; i++) {
                            flujoSalida.print(flujoEntrada.get(i) + ",");
                        }
                        flujoSalida.println(this.valCampos.toString().replace("[", "").replace("]", "").replace(" ", ""));

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
    }
}
