package ordenamiento;

import encriptacion.AESEncryption;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @GRUPO C-Tri (ChristianB, CarlosC y CarlosV)
 * Implementa el algoritmo  mezcla Equilabrada 
 * descrita en el Libro de Joyanes
 */

/* 
    Este algoritmo recibe como parametro el archivo a ordenar, 
    el indice de la columna a la cual va a ordenar y
    un booleano que determine si el campo esta o no enciptado.
*/

public class OrdenamientoBalanceado {

    private final int N = 6;
    private final int N2 = N / 2;
    private String[] cabecera;
    private final File f0;
    private final File[] f = new File[N];
    
    public static Boolean encriptado=false;
    
    public OrdenamientoBalanceado(File nuevoArchivo) {

/*        
        FormatoArchivo.indice = indice;
        FormatoArchivo.asc=asc;
*/
        String[] nomf = {"ar1.csv", "ar2.csv", "ar3.csv", "ar4.csv", "ar5.csv", "ar6.csv"};
        f0 = nuevoArchivo;
        for (int i = 0; i < N; i++) {
            f[i] = new File(nomf[i]);
        }
    }

    public void mezclaEqMple() {
        int i, j, k, k1, t;

        int[] c = new int[N];
        int[] cd = new int[N];
        
        Object[] flujos = new Object[N];
        CsvWriter flujoSalidaActual = null;
        CsvReader flujoEntradaActual = null;
        boolean[] actvs = new boolean[N2];

        try {
            
            t = distribuir();
            FormatoArchivo anterior = new FormatoArchivo();
            FormatoArchivo[] r = new FormatoArchivo[N2];

            for(i=0;i<N2;i++) r[i]=new FormatoArchivo();

            for (i = 0; i < N; i++) c[i] = i;
          
            // bucle hasta número de tramos == 1: archivo ordenado
            do {
                /*
                  Restringe el uso de tramos vacios
                  k1 =  numero de archivos a trabajar
                */
                k1 = (t < N2) ? t : N2;
                
                /*
                    Apertura de archivos de lectura (entrada)
                */
                for (i = 0; i < k1; i++) {
                    flujos[c[i]] = new CsvReader(
                            (new FileReader(f[c[i]].getAbsolutePath())));
                    cd[i] = c[i];
                }
                
                j = N2; // índice de archivo de salida
                t = 0;
                /*
                    Archivos de escritura (salida)
                */
                
                for (i = j; i < N; i++)
                    flujos[c[i]] = new CsvWriter(f[c[i]].getAbsolutePath());
                
                // entrada de una clave de cada flujo
                
                for (int n = 0; n < k1; n++) {
                    flujoEntradaActual = (CsvReader) flujos[cd[n]];
                    if(flujoEntradaActual.readHeaders())
                        r[n].setCampos(flujoEntradaActual.getHeaders());
                }
                while (k1 > 0) {
                    t++; // mezcla de otro tramo
                    for (i = 0; i < k1; i++) actvs[i] = true;
                    
                    flujoSalidaActual = (CsvWriter) flujos[c[j]];
                    
                    while (!finDeTramos(actvs, k1)) {
                        int n;
                        n = minimo(r, actvs, k1);
                        flujoEntradaActual = (CsvReader) flujos[cd[n]];
                        flujoSalidaActual.writeRecord(r[n].getCampos());
                        anterior.setCampos(r[n].getCampos());
                        
                        if(flujoEntradaActual.readHeaders()){
                            r[n].setCampos(flujoEntradaActual.getHeaders());
                            if (anterior.compareTo(r[n])>0) // fin de tramo
                                actvs[n] = false;
                            
                        }else{
                            k1--;
                            flujoEntradaActual.close();
                            cd[n] = cd[k1];
                            r[n] = r[k1];
                            actvs[n] = actvs[k1];
                            actvs[k1] = false;// no se accede a posición k1
                        }
                    }

                    j = (j < N - 1) ? j + 1 : N2; // siguiente flujo de salida
                }
                
                for (i = N2; i < N; i++) {
                    flujoSalidaActual = (CsvWriter) flujos[c[i]];
                    flujoSalidaActual.close();
                }
                /*
                    Cambio de finalidad de los flujos: entrada<->salida
                 */
                for (i = 0; i < N2; i++) {
                    int a;
                    a = c[i];
                    c[i] = c[i + N2];
                    c[i + N2] = a;
                }
            }while (t > 1);
            
            System.out.print("Archivo ordenado ... ");
            escribir(f[c[0]]);

        } catch (Exception e) {throw new Error(e.getMessage());}
        
    }

    //devuelve el índice del menor valor del array de claves
    private int minimo(FormatoArchivo[] r, boolean[] activo, int n) {
        int i, indice=0;
        FormatoArchivo m=r[0];
        
        for (i=1; i < n; i++) {
            if (activo[i] && r[i].compareTo(m) < 0) {
                m = r[i];
                indice = i;
            }
        }
        return indice;
    }
    
    //escribe las claves del archivo
    private void escribir(File f) throws FileNotFoundException, IOException, Exception {
        
        AESEncryption encript=new AESEncryption();
        
            //System.out.println();
            
        CsvReader flujo = new CsvReader(new FileReader(f.getAbsolutePath()));
        CsvWriter archivoOriginal = new CsvWriter(f0.getAbsolutePath());
        
        archivoOriginal.writeRecord(cabecera);
        
        FormatoArchivo r = new FormatoArchivo();
        
        while (flujo.readHeaders()){
            
            r.setCampos(flujo.getHeaders());
            
            //System.out.println(r.getCampoClave());
            
            /*Aqui se vuelven a encriptar los datos*/
            if(encriptado)
                r.setCampoClave(encript.bytesToHex(encript.encryptText(r.getCampoClave(), encript.getSecretEncryptionKey())));
            
            
            archivoOriginal.writeRecord(r.getCampos());
        } 
    
        System.out.println("\n *** Fin del archivo ***\n");
        flujo.close();
        archivoOriginal.close();
        
        FormatoArchivo.indice=0;
        FormatoArchivo.asc=true;
        OrdenamientoBalanceado.encriptado=false;
        
    }
    
    private boolean finDeTramos(boolean[] activo, int n) {
        boolean s = true;
        for (int k = 0; k < n; k++) {
            if (activo[k]) {
                s = false;
            }
        }
        return s;
    }
    
    private int distribuir() throws FileNotFoundException, IOException, Exception {

        AESEncryption encript=new AESEncryption();
        
        int j, nt;
        CsvReader flujo = new CsvReader(new FileReader(f0.getAbsolutePath()));
        CsvWriter[] flujoSalida = new CsvWriter[N2];
        
        if (flujo.readHeaders()) cabecera = flujo.getHeaders();
        else return -1;          
        
        FormatoArchivo.numCampos = cabecera.length;
        
        FormatoArchivo anterior = new FormatoArchivo();
        FormatoArchivo nuevo = new FormatoArchivo();
        
        for (j = 0; j < N2; j++)
            flujoSalida[j] = new CsvWriter(f[j].getAbsolutePath());
        
        j = 0; // indice del flujo de salida
        nt = 0;
        
        
        /* Primer registro */
        if(flujo.readHeaders()){        
            nuevo.setCampos(flujo.getHeaders());
            
            /* aqui reescribe el campo clave Desencriptado */
            if(encriptado)
                nuevo.setCampoClave(encript.decryptText(nuevo.getCampoClave(), encript.getSecretEncryptionKey()));
            
            flujoSalida[j].writeRecord(nuevo.getCampos());            
            nt++;
        }
        
        
        while (flujo.readHeaders()) {
            
            anterior.setCampos(nuevo.getCampos());
            nuevo.setCampos(flujo.getHeaders());
            
            if(encriptado)
                nuevo.setCampoClave(encript.decryptText(nuevo.getCampoClave(), encript.getSecretEncryptionKey()));
            //System.out.println(nuevo.getCampoClave());
            
            if (anterior.compareTo(nuevo) > 0) {
                nt++; // nuevo tramo
                j = (j < N2 - 1) ? j + 1 : 0; // siguiente archivo
            }
            flujoSalida[j].writeRecord(nuevo.getCampos());
        }
        
            //nt++; // cuenta ultimo tramo
            System.out.println("\n*** Número de tramos: " + nt + " ***");
            flujo.close();
            for (j = 0; j < N2; j++) {
                flujoSalida[j].close();
            }
        return nt;
    }
}