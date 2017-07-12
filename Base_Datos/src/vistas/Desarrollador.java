package vistas;

import com.csvreader.CsvReader;
import interfaces.GestionBD;
import java.awt.Color;
import static java.awt.Frame.NORMAL;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import logica.ArchivoExterno;
import logica.ProxyProtectorBD;

public class Desarrollador extends javax.swing.JFrame {

    public static String estadoOperacion = null;
    public static Boolean cargar = false;

    public Desarrollador() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Base de datos");
        Image iconoMenu = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("imagenes/logo.png"));
        this.setIconImage(iconoMenu);
        this.ActualizarListaTablas();
    }

    public void ActualizarListaTablas() {
        try {
            CsvReader read = new CsvReader("BaseDatos\\Informacion.csv");
            DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Tablas");
            while (read.readRecord()) {
                DefaultMutableTreeNode tbl = new DefaultMutableTreeNode();
                tbl.setUserObject(read.get(0));
                raiz.add(tbl);
            }
            DefaultTreeModel arbol = new DefaultTreeModel(raiz);
            this.jTree1.setModel(arbol);
            read.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    private void Ejecutar() {
        try {
            long startTimeMs = System.currentTimeMillis( );
    
            salidaConsola.setText("Ejecutando...");
            salidaConsola.setForeground(Color.BLACK);
            estadoOperacion = null;
            cargar=null;
            GestionBD gestion = new ProxyProtectorBD();
            Object[] arg = {comando.getText()};
            gestion.Peticion(arg);
            
            Long taskTimeMs  = (System.currentTimeMillis( ) - startTimeMs)/1000;
         
            this.tiempo.setText(taskTimeMs.toString());
            
        } catch (Exception se) {
            salidaConsola.setText(se.getMessage());
            salidaConsola.setForeground(Color.red);
        }
        if (estadoOperacion != null) {
            //Mensaje resulatado de la operacion
            salidaConsola.setText(estadoOperacion);
            salidaConsola.setForeground(Color.blue);
            this.ActualizarListaTablas();
        }if (cargar) {
            this.cargarArchivo();
            this.ActualizarListaTablas();
            cargar=false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comando = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        salidaConsola = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        tiempo = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        ejecutar = new javax.swing.JMenuItem();
        abrirArchivo = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        sintaxis = new javax.swing.JMenuItem();
        salir = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 410, 710, 180));

        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTree1);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 49, 170, 540));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("GESTOR DE BASE DE DATOS");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 0, -1, 40));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Registro de tablas");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Detalle de la Tabla");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 380, -1, -1));

        comando.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        comando.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comandoActionPerformed(evt);
            }
        });
        comando.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                comandoKeyTyped(evt);
            }
        });
        getContentPane().add(comando, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, 710, 50));

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Area de comandos");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, -1, -1));

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Salida de consola");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 250, -1, -1));

        salidaConsola.setEditable(false);
        salidaConsola.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        salidaConsola.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salidaConsolaActionPerformed(evt);
            }
        });
        getContentPane().add(salidaConsola, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 280, 710, 60));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 90, 750, 130));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo.png"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 240, 750, 120));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondoTabla.png"))); // NOI18N
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 370, 740, 240));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondoArbol.png"))); // NOI18N
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 210, 610));

        jLabel10.setText("s");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, 10, 20));

        tiempo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tiempo.setText("0");
        getContentPane().add(tiempo, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, 30, 20));

        jLabel12.setText("TIEMPO:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, 50, 20));

        jMenuBar1.setBackground(new java.awt.Color(255, 255, 255));

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/logo.png"))); // NOI18N
        jMenuBar1.add(jMenu3);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/archivo.png"))); // NOI18N
        jMenu1.setText("Archivo");
        jMenu1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        ejecutar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ejecutar.png"))); // NOI18N
        ejecutar.setText("Ejecutar");
        ejecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ejecutarActionPerformed(evt);
            }
        });
        jMenu1.add(ejecutar);

        abrirArchivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/abrir.png"))); // NOI18N
        abrirArchivo.setText("Abir");
        abrirArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirArchivoActionPerformed(evt);
            }
        });
        jMenu1.add(abrirArchivo);

        jMenuBar1.add(jMenu1);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ayuda.png"))); // NOI18N
        jMenu2.setText("Ayuda");
        jMenu2.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        sintaxis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/sintaxis.png"))); // NOI18N
        sintaxis.setText("Sintaxis");
        sintaxis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sintaxisActionPerformed(evt);
            }
        });
        jMenu2.add(sintaxis);

        salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.png"))); // NOI18N
        salir.setText("Salir");
        salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirActionPerformed(evt);
            }
        });
        jMenu2.add(salir);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comandoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comandoActionPerformed

    }//GEN-LAST:event_comandoActionPerformed

    private void salidaConsolaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salidaConsolaActionPerformed

    }//GEN-LAST:event_salidaConsolaActionPerformed

    private void ejecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ejecutarActionPerformed
        this.Ejecutar();
        this.comando.setText(null);
    }//GEN-LAST:event_ejecutarActionPerformed

    private void abrirArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abrirArchivoActionPerformed

    }//GEN-LAST:event_abrirArchivoActionPerformed

    private void sintaxisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sintaxisActionPerformed
        Sintaxis stx = new Sintaxis();
        stx.setVisible(true);
        dispose();
    }//GEN-LAST:event_sintaxisActionPerformed

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_salirActionPerformed

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        try {
            //Obtener el nombre de la tabla que fue seleccionado
            TreePath selPath = jTree1.getClosestPathForLocation(evt.getX(), evt.getY());
            Object nodo = selPath.getLastPathComponent();
            String nombreTabla = "" + nodo;
            if (nombreTabla.replace(" ", "").isEmpty()) {
                return;
            }
            //Numero de filas y columnas
            File path = new File("BaseDatos\\" + nombreTabla + ".csv");
            CsvReader reader = new CsvReader("BaseDatos\\" + nombreTabla + ".csv");
            int filas = 0;
            int columnas = 0;
            while (reader.readRecord()) {
                filas++;
                columnas = reader.getColumnCount();
            }
            filas--;
            reader.close();

            //Conseguir las cabeceras
            reader = new CsvReader("BaseDatos\\" + nombreTabla + ".csv");
            reader.readRecord();
            String matriz[][] = new String[filas][columnas];
            String cabecera[] = new String[columnas];
            for (int i = 0; i < columnas; i++) {
                cabecera[i] = reader.get(i);
            }

            //Conseguir el cuerpo de la tabla
            int i = 0;
            while (reader.readRecord()) {
                if (!reader.getRawRecord().isEmpty()) {
                    for (int j = 0; j < columnas; j++) {
                        matriz[i][j] = reader.get(j);
                    }
                }
                i++;
            }
            reader.close();

            //Creacion de la tabla
            DefaultTableModel tabla;
            tabla = new DefaultTableModel(matriz, cabecera);
            jTable1.setModel(tabla);

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }//GEN-LAST:event_jTree1MouseClicked

    private void comandoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comandoKeyTyped
        char tecla = evt.getKeyChar();
        if (tecla == KeyEvent.VK_ENTER) {
            this.Ejecutar();
            this.comando.setText(null);
        }
    }//GEN-LAST:event_comandoKeyTyped
/*
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Desarrollador().setVisible(true);
            }
        });
    }
  */
    
    public void cargarArchivo() {
        
        String rutaArchivo="";
        
        JFileChooser dlg = new JFileChooser();
        dlg.showOpenDialog(this);
        File abre = dlg.getSelectedFile();
        
        if(abre!=null && abre.isFile()){
            
            String titulo = "Base de Datos C-tri";
            String mensaje= " Nombre del Archivo:"+abre.getName()+
                   "\nDestino: Base de Datos C-Trie\n\nSi estas seguro de realizar la operacion\n"+
                    "presiona si para continuar\ncaso contrario... NADIE TE OBLIGA";
            
            rutaArchivo = abre.getAbsolutePath();
                
            int respuesta=JOptionPane.showOptionDialog(rootPane,mensaje,titulo, WIDTH, HEIGHT,null, null, NORMAL);
        
            if(respuesta ==0){
                String nombre=JOptionPane.showInputDialog("Escribe un nombre para la Tabla");
                String clave=JOptionPane.showInputDialog("¿Cual es el campo Clave?");
                        
                try{
                    ArchivoExterno.pasar_A_BaseDatos(abre,nombre,clave);
                    JOptionPane.showMessageDialog(null,"En hora buena, tu pedición se ha completado","Transacción Exitosa",JOptionPane.INFORMATION_MESSAGE);
                    //ActualizarListaTablas();
                }catch(Error e){this.salidaConsola.setText(e.toString());}
            }
            
        }else{
            JOptionPane.showMessageDialog(null,"No se ha encontrado el archivo","Atención",JOptionPane.WARNING_MESSAGE);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem abrirArchivo;
    private javax.swing.JTextField comando;
    private javax.swing.JMenuItem ejecutar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTable jTable1;
    private javax.swing.JTree jTree1;
    private javax.swing.JTextField salidaConsola;
    private javax.swing.JMenuItem salir;
    private javax.swing.JMenuItem sintaxis;
    private javax.swing.JLabel tiempo;
    // End of variables declaration//GEN-END:variables
}
