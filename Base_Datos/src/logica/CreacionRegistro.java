package logica;

import interfaces.Creacion;
import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CreacionRegistro extends Creacion {
    
    String nombreTabla;
    List<String> valoresCampos;

    @Override
    protected void IdentificarParametros(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.valoresCampos = (List)(ArrayList<String>) args[1];
    }
    
    @Override
    protected void RealizarOperacion() {
        
        try {
            File file = new File("BaseDatos\\" + this.nombreTabla + ".csv");
            if(file.exists()){
                Scanner db = new Scanner(new File("BaseDatos\\" + this.nombreTabla + ".csv")).useDelimiter("\\A");
                String contenido = db.next();
                db.close();
                file.delete();
                PrintWriter wr = new PrintWriter("BaseDatos\\" + this.nombreTabla + ".csv");
                String nuevoRegistro = contenido + "\n" + this.valoresCampos.toString().replace("[", "").replace("]", "").replace(" ", "");
                wr.println(nuevoRegistro);
                wr.close();
            }
            else{
                
            }
            
            
        } catch (FileNotFoundException ex) {}
    }

    @Override
    protected void ActualizarINFORMACION() {
               try{
        //Si el archivo Informacion.csv existe, leer y escribir su contenido
            if(new File(this.fileInformacion).exists())
            {
                File file = new File(this.fileInformacion);
                //Renombrar el archivo
                file.renameTo(new File(file.getParent() + "\\" + "fileAux.csv"));
                CsvReader flujoEntrada = new CsvReader(file.getParent() + "\\" + "fileAux.csv");
                
                //Lectura y escritura
                PrintWriter flujoSalida = new PrintWriter(this.fileInformacion);//Crear el archivo luego de superar la condicion
                while(flujoEntrada.readRecord()){
                    if(!this.nombreTabla.equals(flujoEntrada.get(0)))
                        flujoSalida.println(flujoEntrada.getRawRecord());
                    else{
                        flujoSalida.print(flujoEntrada.get(0) + ",");
                        flujoSalida.print((Integer.parseInt(flujoEntrada.get(1))+1) + ",");
                        for(int i = 2; i<flujoEntrada.getColumnCount(); i++){
                            flujoSalida.print(flujoEntrada.get(i));
                            if(i+1!=flujoEntrada.getColumnCount())
                                flujoSalida.print(",");
                        }
                        flujoSalida.println();
                    }
                }
                    
                flujoSalida.close();
                flujoEntrada.close();
         
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
