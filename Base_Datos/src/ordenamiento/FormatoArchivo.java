package ordenamiento;

public class FormatoArchivo {

    private final String[] campos;
    
    public static int indice=0,numCampos=0;
    public static Boolean asc = true;
    
    public FormatoArchivo(){
        campos = new String[numCampos];
    }
    
    public int compareTo(FormatoArchivo otroArchivo) {
        
        //System.out.println(this.campos[indice]+" : " + otroArchivo.campos[indice]+" = "+this.campos[indice].compareTo(otroArchivo.campos[indice]));
        if(asc)
            return this.campos[indice].compareTo(otroArchivo.campos[indice]);
        return -this.campos[indice].compareTo(otroArchivo.campos[indice]);
    }
    
    public void setCampoClave(String cadena){
        campos[indice]=cadena;
    }
    public String getCampoClave(){
        return campos[indice];
    }    
    public void setCampos(String[] campos) {
        System.arraycopy(campos, 0, this.campos, 0, campos.length);
    }
    
    public String[] getCampos(){
        return campos;
    }
}
