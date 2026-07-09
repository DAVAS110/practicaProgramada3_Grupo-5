/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.fidelitas.java.practicaprogramada3_grupo5;

import service.GestorMatriculas;
import ui.Principal;

/**
 *
 * @author varga
 */
public class PracticaProgramada3_Grupo5 {

    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PracticaProgramada3_Grupo5.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PracticaProgramada3_Grupo5.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PracticaProgramada3_Grupo5.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PracticaProgramada3_Grupo5.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        GestorMatriculas gestor = new GestorMatriculas();
        Principal mf = new Principal(gestor);
        mf.setLocationRelativeTo(null);
        java.awt.EventQueue.invokeLater(() -> mf.setVisible(true));
    }
}
