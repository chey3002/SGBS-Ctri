package logica;

import interfaces.ModificacionFactory;
import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class ModificacionRegistro extends ModificacionFactory {

    String nombreTabla;
    String campoClave;
    String nombreCampo;
    String valorCampo;
    private int posicionCampoClave;

    @Override
    protected void IdentificarParametros(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.campoClave = (String) args[1];
        this.nombreCampo = (String) args[2];
        this.valorCampo = (String) args[3];
        this.posicionCampoClave = (int) args[4];

    }

    @Override
    //ACTUALIZAR REGISTRO n3 CLAVE 3 CAMPO campo3 POR 3
    protected void RealizarOperacion() {
        try {
            File mod = new File("BaseDatos\\" + this.nombreTabla + ".csv");
            if (mod.exists()) {

                //Renombrar el archivo
                mod.renameTo(new File("BaseDatos\\" + this.nombreTabla + "AuxTabla.csv"));
                CsvReader flujoEntrada = new CsvReader("BaseDatos\\" + this.nombreTabla + "AuxTabla.csv");
                PrintWriter flujoSalida = new PrintWriter("BaseDatos\\" + this.nombreTabla + ".csv");
                //Lectura y escritura
                flujoEntrada.readRecord();
                flujoSalida.println(flujoEntrada.getRawRecord());
                int posicionCampoCambio = -1;
                for (int i = 0; i < flujoEntrada.getColumnCount(); i++) {
                    if (flujoEntrada.get(i).equals(this.nombreCampo)) {
                        posicionCampoCambio = i;
                        break;
                    }
                }
                if (posicionCampoCambio == -1)
                    throw new Error("No se encontro los datos");
                
                while (flujoEntrada.readRecord()) {
                    if (!flujoEntrada.get(this.posicionCampoClave).equals(this.campoClave)) {
                        flujoSalida.println(flujoEntrada.getRawRecord());
                    } else {
                        for (int i = 0; i < flujoEntrada.getColumnCount(); i++) {
                            if (i != posicionCampoCambio) {
                                flujoSalida.print(flujoEntrada.get(i));
                            } else {
                                flujoSalida.print(this.valorCampo);
                            }
                            if (i + 1 == flujoEntrada.getColumnCount()) {
                                flujoSalida.print("\n");
                            } else {
                                flujoSalida.print(",");
                            }
                        }
                    }
                }
                flujoEntrada.close();
                flujoSalida.close();

                //Borrar el archivo auxiliar
                new File("BaseDatos\\" + this.nombreTabla + "AuxTabla.csv").delete();
                flujoEntrada.close();
                flujoSalida.close();
            }
        } catch (FileNotFoundException ex) {
            throw new Error("Error al cargar los archivos");
        } catch (IOException ex) {
            throw new Error("Error al cargar los archivos");
        }
    }

    @Override
    protected void ActualizarINFORMACION() {
        //Actualizaciones AQUI
    }
}
