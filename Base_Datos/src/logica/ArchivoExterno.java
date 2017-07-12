package logica;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ArchivoExterno extends CreacionTabla {

    public static String fileMETA = "BaseDatos\\Informacion.csv";

    public static void pasar_A_BaseDatos(File archivoDePaso,String nombreTabla,String campoClave) {

        CsvReader tabla = null;
        CsvWriter tablaBD = null;

        try {
            
            /*String nombreTabla = archivoDePaso.getName();
            nombreTabla = nombreTabla.substring(0, nombreTabla.lastIndexOf('.'));*/
            
            //--------------BLOQUE PARA VALIDAR EL NOMBRE-----------------------
            
            try {
            CsvReader ar = new CsvReader(fileMETA);
            while(ar.readRecord()){
                if(ar.get(0).equals(nombreTabla)){
                    ar.close(); 
                    throw new Error("Ya existe Una tabla con Este Nombre");
                    }
                } ar.close();
            }catch (IOException e) {
            throw new Error("No se ha podido conectar con la Base de Datos");
            }
            
            //------------------------------------------------------------
           
            tabla = new CsvReader(new FileReader(archivoDePaso.getAbsolutePath()));
            tablaBD = new CsvWriter("BaseDatos\\" + nombreTabla + ".csv");

            int numRegistros = 0;

            ArrayList<String> campos = new ArrayList();
            ArrayList<Integer> longitudes = new ArrayList();
            ArrayList<String> ce = new ArrayList();
            Boolean existeClave = false;

            if (tabla.readRecord()) {
                for (String campo : tabla.getValues()) {
                    System.out.println(campo+" : "+campoClave);
                    if (campo.equals(campoClave)) {
                        existeClave = true;
                    }
                    campos.add(campo);
                    ce.add("F");
                    longitudes.add(0);
                }
            }

            tablaBD.writeRecord(tabla.getValues());
            
            if (!existeClave) {
                tabla.close();
                tablaBD.close();
                throw new Error("Campo Clave No Existe");
            }

                
            while (tabla.readRecord()) {
                for (int i = 0; i < tabla.getColumnCount(); i++) {
                    if (tabla.get(i).length() > longitudes.get(i)) {
                        longitudes.set(i, tabla.get(i).length());
                    }
                }
                tablaBD.writeRecord(tabla.getValues());
                numRegistros++;
            }

            tabla.close();
            tablaBD.close();

            Object[] arg = {"CARGARARCHIVO", nombreTabla, campos, campoClave, longitudes, ce, numRegistros};
            new FacadeBD().CargarArchivo(arg);

        } catch(IOException ex) {
            throw new Error("No es un archivo compatible");
        }

    }

}
