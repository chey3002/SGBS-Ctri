package logica;

import interfaces.Eliminacion;
import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class EliminacionRegistro extends Eliminacion{
    
    String nombreTabla;
    String valorCampoClave;
    private int posicionCampoClave;

    @Override
    protected void IdentificarParametros(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.valorCampoClave = (String) args[1];
        this.posicionCampoClave = (int) args[2];
    }
    
    @Override
    protected void RealizarOperacion() {
        try{
            
            File mod = new File("BaseDatos\\" + this.nombreTabla + ".csv");
            if(mod.exists())
            {
                
                //Renombrar el archivo
                mod.renameTo(new File("BaseDatos\\" + this.nombreTabla + "AuxTabla.csv"));
                CsvReader flujoEntrada = new CsvReader("BaseDatos\\" + this.nombreTabla + "AuxTabla.csv");
                PrintWriter flujoSalida = new PrintWriter("BaseDatos\\" + this.nombreTabla + ".csv");
                
                //Lectura y escritura
                flujoEntrada.readRecord();
                flujoSalida.println(flujoEntrada.getRawRecord());
                while(flujoEntrada.readRecord()){
                    if(!flujoEntrada.get(this.posicionCampoClave).equals(this.valorCampoClave))
                        flujoSalida.println(flujoEntrada.getRawRecord());
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
                        flujoSalida.print((Integer.parseInt(flujoEntrada.get(1))-1) + ",");
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