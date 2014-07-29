
package Grafico;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import Forma.Fcubo;
import Forma.Mover;

/**
 *
 * @author YEISON
 */

public class JpCubo extends JPanel {

    private AnimCube animCubo;
    private Fcubo cubo;

    
    public JpCubo() {
        this(new Fcubo());
    }

    
    public JpCubo(Fcubo c) {
        super();
        cubo = c;
        animCubo = new AnimCube();
        animCubo.setPreferredSize(new Dimension(250, 250));
        animCubo.init();
        animCubo.setCubo(c);
                
        this.setLayout(new BorderLayout());
        

        this.add(animCubo, BorderLayout.CENTER);
        JPanel aux = new JPanel();
       
    }

    public Fcubo getCubo() {
        return (animCubo.getCubo());
    }

    public void setCubo(Fcubo cubo) {
        this.animCubo.setCubo(cubo);
    }
    
    public void animarCubo(Vector<Mover> movimientos)  {
        Fcubo cuboActual = animCubo.getCubo();
        
        for (Mover m : movimientos){
            cuboActual.mover(m);
            animCubo.setCubo(cuboActual);
            
        }
        
    }

}
