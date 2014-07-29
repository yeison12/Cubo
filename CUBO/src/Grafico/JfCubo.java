
package Grafico;

import java.util.Vector;
import Buscar.BusqAncho;
import Buscar.Operar;
import Buscar.Problema;
import Forma.Fcubo;
import Forma.EstCubo;
import Buscar.Estado;
import Forma.Mover;
/**
 *
 * @author YEISON
 */

public class JfCubo extends javax.swing.JFrame {

    /** Creates new form JFrameRubik */
    public JfCubo() {
        initComponents();
    }
    private void bloquearComponentes() {
        btnDesordenar.setEnabled(false);
        btnInicializar.setEnabled(false);
        btnMover.setEnabled(false);
        btnResolver.setEnabled(false);
        jPanelRubik.setEnabled(false);
    }

    private void desbloquearComponentes() {
        btnDesordenar.setEnabled(true);
        btnInicializar.setEnabled(true);
        btnMover.setEnabled(true);
        btnResolver.setEnabled(true);
        jPanelRubik.setEnabled(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelRubik = new Grafico.JpCubo();
        panel1 = new java.awt.Panel();
        btnResolver = new javax.swing.JButton();
        txtResolver = new javax.swing.JTextField();
        btAplicar = new javax.swing.JButton();
        btnDesordenar = new javax.swing.JButton();
        spnPasos = new javax.swing.JSpinner();
        txtDesordenar = new javax.swing.JTextField();
        btnMover = new javax.swing.JButton();
        txtMover = new javax.swing.JTextField();
        btnInicializar = new javax.swing.JButton();
        btSalir = new javax.swing.JButton();

        setTitle("DEMO RUBIK");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnResolver.setText("Resolver");
        btnResolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResolverActionPerformed(evt);
            }
        });

        txtResolver.setEditable(false);

        btAplicar.setText("Aplicar");
        btAplicar.setEnabled(false);
        btAplicar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAplicarActionPerformed(evt);
            }
        });

        btnDesordenar.setText("Desordenar");
        btnDesordenar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesordenarActionPerformed(evt);
            }
        });

        spnPasos.setValue(1);

        txtDesordenar.setEditable(false);

        btnMover.setText("Mover");
        btnMover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoverActionPerformed(evt);
            }
        });

        btnInicializar.setText("Inicializar cubo");
        btnInicializar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicializarActionPerformed(evt);
            }
        });

        btSalir.setText("Salir");
        btSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnDesordenar)
                        .addGap(18, 18, 18)
                        .addComponent(spnPasos, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtResolver, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(btnResolver)
                        .addGap(18, 18, 18)
                        .addComponent(btAplicar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(txtMover, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtDesordenar, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnInicializar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(btnMover)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(btSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDesordenar)
                    .addComponent(spnPasos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtDesordenar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnMover)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtMover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnResolver)
                    .addComponent(btAplicar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtResolver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnInicializar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btSalir)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelRubik, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelRubik, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
private String resolverCubo(Fcubo r) {
        Problema pro = new Problema((Estado) new EstCubo((r)),
                                             new BusqAncho());
        Vector<Operar> solucion = pro.obtenerSolucion();
        String sol = "";
        for (Operar o : solucion) {
            sol = sol + o.getEtiqueta() + " ";
        }
        return (sol);
    }
private void moverCubo(String text) {
        if (text != null) {
            Vector<Mover> movimientos = new Vector<Mover>();
            String[] componentes = text.split(" ");

            for (String etq : componentes) {
                // Burca id del movimiento
                for (int i = 0; i < Mover.etq_corta.length; i++) {
                    if (etq.equalsIgnoreCase(Mover.etq_corta[i])) {
                        movimientos.add(Mover.movimientosPosibles[i]);
                    }
                }
            }
            if (movimientos.size() > 0) {
                jPanelRubik.animarCubo(movimientos);
            }
        }
}
private String desordenarCubo(int pasos) {
        Fcubo c = jPanelRubik.getCubo();
        Vector<Mover> movimientos = c.mezclar(pasos);
        jPanelRubik.setCubo(c);
        String strMovimientos = "";
        for (Mover m : movimientos) {
            strMovimientos = strMovimientos + m.toString() + " ";
        }
        return (strMovimientos);
}
private void inicializarCubo() {
        jPanelRubik.setCubo(new Fcubo());
}

    private void salir() {
        System.exit(0);
}
private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
   this.salir();
}//GEN-LAST:event_formWindowClosing

    private void btnResolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResolverActionPerformed
        // TODO add your handling code here:
        Runnable hiloBuscador = new Runnable() {
        public void run() {
            bloquearComponentes();
            String solucion ="";
            solucion = resolverCubo(jPanelRubik.getCubo());
            desbloquearComponentes();
            txtResolver.setText(solucion);
            txtDesordenar.setText("");
            if (!solucion.equals("")) {
                btAplicar.setEnabled(true);
            }
        }
    };
    new Thread(hiloBuscador).start();
    }//GEN-LAST:event_btnResolverActionPerformed

    private void btAplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAplicarActionPerformed
        moverCubo(txtResolver.getText());
        btAplicar.setEnabled(false);
    }//GEN-LAST:event_btAplicarActionPerformed

    private void btnDesordenarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesordenarActionPerformed
        String movimientos = desordenarCubo(((Integer) spnPasos.getValue()).intValue());
        txtDesordenar.setText(movimientos);
        txtResolver.setText("");
        btAplicar.setEnabled(false);
    }//GEN-LAST:event_btnDesordenarActionPerformed

    private void btnMoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoverActionPerformed
        txtResolver.setText("");
        txtDesordenar.setText("");
        moverCubo(txtMover.getText());
        btAplicar.setEnabled(false);
    }//GEN-LAST:event_btnMoverActionPerformed

    private void btnInicializarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicializarActionPerformed
        inicializarCubo();
        txtResolver.setText("");
        txtDesordenar.setText("");
    }//GEN-LAST:event_btnInicializarActionPerformed

    private void btSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalirActionPerformed
        this.salir();
    }//GEN-LAST:event_btSalirActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

                                    public void run() {
                                        new JfCubo().setVisible(true);
                                    }
                                });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAplicar;
    private javax.swing.JButton btSalir;
    private javax.swing.JButton btnDesordenar;
    private javax.swing.JButton btnInicializar;
    private javax.swing.JButton btnMover;
    private javax.swing.JButton btnResolver;
    private Grafico.JpCubo jPanelRubik;
    private java.awt.Panel panel1;
    private javax.swing.JSpinner spnPasos;
    private javax.swing.JTextField txtDesordenar;
    private javax.swing.JTextField txtMover;
    private javax.swing.JTextField txtResolver;
    // End of variables declaration//GEN-END:variables

}
