package main;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class SeleccionarMP3 extends javax.swing.JFrame implements Runnable {

    private Player apl;

    private Thread hilo;

    private static volatile boolean flag;

    public SeleccionarMP3() {
        setTitle("Seleccionar MP3");
        setResizable(false);
        initComponents();
        SeleccionarMP3.flag = true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jFileChooser1.setDialogTitle("");
        jFileChooser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(92, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooser1ActionPerformed

        Pattern coinci = Pattern.compile(".mp3$");
        File archivo = jFileChooser1.getSelectedFile();

        if (archivo != null) {

            Matcher arch = coinci.matcher(archivo.toString());
            boolean coin = arch.find();

            if (coin) {
                iniciar();

            } else {
                jFileChooser1.setSelectedFile(new File(""));
                JOptionPane.showMessageDialog(null, "Esto no es un archivo .MP3", "Error de Lectura", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "No se puede cancelar ya que no se ha seleccionado ningun archivo", "Error de Lectura", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_jFileChooser1ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SeleccionarMP3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SeleccionarMP3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SeleccionarMP3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SeleccionarMP3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SeleccionarMP3().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser jFileChooser1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {

        try {
            File archivo = jFileChooser1.getSelectedFile();
            apl = new Player(new FileInputStream(
                    archivo));
            this.dispose();
            Radio radio = new Radio(apl, hilo, archivo);
            radio.setVisible(true);

            while (flag) {
             
                apl.play();

            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Archivo no encontrado", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        } catch (JavaLayerException ex) {
            JOptionPane.showMessageDialog(null, "Error al reproducir", "Error de Archivo", JOptionPane.ERROR_MESSAGE);

        }
    }

    public void iniciar() {
        hilo = new Thread(this, "Reproducir");
        hilo.start();
    }

    public synchronized static boolean isFlag() {
        return flag;
    }

    public static void setFlag(boolean flag) {
        SeleccionarMP3.flag = flag;
    }

    public static int getInfoCancion(File archivo) {
        int duracion = 0;
        try {
            AudioFile cancion = AudioFileIO.read(archivo);
            Tag tag = cancion.getTag();
            AudioHeader audio = cancion.getAudioHeader();
            duracion = audio.getTrackLength();

        } catch (CannotReadException ex) {
            System.out.println("No se puede leer");
        } catch (IOException ex) {
            System.out.println("Error con el archivo");
        } catch (TagException ex) {
            System.out.println("Error con el tag");
        } catch (ReadOnlyFileException ex) {
            System.out.println("Solamente se puede leer");
        } catch (InvalidAudioFrameException ex) {
            System.out.println("Error de audio");
        }
        return duracion;
    }



}
