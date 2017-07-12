package logica;

import encriptacion.AESEncryption;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.table.DefaultTableModel;
import ordenamiento.OrdenamientoBalanceado;
import vistas.Desarrollador;

public class UnirTablas {

    private final String nombreTabla1;
    private final String nombreTabla2;
    private final String nombreCampo;
    private Boolean encriptado[] = null;
    private Boolean encriptado2 = false;
    private int posicionCampo1 = 0;
    private int posicionCampo2 = 0;
    private String detalleCampo;
    private int maximo = -1;
    private final AESEncryption encript = new AESEncryption();

    public UnirTablas(Object[] args) {
        this.nombreTabla1 = (String) args[0];
        this.nombreTabla2 = (String) args[1];
        this.nombreCampo = (String) args[2];
        this.detalleCampo = (String) args[3];

        ArrayList s = (ArrayList) args[4];
        encriptado = new Boolean[s.size()];
        for (int j = 0; j < s.size(); j++) {
            encriptado[j] = (Boolean) s.get(j);
        }

        encriptado2 = (Boolean) args[5];
        posicionCampo1 = (int) args[6];
        posicionCampo2 = (int) args[7];
        maximo = (int) args[8];
    }

    private ArrayList<String> getCamposTabla(String nombreTabla, int posicionCampo) {
        return null;
    }

    public void Visualizar() {

        String Cabecera[] = new String[encriptado.length];

        String matriz[][] = null;

        if (encriptado[0]) {
            try {
                detalleCampo = encript.bytesToHex(encript.encryptText(detalleCampo, encript.getSecretEncryptionKey()));
            } catch (Exception ex) {;
            }
        }

        try {
            CsvReader read = new CsvReader("BaseDatos\\" + nombreTabla1 + ".csv");
            read.readRecord();
            int j = 0;
            Cabecera[j++] = read.get(posicionCampo1);

            for (int i = 0; i < read.getColumnCount(); i++) {
                if (i != posicionCampo1) {
                    Cabecera[j++] = read.get(i);
                }
            }

            read.close();

            read = new CsvReader("BaseDatos\\" + nombreTabla2 + ".csv");
            read.readRecord();

            for (int i = 0; i < read.getColumnCount(); i++) {
                if (i != posicionCampo2) {
                    Cabecera[j++] = read.get(i);
                }
            }

            read.close();

            read = new CsvReader("BaseDatos\\" + nombreTabla1 + ".csv");
            read.readRecord();

            CsvWriter consulta = new CsvWriter("archivoMuestra.csv");

            consulta.writeRecord(Cabecera);

            String[] regs = new String[encriptado.length];
            int fila = 0;
            String aux;
            while (read.readRecord() & fila != maximo) {
                j = 0;
                //Primer campo que es el campo en comun
                if ((aux = read.get(posicionCampo1)).equals(detalleCampo)) {

                    regs[j++] = aux;
                    //Registrar el resto de campos de la primera tabla, archivo actual
                    for (int i = 0; i < read.getColumnCount(); i++) {
                        if (i != posicionCampo1) {
                            regs[j++] = read.get(i);
                        }
                    }
                    //Recuperar los campos de la segunda tabla
                    //SEGUNDO RECORRIDO
                    CsvReader interno = new CsvReader("BaseDatos\\" + nombreTabla2 + ".csv");
                    interno.readRecord();
                    while (interno.readRecord()) {
                        String claveAux = interno.get(posicionCampo2);
                        if (!Objects.equals(encriptado2, encriptado[0])) {
                            if (encriptado2) {
                                try {
                                    claveAux = encript.decryptText(claveAux, encript.getSecretEncryptionKey());
                                } catch (Exception ex) {
                                    throw new Error(ex);
                                }
                            } else {
                                try {
                                    claveAux = encript.bytesToHex(encript.encryptText(claveAux, encript.getSecretEncryptionKey()));
                                } catch (Exception ex) {
                                    throw new Error(ex);
                                }
                            }
                        }
                        if (claveAux.equals(read.get(posicionCampo1))) {
                            int m = j;
                            for (int i = 0; i < interno.getColumnCount(); i++) {
                                if (i != posicionCampo2) {
                                    regs[m++] = interno.get(i);
                                }
                            }
                            fila++;
                            consulta.writeRecord(regs);
                        }
                    }
                }
            }
            consulta.close();

            /* AQUI SE ORDENA EL ARCHIVO MUESTRA*/
            if (fila != 0) {
                OrdenamientoBalanceado o = new OrdenamientoBalanceado(new File("archivoMuestra.csv"));
                o.mezclaEqMple();
            }
            matriz = new String[fila][Cabecera.length];

            CsvReader reader = new CsvReader("archivoMuestra.csv");
            reader.readHeaders();

            int i = 0;

            while (reader.readRecord()) {
                for (int k = 0; k < Cabecera.length; k++) {
                    if (encriptado[k]) {
                        try {
                            matriz[i][k] = (encript.decryptText(reader.get(k), encript.getSecretEncryptionKey()));
                        } catch (Exception ex) {
                            throw new Error(ex);
                        }
                    } else {
                        matriz[i][k] = (reader.get(k));
                    }
                }
                i++;
            }

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        //Creacion de la tabla
        DefaultTableModel tabla;
        tabla = new DefaultTableModel(matriz, Cabecera);
        Desarrollador.jTable1.setModel(tabla);
    }
}
