package logica;

import encriptacion.AESEncryption;
import com.csvreader.CsvReader;
import interfaces.GestionBD;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ordenamiento.OrdenamientoBalanceado;
import vistas.Desarrollador;

public class ProxyProtectorBD implements GestionBD {

    private AESEncryption encript = new AESEncryption();
    
    static int opcion = 0;
    static final int NT = 1;//Crear nueva tabla
    static final int MT = 2;//Modificar tabla
    static final int DT = 3;//Eliminar tabla
    static final int NR = 4;//Crear nuevo registro
    static final int AR = 5;//Actualizar registro
    static final int DR = 6;//Eliminar registro existente
    static final int SELECT = 7;//operación select
    static final int UNION = 8;//operacioón join
    static final int CARGAR = 9; // cargar Archivos a la base de Datos

    private boolean BuscarTabla(String nombreTabla) {
        if (!new File("BaseDatos\\Informacion.csv").exists()) {
            return false;
        }
        String fileINFORMACION = "BaseDatos\\Informacion.csv";
        try {
            CsvReader ar = new CsvReader(fileINFORMACION);
            while (ar.readRecord()) {
                if (ar.get(0).equals(nombreTabla)) {
                    ar.close();
                    return true;
                }
            }
            ar.close();
        } catch (FileNotFoundException ex) {
            throw new SecurityException("Error al carga los archivos");
        } catch (IOException ex) {
            throw new SecurityException("Error al carga los archivos");
        }
        return false;
    }

    @Override
    public void Peticion(Object[] args) {
        //El argumento en la posicion cero es el comando
        String entrada = (String) args[0];
        //Asignacion tipo
        if (entrada.replace(" ", "").contains("CREARTABLA")) {
            opcion = NT;
        } else if (entrada.replace(" ", "").contains("MODIFICARTABLA")) {
            opcion = MT;
        } else if (entrada.replace(" ", "").contains("ELIMINARTABLA")) {
            opcion = DT;
        } else if (entrada.replace(" ", "").contains("INSERTAREN")) {
            opcion = NR;
        } else if (entrada.replace(" ", "").contains("ACTUALIZARREGISTRO")) {
            opcion = AR;
        } else if (entrada.replace(" ", "").contains("BORRARREGISTRO")) {
            opcion = DR;
        } else if (entrada.replace(" ", "").contains("SELECCIONARDE")) {
            opcion = SELECT;
        } else if (entrada.replace(" ", "").contains("UNIR")) {
            opcion = UNION;
        } else if (entrada.replace(" ", "").equals("CARGAR")){
            opcion = CARGAR;
        } else {
            throw new SecurityException("Consulta inválido");
        }
        switch (opcion) {
            case NT: {
                int i = "CREAR TABLA ".length();
                String nombreTabla = null;
                String campoClave = null;
                List<String> campos = new ArrayList<>();
                List<Integer> longitudes = new ArrayList<>();
                List<String> ce = new ArrayList<>();
                for (i = i; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        break;
                    }
                }
                StringBuffer atributo = new StringBuffer();
                //Nombre de la tabla
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        atributo.append(entrada.charAt(i));
                    } else {
                        break;
                    }
                }
                //lectura de  campos
                nombreTabla = atributo.toString();
                atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        break;
                    }
                }

                //indentificar CAMPOS
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        atributo.append(entrada.charAt(i));
                    } else {
                        break;
                    }
                }

                if (!atributo.toString().equals("CAMPOS")) {
                    throw new SecurityException("Palabra CAMPOS no encontrada");
                }

                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        break;
                    }
                }

                //Listado de Campos
                atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++) {
                    atributo.append(entrada.charAt(i));
                }

                if (!atributo.toString().contains("CLAVE")) {
                    throw new SecurityException("Palabra CLAVE no encontrada");
                }

                //Listado de campos
                String comando2 = atributo.toString().replace(" ", "");
                //System.out.println(comando2);

                atributo = new StringBuffer();
                for (i = 0; i < comando2.indexOf("CLAVE"); i++) {
                    if (comando2.charAt(i) != ',') {
                        atributo.append(comando2.charAt(i));
                    } else {
                        campos.add(atributo.toString());
                        atributo = new StringBuffer();
                    }
                }
                campos.add(atributo.toString());

                //Campo clave
                atributo = new StringBuffer();
                for (i = i + 0; i < comando2.length(); i++) {
                    atributo.append(comando2.charAt(i));
                }
                if (!atributo.toString().contains("LONGITUD")) {
                    throw new SecurityException("Palabra LONGITUD no encontrada");
                }
                entrada = atributo.toString().replace("CLAVE", "");
                atributo = new StringBuffer();
                for (i = 0; i < entrada.indexOf("LONGITUD"); i++) {
                    atributo.append(entrada.charAt(i));
                }
                campoClave = atributo.toString();
                entrada = entrada.replace(campoClave, "");
                //Longitud de los campos
                entrada = entrada.replace("LONGITUD", "");

                atributo = new StringBuffer();
                try {
                    for (i = 0; i < entrada.length(); i++) {
                        if (entrada.charAt(i) != ',' & entrada.charAt(i)!='E') {
                            atributo.append(entrada.charAt(i));
                        }else if (entrada.charAt(i) == 'E'){break;}                     
                        else {
                            if (Integer.parseInt(atributo.toString()) < 1 |
                                    Integer.parseInt(atributo.toString()) > 100)
                                throw new SecurityException("Verifique el rango de longitud (0,100]");
                            
                            longitudes.add(Integer.parseInt(atributo.toString()));
                            atributo = new StringBuffer();                           
                        }        
                    }
                    if (Integer.parseInt(atributo.toString()) < 1 | 
                            Integer.parseInt(atributo.toString()) > 100)
                        throw new SecurityException("Verifique el rango de longitud (0,100]");
                    
                    longitudes.add(Integer.parseInt(atributo.toString()));

                } catch (NumberFormatException e) {
                    throw new SecurityException("Longuites incorrectas");
                }

                if(longitudes.size() == 1) {
                    for (int j = 1; j < campos.size(); j++) {
                        longitudes.add(longitudes.get(0));
                    }
                } else if (longitudes.size() != campos.size()) {
                    throw new SecurityException("Verifique las longitudes");
                }

                i = 0;
                atributo = new StringBuffer();
                do {

                    atributo.append(entrada.charAt(i));
                    i++;

                } while (entrada.charAt(i) != 'E' && i < entrada.length() - 1);
                if (entrada.charAt(i) != 'E') {
                    atributo.append(entrada.charAt(entrada.length() - 1));
                }
                entrada = entrada.replace(atributo.toString(), "");
                String num = atributo.toString();
                //Palabra reservaba ENCRIPTADO
                atributo = new StringBuffer();
                for (i = 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        atributo.append(entrada.charAt(i));
                    } else {
                        break;
                    }
                }

                if (!atributo.toString().contains("ENCRIPTADO")) {
                    if (atributo.toString().equals("")) {
                        atributo.append("F");
                        ce.add(atributo.toString());
                        atributo = new StringBuffer();
                        for (i = 1; i < campos.size(); i++) {
                            atributo.append("F");
                            ce.add(atributo.toString());
                            atributo = new StringBuffer();
                        }

                    } else {
                        throw new SecurityException("Palabra ENCRIPTADO mal escrita");
                    }
                }
                //Listado de Campos encriptados
                String comandos = atributo.toString().replace("ENCRIPTADO", "");
                atributo = new StringBuffer();
                if (entrada.length() > 0) {
                    for (i = 0; i < comandos.length(); i++) {
                        if (comandos.charAt(i) != ',') {
                            atributo.append(comandos.charAt(i));
                        } else {
                            if ("T".equals(atributo.toString()) || "F".equals(atributo.toString())) {
                                ce.add(atributo.toString());
                            } else {
                                throw new SecurityException("Los campos a encriptar deben estar especificados con T, o F");
                            }
                            atributo = new StringBuffer();
                        }
                    }
                    //System.out.println(atributo.toString());
                    if (atributo.toString().contains("T") || atributo.toString().contains("F")) {
                        ce.add(atributo.toString());
                    } else {
                        throw new SecurityException("Los campos a encriptar deben estar especificados con T, o F");
                    }

                    ce.remove("");
                }
                
                if(ce.size()!=campos.size())
                    throw new SecurityException("Faltan datos para la Encriptación");
                
                //Validacion de tabla existente
                if (this.BuscarTabla(nombreTabla)) {
                    throw new SecurityException("Tabla ya existente");
                }
                //Validacion campo clave existente
                if (campos.contains(campoClave) == false) {
                    throw new SecurityException("Error en campo clave");
                }

                /*
                * Solicitud aceptada correctamente
                **/
                
                /*por ser tabla nueva*/ int numRegistros = 0;
               
                Object[] arg = {"CREARTABLA", nombreTabla, campos, campoClave, longitudes, ce, numRegistros};
                new FacadeBD().CrearTabla(arg);
                break;

            }
            case MT: {
                int i = "MODIFICAR TABLA ".length();

                String nombreTabla = null;
                String nombreCampo = null;
                String nuevoCampo = null;

                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        break;
                    }
                }
                StringBuffer atributo = new StringBuffer();

                //Nombre de la tabla
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        atributo.append(entrada.charAt(i));
                    } else {
                        break;
                    }
                }

                nombreTabla = atributo.toString();

                if (nombreTabla.isEmpty()) {
                    throw new SecurityException("ERROR: No se ha especificado el nombre de la tabla");
                }
                //Palabra reservada Campo
                atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        break;
                    }
                }
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        atributo.append(entrada.charAt(i));
                    } else {
                        break;
                    }
                }

                if (!atributo.toString().equals("CAMPO")) {
                    throw new SecurityException("ERROR DE SINTAXIS: Falta la palabra reservada CAMPO");
                }
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        break;
                    }
                }
                //Campo
                atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        break;
                    }
                }
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        atributo.append(entrada.charAt(i));
                    } else {
                        break;
                    }
                }

                nombreCampo = atributo.toString();
                if (nombreCampo.isEmpty()) {
                    throw new SecurityException("ERROR: No se ha especificado el nombre del campo a modificar");
                }

                //Palabra reservada POR
                atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        break;
                    }
                }
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        atributo.append(entrada.charAt(i));
                    } else {
                        break;
                    }
                }

                if (!atributo.toString().equals("POR")) {
                    throw new SecurityException("ERROR DE SINATXIS: Falta la palabra reservada POR");
                }

                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        break;
                    }
                }

                //Nuevo valor del campo
                atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        break;
                    }
                }
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        atributo.append(entrada.charAt(i));
                    } else {
                        break;
                    }
                }

                nuevoCampo = atributo.toString();
                if (nuevoCampo.isEmpty()) {
                    throw new SecurityException("ERROR: No se ha especificado el nuevo valor del campo");
                }

                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        throw new SecurityException("ERROR: Existen caracteres de mas en la sentencia");
                    }
                }

                //Validacion de tabla existente
                if (!this.BuscarTabla(nombreTabla)) {
                    throw new SecurityException("ERROR: El nombre de la tabla especificada no existe");
                }

                //Validacion campo existente
                ArrayList<String> campos = new ArrayList<>();
                String fileINFORMACION = "BaseDatos\\Informacion.csv";
                boolean boolClave = false;
                try {
                    CsvReader ar = new CsvReader(fileINFORMACION);
                    while (ar.readRecord()) {
                        if (ar.get(0).equals(nombreTabla)) {
                            for (int j = 4; j < ar.getColumnCount(); j++) {
                                campos.add(ar.get(j));
                            }
                            if (ar.get(2).equals(nombreCampo)) {
                                boolClave = true;
                            }
                        }

                    }
                    ar.close();
                } catch (FileNotFoundException ex) {
                    throw new Error("Error al cargar los datos");
                } catch (IOException ex) {
                    throw new Error("Error al cargar los datos");
                }

                if (!campos.contains(nombreCampo)) {
                    throw new SecurityException("ERROR: El nombre del campo especificado no existe");
                }

                if (boolClave == true) {
                    throw new SecurityException("ERROR: No se puede modificar el campo clave");
                }
                
                        

                /*
                 * Solicitud aceptada correctamente
                 *
                 */
                Object[] arg = {"MODIFICARTABLA", nombreTabla, nombreCampo, nuevoCampo};
                new FacadeBD().ModificarTabla(arg);

                break;
            }
            case DT: //----------------------------------------------
            {
                String nombreTabla = null;

                int i = "ELIMINAR TABLA ".length();
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        break;
                    }
                }
                StringBuilder atributo = new StringBuilder();

                //Nombre de la tabla
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        atributo.append(entrada.charAt(i));
                    } else {
                        break;
                    }
                }

                nombreTabla = atributo.toString();
                if (nombreTabla.isEmpty()) {
                    throw new SecurityException("ERROR: No se ha especificado el nombre de la tabla");
                }

                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        throw new SecurityException("ERROR: Existen caracteres de mas en la sentencia");
                    }
                }

                String fileINFORMACION = "BaseDatos\\Informacion.csv";
                Boolean existeCampo = false;
                try {
                    CsvReader ar = new CsvReader(fileINFORMACION);
                    while (ar.readRecord()) {
                        if (ar.get(0).equals(nombreTabla)){
                            if(!ar.get(1).equals("0")){
                                ar.close();
                                throw new SecurityException("NO SE PUEDE ELIMINAR: La tabla contiene Registros");
                            }
                            existeCampo =true;
                    }
                }
                    ar.close();
                } catch (FileNotFoundException ex) {
                    throw new SecurityException("Error al carga los archivos");
                } catch (IOException ex) {
                    throw new SecurityException("Error al carga los archivos");
                }

                //Validacion de tabla existente
                if (!existeCampo)
                    throw new SecurityException("ERROR: El nombre de la tabla especificada no existe");
                
                
                /**
                 * Solicitud aceptada correctamente
                 *
                 */
                Object[] arg = {"ELIMINARTABLA", nombreTabla};
                new FacadeBD().EliminarTabla(arg);

                break;
            }
            case NR: //----------------------------------------------
            {
                int i = "INSERTAR EN ".length();

                String nombreTabla = null;
                List<String> valoresCampos = new ArrayList<>();

                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        break;
                    }
                }

                StringBuffer atributo = new StringBuffer();

                //Nombre de la tabla
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        atributo.append(entrada.charAt(i));
                    } else {
                        break;
                    }
                }

                nombreTabla = atributo.toString();

                if (nombreTabla.isEmpty()) {
                    throw new SecurityException("ERROR: No se ha especificado el nombre de la tabla");
                }

                atributo = new StringBuffer();

                //Palabra reservada CLAVE
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        break;
                    }
                }
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        atributo.append(entrada.charAt(i));
                    } else {
                        break;
                    }
                }

                if (!atributo.toString().equals("VALORES")) {
                    throw new SecurityException("ERROR DE SINTAXIS: Falta la palabra reservada VALORES");
                }

                atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ' ') {
                        atributo.append(entrada.charAt(i));
                    }
                }

                entrada = atributo.toString();

                //Validacion de tabla existente
                if (!this.BuscarTabla(nombreTabla)) {
                    throw new SecurityException("ERROR: El nombre de la tabla especificada no existe");
                }

                //Validaciones longitud y numero de campos
                int nroCampos = - 1;
                int longitud = -1;
                ArrayList<Integer> longitudCampo = new ArrayList();
                String campoClave = null;
                ArrayList<String> cencript = new ArrayList();
                String fileINFORMACION = "BaseDatos\\Informacion.csv";

                try {
                    CsvReader ar = new CsvReader(fileINFORMACION);
                    while (ar.readRecord()) {

                        if (ar.get(0).equals(nombreTabla)) {
                            nroCampos = (ar.getColumnCount() - 3) / 3;

                            for (int j = 0; j < nroCampos; j++) {
                                longitudCampo.add(Integer.parseInt(ar.get(j + 3)));
                            }
                            campoClave = ar.get(2);

                            for (i = nroCampos; i >= 1; i--) {
                                cencript.add(ar.get(ar.getColumnCount() - i));
                            }
                            break;
                        }
                    }
                    ar.close();
                } catch (IOException | NumberFormatException e) {
                    throw new Error("Error al cargar los archivos");
                }
                //Valores de campos
                int valorLongitud=0;
                atributo = new StringBuffer();
                for (i = 0; i < entrada.length(); i++) {
                    if (entrada.charAt(i) != ',') {
                        atributo.append(entrada.charAt(i));
                    } else {
                        valoresCampos.add(atributo.toString());
                        longitud=longitudCampo.get(valorLongitud);
                        if (atributo.toString().length() > longitud) {
                            throw new SecurityException("ERROR: La longitud de los datos es superior a la establesida");
                        }
                        valorLongitud++;
                        if (cencript.get(valoresCampos.size() - 1).contains("T")) {

                            
                            try {
                                valoresCampos.set(valoresCampos.size() - 1, encript.bytesToHex(encript.encryptText(atributo.toString(), encript.getSecretEncryptionKey())));
                                //       System.out.println(new String(encript.decryptText(valoresCampos.get(valoresCampos.size()-1), encript.getSecretEncryptionKey())));
                            } catch (Exception ex) {
                                Logger.getLogger(ProxyProtectorBD.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        atributo = new StringBuffer();
                    }
                }
                if (cencript.get(cencript.size() - 1).contains("T")) {

                    AESEncryption encript = new AESEncryption();
                    try {
                    valoresCampos.add(encript.bytesToHex(encript.encryptText(atributo.toString(), encript.getSecretEncryptionKey())));
                    } catch (Exception ex) {throw new SecurityException("Error de encritpación");}
                } else 
                    valoresCampos.add(atributo.toString());
                

                //Validacion valores null
                if (valoresCampos.contains(""))
                    throw new SecurityException("ERROR Uno valor del campo es nulo");
                
                if (nroCampos == -1 || longitud == -1 || campoClave == null)
                    throw new SecurityException("ERROR: No se ha encontrado los datos de la tabla especificada");
                

                if (valoresCampos.size() != nroCampos)
                    throw new SecurityException("ERROR: El numero total de valores ingresados no coincide con el numero de campos registrados");
                

                //Validacion de que el campo clave no se repita
                try {

                    CsvReader ar = new CsvReader("BaseDatos\\" + nombreTabla + ".csv");
                    int posicion = -1;
                    ar.readRecord();
                    for (int k = 0; k < ar.getColumnCount(); k++)
                        if (ar.get(k).equals(campoClave))
                            posicion = k;

                    if (posicion == -1)
                        throw new SecurityException("ERROR: No se han encontrado los datos de la tabla especificada");
                    
                    while (ar.readRecord())
                        if (ar.get(posicion).equals(valoresCampos.get(posicion)))
                            throw new SecurityException("ERROR: El valor correspondiente al campo clave ya existe");
                        
                    ar.close();

                } catch (FileNotFoundException ex) {
                    throw new SecurityException("Error al cargar los archivos");
                } catch (IOException ex) {
                    throw new SecurityException("ERROR DE PROCESO");
                }

                Object[] arg = {"INSERTAREN", nombreTabla, valoresCampos};
                new FacadeBD().CrearRegistro(arg);

                break;
            }
            case AR: //------------------------------------------
            {
                int i = "ACTUALIZAR REGISTRO ".length();

                String nombreTabla = null;
                String valorCampoClave = null;
                String nombreCampo = null;
                String valorCampo = null;

                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        break;
                    
                StringBuffer atributo = new StringBuffer();

                //Nombre de la tabla
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        atributo.append(entrada.charAt(i));
                    else break;
                    
                nombreTabla = atributo.toString();
                
                if (nombreTabla.isEmpty())
                    throw new SecurityException("ERROR: No se ha especificado el nombre de la tabla");

                atributo = new StringBuffer();

                //Palabra reservada CLAVE
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        break;
                
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        atributo.append(entrada.charAt(i));
                    else break;
                    
                if (!atributo.toString().equals("CLAVE"))
                    throw new SecurityException("ERROR DE SINTAXIS: Falta la palabra reservada CLAVE");
                

                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ') break;
                 

                //Campo clave 
                atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ') break;
                    
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        atributo.append(entrada.charAt(i));
                    else break;
                    
                valorCampoClave = atributo.toString();
                if (valorCampoClave.isEmpty())
                    throw new SecurityException("ERROR: No se ha especificado el campo clave");
                
                //Palabra reservada CAMPO
                atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ') break;
                        
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        atributo.append(entrada.charAt(i));
                    else break;
                    
                if (!atributo.toString().equals("CAMPO"))
                    throw new SecurityException("ERROR DE SINTAXIS: Falta la palabra reservada CAMPO");
                
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ') break;
                    
                //Campo
                atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ') break;
                    
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        atributo.append(entrada.charAt(i));
                    else break;
                    
                nombreCampo = atributo.toString();
                if (nombreCampo.isEmpty())
                    throw new SecurityException("ERROR: No se ha especificado el nombre del campo a modificar");
                
                //Palabra reservada POR
                atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        break;
                    
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        atributo.append(entrada.charAt(i));
                    else break;
                   
                if (!atributo.toString().equals("POR"))
                    throw new SecurityException("ERROR DE SINTAXIS: Falta la palabra reservada POR");
                
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ') break;
                        
                //Nuevo valor del campo
                atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ') break;
                    
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        atributo.append(entrada.charAt(i));
                    else break;
                    
                valorCampo = atributo.toString();
                if (valorCampo.isEmpty())
                    throw new SecurityException("ERROR: No se ha especificado el nuevo valor del campo");
                
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        throw new SecurityException("ERROR: Existen caracteres de mas en la sentencia");
                    
                //Validacion de tabla existente
                if (!this.BuscarTabla(nombreTabla))
                    throw new SecurityException("ERROR: La tabla especificada no existe");
               
                //Validacion tabla con registros
                try {
                    CsvReader read = new CsvReader("BaseDatos\\" + nombreTabla + ".csv");
                    int cont = 0;
                    while (read.readRecord()) {
                        cont++;
                    }
                    if (cont <= 1) {
                        throw new SecurityException("ERROR: La tabla especificada no cuenta con registros");
                    }
                    read.close();
                } catch (FileNotFoundException ex) {
                    throw new SecurityException("Error al cargar los archivos");
                } catch (IOException ex) {
                    throw new SecurityException("ERROR DE PROCESO");
                }

                //Validaciones de existencia y longitud
                String campoClaveReal = null;
                String fileINFORMACION = "BaseDatos\\Informacion.csv";
                int posicionCampo = -1;
                boolean estadoEncontrado = false;
                boolean estadoEncontradoClave = false;
                boolean estadoencriptado = false;
                boolean estadovcencript = false;
                try {
                    CsvReader ar = new CsvReader(fileINFORMACION);
                    while (ar.readRecord()) {
                        //Existencia del campo
                        if (ar.get(0).equals(nombreTabla)) {
                            estadoEncontrado = false;
                            estadoEncontradoClave = false;
                            estadoencriptado = false;
                            estadovcencript = false;
                            campoClaveReal = ar.get(2);
                            for (int w = ((ar.getColumnCount() - 3) / 3) + 3; w < (2 * (ar.getColumnCount() - 3) / 3) + 3; w++) {
                                if (ar.get(w).equals(nombreCampo)) {
                                    estadoEncontrado = true;
                                    if ("T".equals(ar.get((((ar.getColumnCount() - 3) / 3) + w)))) {
                                        estadoencriptado = true;
                                    }
                                }
                                if (ar.get(w).equals(campoClaveReal)) {
                                    estadoEncontradoClave = true;
                                    posicionCampo = w;
                                    if ("T".equals(ar.get((((ar.getColumnCount() - 3) / 3) + w)))) {
                                        estadovcencript = true;
                                    }
                                }
                            }
                            posicionCampo = posicionCampo - ((ar.getColumnCount() - 3) / 3) - 3;
                            //Errores varios
                            if (estadoEncontrado == false) {
                                throw new SecurityException("ERROR: El nombre del campo especificado no existe");
                            }
                            if (estadoEncontradoClave == false) {
                                throw new SecurityException("ERROR: El nombre del campo clave especificado no existe");
                            }
                            if (ar.get(2).equals(nombreCampo)) {
                                throw new SecurityException("ERROR: No es permite modificar el valor del campo clave");
                            }
                            if (valorCampo.length() > Integer.parseInt(ar.get(3))) {
                                throw new SecurityException("ERROR: El nuevo valor del campo excede el maximo de longitud establecido");
                            }

                            break;
                        }
                    }
                    ar.close();

                    CsvReader read = new CsvReader("BaseDatos\\" + nombreTabla + ".csv");
                    estadoEncontrado = false;
                    read.readRecord();
                    while (read.readRecord()) {
                        String comparacion;
                        if (estadovcencript) {
                            AESEncryption encript = new AESEncryption();
                            comparacion = encript.decryptText(read.get(posicionCampo), encript.getSecretEncryptionKey());
                        } else
                            comparacion = read.get(posicionCampo);
                        
                        if (comparacion.equals(valorCampoClave)) {
                            estadoEncontrado = true;
                            break;
                        }
                    }
                    read.close();
                    read.close();
                    if (!estadoEncontrado)
                        throw new SecurityException("ERROR: No existe un registro con ese valor de campo clave");
                    
                } catch (IOException | NumberFormatException e) {
                    throw new SecurityException("Error al cargar los archivos");
                } catch (Exception ex) {
                    throw new SecurityException("Error al cargar los archivos");
                }
                if (campoClaveReal == null) {
                    throw new SecurityException("ERROR: No se han encontrado los datos de la tabla especificada");
                }

                if (estadoencriptado) {
                    try {
                        valorCampo = encript.bytesToHex(encript.encryptText(valorCampo, encript.getSecretEncryptionKey()));
                    } catch (Exception ex) {throw new SecurityException("ERROR: Encriptacion");}
                }
 
                Object[] arg = {"ACTUALIZARREGISTRO", nombreTabla, valorCampoClave, nombreCampo, valorCampo, posicionCampo};
                new FacadeBD().ActualizarRegistro(arg);

                break;
            }
            case DR: //-------------------------------------------
            {
                String nombreTabla = null;
                String valorCampo = null;
                
                int i = "BORRAR REGISTRO ".length();

                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ') break;
                    
                StringBuffer atributo = new StringBuffer();

                //Nombre de la tabla
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        atributo.append(entrada.charAt(i));
                    else break;
                
                nombreTabla = atributo.toString();
                               
                if (nombreTabla.isEmpty())
                    throw new SecurityException("ERROR, No se ha especificado el nombre de la tabla");

                atributo = new StringBuffer();

                //Palabra reservada CLAVE
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        break;
                
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        atributo.append(entrada.charAt(i)); else break;
                    

                if (!atributo.toString().equals("CLAVE"))
                    throw new SecurityException("ERROR DE SINTAXIS: Falta la palabra reservada CLAVE");

                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        break;

                //Campo clave 
                atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        break;
                
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        atributo.append(entrada.charAt(i));
                    else break;
                 

                valorCampo = atributo.toString();
                
                if (valorCampo.isEmpty())
                    throw new SecurityException("ERROR: No se ha especificado el valor del campo clave");
                
                
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ') 
                        throw new SecurityException("ERROR: Existen caracteres de mas en la sentencia");
                
                String fileINFORMACION = "BaseDatos\\Informacion.csv";
                
                int posicion = -1;
                
                try {    
                    String campoClaveReal = null;
                    CsvReader ar = new CsvReader(fileINFORMACION);
                    while (ar.readRecord()) {
                        if (ar.get(0).equals(nombreTabla)) {
                            if(ar.get(1).equals("0"))
                                throw new SecurityException("No hay Regitros En la Tabla");
                            
                            int numCampos= (ar.getColumnCount()-3)/3;
                            
                            campoClaveReal = ar.get(2);
                            
                            for (int k = 0; k < numCampos; k++) {
                                if (ar.get(k+3+numCampos).equals(campoClaveReal)) {
                                    posicion = k;
                                    if("T".equals(ar.get(k+3+2*numCampos)))
                                        try {valorCampo= encript.bytesToHex(encript.encryptText(valorCampo,encript.getSecretEncryptionKey()));
                                        }catch (Exception e){throw new SecurityException("ERROR: En encriptación");}
                                    break;
                                }
                            }
                            break;
                        }
                    
                    }
                    ar.close();
                    ar.close();
                    
                } catch (FileNotFoundException ex) {
                    throw new SecurityException("Error al cargar los archivos");
                } catch (IOException ex) {
                    throw new SecurityException("Error al cargar los archivos");
                } catch (SecurityException ex) {
                    throw new SecurityException(ex);
            }
                
                Object[] arg = {"BORRARREGISTRO", nombreTabla, valorCampo, posicion};
                new FacadeBD().EliminarRegistro(arg);

                break;
            }
            case SELECT://-------------------------------------------
            {
                int i = "SELECCIONAR DE ".length();

                String nombreTabla = null;
                String nombreCampo = null;
                String valorCampo = null;

                int maximo = -1;

                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ') break;

                StringBuffer atributo = new StringBuffer();

                //Nombre de la tabla
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        atributo.append(entrada.charAt(i));
                    else break;
                    
                nombreTabla = atributo.toString();

                if (nombreTabla.isEmpty())
                    throw new SecurityException("ERROR: No se ha especificado el nombre de la tabla");
                
                atributo = new StringBuffer();

                //Palabra reservada DONDE
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ') break;
                        
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ') break;
                
                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        atributo.append(entrada.charAt(i));
                    else break;
                    
                if (!atributo.toString().equals("DONDE"))
                    throw new SecurityException("ERROR DE SINTAXIS: Falta la palabra reservada DONDE");
                
                atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++)
                    atributo.append(entrada.charAt(i));
                
                if (!atributo.toString().contains("="))
                    throw new SecurityException("ERROR DE SINTAXIS: Falta el operador =");
                
                entrada = atributo.toString().replace(" ", "");
                //Palabra del campo de atributo campo

                atributo = new StringBuffer();
                for (i = 0; i < entrada.indexOf("="); i++)
                    atributo.append(entrada.charAt(i));
                
                nombreCampo = atributo.toString();

                if (!entrada.contains("\""))
                    throw new SecurityException("ERROR DE SINTAXIS: Faltan las comillas \" para especificar el valor de busqueda");
                
                if (entrada.charAt(i + 1) != '\"')
                    throw new SecurityException("ERROR DE SINTAXIS: faltan las comillas \" para especificar el valor de busqueda");
                

                //Valor
                atributo = new StringBuffer();
                for (i = i + 2; i < entrada.length(); i++)
                    atributo.append(entrada.charAt(i));
                
                entrada = atributo.toString();
                if (!entrada.toString().contains("\""))
                    throw new SecurityException("ERROR DE SINTAXIS: faltan las comillas \" para especificar el valor de busqueda");
                
                atributo = new StringBuffer();
                for (i = 0; i < entrada.indexOf("\""); i++)
                    atributo.append(entrada.charAt(i));
                
                valorCampo = atributo.toString();
                if (nombreCampo.isEmpty())
                    throw new SecurityException("ERROR: No se ha especificado el nombre del campo");
                
                if (valorCampo.isEmpty())
                    throw new SecurityException("Error: No se ha especificado el valor del campo");
               
                atributo = new StringBuffer();
                for (i = i + 1; i < entrada.length(); i++)
                    atributo.append(entrada.charAt(i));
                
                entrada = atributo.toString();
                while (entrada.length() != 0) {
                    if (entrada.length() > "ORDENADO".length() && "ORDENADO".equals(entrada.substring(0, "ORDENADO".length()))) {
                        entrada = entrada.substring("ORDENADO".length());
                        if (entrada.length() >= "asc".length() && "asc".equals(entrada.substring(0, "asc".length()))) {
                            ordenamiento.FormatoArchivo.asc = true;
                            entrada = entrada.substring("asc".length());
                        } else if (entrada.length() >= "desc".length() && "desc".equals(entrada.substring(0, "desc".length()))) {
                            ordenamiento.FormatoArchivo.asc = false;
                            entrada = entrada.substring("desc".length());
                        } else {
                            throw new SecurityException("ERROR: Opcion incorrecta de ORDENADO");
                        }

                    } else if (entrada.length() > "VER".length() && "VER".equals(entrada.substring(0, "VER".length()))) {
                        entrada = entrada.substring("VER".length());
                        atributo = new StringBuffer();
                        for (i = 0; i < entrada.length(); i++) {
                            if ('O' == entrada.charAt(i)) {
                                break;
                            }
                            atributo.append(entrada.charAt(i));
                        }
                        entrada = entrada.substring(i);
                        try {
                            maximo = Integer.parseInt(atributo.toString());
                            if (maximo < 1) {
                                throw new SecurityException("ERROR: Valor maximo de registros DENEGADO");
                            }
                        } catch (Exception e) {
                            throw new SecurityException("ERROR: Opcion incorrecta de ORDENAMIENTO");
                        }
                    } else {
                        throw new SecurityException("ERROR: Existen datos de mas e la sentencia");
                    }
                }

                //Validacion de tabla existente
                if (!this.BuscarTabla(nombreTabla)) {
                    throw new SecurityException("ERROR: El nombre de la tabla especificada no existe");
                }

                //Validacion de que el campo especificado exista...
                int posicion = -1;
                
                String fileINFORMACION = "BaseDatos\\Informacion.csv";

                Boolean encriptado[] = null;

                try {
                    CsvReader ar = new CsvReader(fileINFORMACION);
                    while (ar.readRecord()) {
                        if (ar.get(0).equals(nombreTabla)) {
                            ordenamiento.FormatoArchivo.numCampos = (ar.getColumnCount() - 3) / 3;
                            encriptado = new Boolean[ordenamiento.FormatoArchivo.numCampos];
                            for (int k = 0; k < ordenamiento.FormatoArchivo.numCampos; k++) {
                                encriptado[k] = (ar.get(3 + 2 * ordenamiento.FormatoArchivo.numCampos + k).equals("T")) ? true : false;
                                if (ar.get(3 + ordenamiento.FormatoArchivo.numCampos + k).equals(nombreCampo)) {
                                    posicion = k;
                                }
                                if (ar.get(3 + ordenamiento.FormatoArchivo.numCampos + k).equals(ar.get(2))) {
                                    ordenamiento.FormatoArchivo.indice = k;
                                    OrdenamientoBalanceado.encriptado = encriptado[k];
                                }
                            }
                            break;
                        }
                    }
                    ar.close();
                } catch (FileNotFoundException ex) {
                    throw new SecurityException("Error al cargar los archivos");
                } catch (IOException ex) {
                    throw new SecurityException("Error al cargar los archivos");
                }
                if (posicion == -1) {
                    throw new SecurityException("ERROR: La tabla: " + nombreTabla + " no posee ese campo");
                }

                Object[] arg = {"SELECCIONARDE", nombreTabla, nombreCampo, valorCampo, posicion, encriptado, maximo};
                new FacadeBD().Select(arg);

                break;
            }
            case UNION://--------------------------------------------------------
            {
                int i = "UNIR ".length();

                String nombreTabla1 = null;
                String nombreTabla2 = null;
                String nombreCampo = null;
                String valorCampo = null;

                int maximo = -1;

                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ')
                        break;
                    
                //Listado de Campos
                StringBuffer atributo = new StringBuffer();
                for (i = i + 0; i < entrada.length(); i++)
                    atributo.append(entrada.charAt(i));
                
                //Listado de campos
                String comando2 = atributo.toString().replace(" ", "");
                atributo = new StringBuffer();

                if (!entrada.contains("POR"))
                    throw new SecurityException("ERROR DE SINTAXIS: Falta la palabra reservada POR");
                
                if (!entrada.contains(","))
                    throw new SecurityException("ERROR: Falta especificar el nombre de las tablas");
                
                for (i = 0; i < comando2.indexOf("POR"); i++) {
                    if (comando2.charAt(i) != ',') {
                        atributo.append(comando2.charAt(i));
                    } else {
                        nombreTabla1 = atributo.toString();
                        atributo = new StringBuffer();
                        entrada = entrada.replace(nombreTabla1, "");
                    }
                }
                nombreTabla2 = atributo.toString();
                entrada = entrada.replace(nombreTabla2, "");
                if (nombreTabla1.isEmpty())
                    throw new SecurityException("ERROR: No se ha especificado el nombre de la primera tabla");
                
                if (nombreTabla2.isEmpty())
                    throw new SecurityException("ERROR: No se ha especificado el nombre de la segunda tabla");
                
                //Palabra reservada POR
                atributo = new StringBuffer();
                i = entrada.indexOf("POR") + 3;

                for (i = i + 0; i < entrada.length(); i++)
                    if (entrada.charAt(i) != ' ') break;
                    
                entrada = entrada.substring(i).replace(" ", "");
                atributo = new StringBuffer();
                for (i = 0; i < entrada.indexOf("="); i++) 
                    atributo.append(entrada.charAt(i));
                
                nombreCampo = atributo.toString();
                if (!entrada.contains("\""))
                    throw new SecurityException("ERROR DE SINTAXIS: Faltan las comillas \" para especificar el valor de busqueda");
                
                if (entrada.charAt(i + 1) != '\"')
                    throw new SecurityException("ERROR DE SINTAXIS: Faltan las comillas \" para especificar el valor de busqueda");
                

                //Valor
                atributo = new StringBuffer();
                for (i = i + 2; i < entrada.length(); i++)
                    atributo.append(entrada.charAt(i));
                
                entrada = atributo.toString();
                if (!entrada.toString().contains("\""))
                    throw new SecurityException("ERROR DE SINTAXIS: Faltan las comillas \" para especificar el valor de busqueda");
                
                atributo = new StringBuffer();
                for (i = 0; i < entrada.indexOf("\""); i++)
                    atributo.append(entrada.charAt(i));

                valorCampo = atributo.toString();
                if (nombreCampo.isEmpty())
                    throw new SecurityException("ERROR: No se ha especificado el nombre del campo");
                
                if (valorCampo.isEmpty())
                    throw new SecurityException("ERROR: No se ha especificado el valor del campo");
                
                atributo = new StringBuffer();
                for (i = i + 1; i < entrada.length(); i++)
                    atributo.append(entrada.charAt(i));

                entrada = atributo.toString();
                while (entrada.length() != 0) {
                    if (entrada.length() > "ORDENADO".length() && "ORDENADO".equals(entrada.substring(0, "ORDENADO".length()))) {
                        entrada = entrada.substring("ORDENADO".length());
                        if (entrada.length() >= "asc".length() && "asc".equals(entrada.substring(0, "asc".length()))) {
                            ordenamiento.FormatoArchivo.asc = true;
                            entrada = entrada.substring("asc".length());
                        } else if (entrada.length() >= "desc".length() && "desc".equals(entrada.substring(0, "desc".length()))) {
                            ordenamiento.FormatoArchivo.asc = false;
                            entrada = entrada.substring("desc".length());
                        } else {
                            throw new SecurityException("ERROR: Opcion incorrecta de ORDENADO");
                        }
                    } else if (entrada.length() > "VER".length() && "VER".equals(entrada.substring(0, "VER".length()))) {
                        entrada = entrada.substring("VER".length());
                        atributo = new StringBuffer();
                        for (i = 0; i < entrada.length(); i++) {
                            if ('O' == entrada.charAt(i)) {
                                break;
                            }
                            atributo.append(entrada.charAt(i));
                        }
                        entrada = entrada.substring(i);
                        try {
                            maximo = Integer.parseInt(atributo.toString());
                            if (maximo < 1) {
                                throw new SecurityException("ERROR: Valor maximo de registros DENEGADO");
                            }
                        } catch (Exception e) {
                            throw new SecurityException("ERROR Opcion incorrecta de ORDENAMIENTO");
                        }
                    } else {
                        throw new SecurityException("ERROR: Existen datos de mas en la sentencia");
                    }
                }

                ArrayList encriptado = new ArrayList();
                //---------------------------------------------------
                String fileINFORMACION = "BaseDatos\\Informacion.csv";
                //Validacion campo existentes en las tablas
                boolean estadoTabla1 = false;
                boolean estadoTabla2 = false;

                int tCampos1 = 0, tCampos2 = 0;
                int posicion1 = 0, posicion2 = 0;
                /* determina si esta o no encriptado en campo clave en 2*/
                Boolean encriptado2 = false, auxiliar = null;

                try {
                    CsvReader ar = new CsvReader(fileINFORMACION);

                    while (ar.readRecord() & (!estadoTabla1 | !estadoTabla2)) {
                        if (ar.get(0).equals(nombreTabla1) & !estadoTabla1) {
                            encriptado.add(true);
                            tCampos1 = (ar.getColumnCount() - 3) / 3;
                            for (int k = 0; k < tCampos1; k++) {
                                auxiliar = (ar.get(3 + 2 * tCampos1 + k).equals("T")) ? true : false;
                                if (ar.get(3 + tCampos1 + k).equals(ar.get(2))) {
                                    ordenamiento.FormatoArchivo.indice = encriptado.size();
                                    //System.out.println("**"+ordenamiento.FormatoArchivo.indice);
                                    OrdenamientoBalanceado.encriptado = auxiliar;
                                }
                                if (ar.get(3 + tCampos1 + k).equals(nombreCampo)) {
                                    estadoTabla1 = true;
                                    encriptado.set(0, auxiliar);
                                    posicion1 = k;
                                } else {
                                    encriptado.add(auxiliar);
                                }

                            }
                        } else if (ar.get(0).equals(nombreTabla2) & !estadoTabla2) {
                            tCampos2 = (ar.getColumnCount() - 3) / 3;
                            for (int k = 0; k < tCampos2; k++) {
                                auxiliar = (ar.get(3 + 2 * tCampos2 + k).equals("T")) ? true : false;
                                if (ar.get(3 + tCampos2 + k).equals(nombreCampo)) {
                                    estadoTabla2 = true;
                                    encriptado2 = auxiliar;
                                    posicion2 = k;
                                } else {
                                    encriptado.add(auxiliar);
                                }
                            }
                        }
                    }

                    ar.close();

                } catch (FileNotFoundException ex) {
                    throw new SecurityException("Error al cargar los datos");
                } catch (IOException ex) {
                    throw new SecurityException("Error al cargar los datos");
                }

                if (estadoTabla1 == false) {
                    throw new SecurityException("ERROR: La tabla: " + nombreTabla1 + " no posee el campo: " + nombreCampo);
                }

                if (estadoTabla2 == false) {
                    throw new SecurityException("ERROR: La tabla: " + nombreTabla2 + " no posee el campo: " + nombreCampo);
                }

                Object[] arg = {"UNIR", nombreTabla1, nombreTabla2, nombreCampo, valorCampo, encriptado, encriptado2, posicion1, posicion2, maximo};
                new FacadeBD().Join(arg);

                break;
            }
            case CARGAR://--------------------------------------------------------
            {
                /* Realiza una peticion a la capa de vistas */
                Desarrollador.cargar=true;
                break;
            }
            default:
                throw new SecurityException("ERROR: Comando no soportado");
        }
    }
}