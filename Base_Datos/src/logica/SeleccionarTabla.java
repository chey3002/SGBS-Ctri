package logica;

import encriptacion.AESEncryption;
import ordenamiento.FormatoArchivo;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;
import ordenamiento.OrdenamientoBalanceado;
import vistas.Desarrollador;

public class SeleccionarTabla {
    private final String nombreTabla;
    private final String nombreCampo;
    private String valorCampo;
    private final int posicion;
    private Boolean[] encriptado=null;
    private int maximo=0;
    private final AESEncryption encript = new AESEncryption();

    public SeleccionarTabla(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.nombreCampo = (String) args[1];
        this.valorCampo = (String) args[2];
        this.posicion = (int) args[3];
        this.encriptado = (Boolean[]) args[4];
        this.maximo=(int)args[5];
    }
    
    public void Visualizar() throws IOException{
        try {
            try {
                if(encriptado[posicion])
                    valorCampo=encript.bytesToHex(encript.encryptText(valorCampo, encript.getSecretEncryptionKey()));
            } catch (Exception ex) {throw new Error(ex.getMessage());}

            CsvReader reader = new CsvReader("BaseDatos\\" + nombreTabla + ".csv");
            CsvWriter consulta = new CsvWriter("archivoMuestra.csv");
            
            
            String[] cabecera=null;
            
            if(reader.readHeaders())
               cabecera = reader.getHeaders();
            
            consulta.writeRecord(cabecera);
            
            int filas = 0;
            int columnas = 0;
            
            while(reader.readRecord() & maximo!=filas){
                if(reader.get(posicion).equals(valorCampo)){
                    filas++;
                    consulta.writeRecord(reader.getValues());
                }
            }
            
            reader.close();
            consulta.close();
            
            columnas = FormatoArchivo.numCampos;
            
            if(filas!=0){
                OrdenamientoBalanceado o = new OrdenamientoBalanceado(
                        new File("archivoMuestra.csv"));
                o.mezclaEqMple();
            }
            
            reader = new CsvReader("archivoMuestra.csv");
            
            reader.readHeaders();
            
            int i = 0;
            
            String matriz[][] = new String[filas][columnas];
            
            while(reader.readRecord()){
                for (int j = 0; j<columnas; j++){
                    if(encriptado[j]){
                        try {matriz[i][j] = encript.decryptText(reader.get(j),encript.getSecretEncryptionKey());
                        } catch (Exception ex){throw new Error(ex);}
                    }else{matriz[i][j] = reader.get(j);}                    
                }
                i++;
            }
            reader.close();
            
            
            //Creacion de la tabla
            DefaultTableModel tabla;
            tabla = new DefaultTableModel(matriz, cabecera);
            Desarrollador.jTable1.setModel(tabla);
            
        } catch (FileNotFoundException ex) {
            throw new Error("Error al cargar los archivos");
        }
        catch (IOException ex) {
            throw new Error("Error al cargar los archivos");
        }
    }
}
