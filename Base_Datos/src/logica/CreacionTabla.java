package logica;

import interfaces.Creacion;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CreacionTabla extends Creacion{
    
    protected String nombreTabla;
    protected ArrayList<String> campos;
    protected String campoClave;
    protected ArrayList<Integer> longitudCampos;
    protected ArrayList<String> cEncriptados;
    protected int numRegistros;

    @Override
    protected void IdentificarParametros(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.campos = (ArrayList<String>) (List) args[1];
        this.campoClave = (String) args[2];
        this.longitudCampos = (ArrayList<Integer>) (List) args[3];
        this.cEncriptados=(ArrayList<String>) (List) args[4];
        this.numRegistros= (int) args[5];
    }

    @Override
    protected void RealizarOperacion() {
        new File("BaseDatos\\").mkdirs();//Crea el directorio en caso de que no exista  
        try{
            PrintWriter flujoSalida = new PrintWriter("BaseDatos" + "\\" + nombreTabla + ".csv"); 
            flujoSalida.println(this.campos.toString().replace("[", "").replace("]", "").replace(" ", ""));
            flujoSalida.close(); 
        }
        catch(Exception e ){
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        } 
    }
    
    @Override
    protected void ActualizarINFORMACION() {
            try {
            File file = new File(this.fileInformacion);
            if(file.exists()){
                Scanner scan =  new Scanner(new File(this.fileInformacion)).useDelimiter("\\A");
                String contenido =scan.next();
                scan.close();
                file.delete();
                PrintWriter wr = new PrintWriter(this.fileInformacion);
                wr.print(contenido + "\n");
                wr.println(this.nombreTabla + "," + numRegistros + "," + this.campoClave + "," +
                        this.longitudCampos.toString().replace("[", "").replace("]", "").replace(" ", "")
                + "," + this.campos.toString().replace("[", "").replace("]", "").replace(" ", "")+","+this.cEncriptados.toString().replace("[", "").replace("]", "").replace(" ", ""));
                wr.close();
            }
            else{
                PrintWriter wr = new PrintWriter(this.fileInformacion);
                wr.println(this.nombreTabla + "," + numRegistros + "," + this.campoClave + "," + this.longitudCampos.toString().replace("[", "").replace("]", "").replace(" ", "")
                + "," + this.campos.toString().replace("[", "").replace("]", "").replace(" ", "")+","+this.cEncriptados.toString().replace("[", "").replace("]", "").replace(" ", ""));
                wr.close();
            }
        } catch (FileNotFoundException ex) {
            throw new Error("No se encontro el archivo");}
    
    }
}