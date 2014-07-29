/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Forma;

/**
 *
 * @author YEISON
 */
public class Mover {
    public final static byte U = Fcubo.UP;
    public final static byte Ui = Mover.U + 6;
    public final static byte L = Fcubo.LEFT;
    public final static byte Li = Mover.L + 6;
    public final static byte F = Fcubo.FRONT;
    public final static byte Fi = Mover.F + 6;
    public final static byte R = Fcubo.RIGHT;
    public final static byte Ri = Mover.R + 6;
    public final static byte B = Fcubo.BACK;
    public final static byte Bi = Mover.B + 6;
    public final static byte D = Fcubo.DOWN;
    public final static byte Di = Mover.D + 6;

    public final static String[] etq_corta = {"U", "L", "F", "R", "B", "D","Ui", "Li", "Fi", "Ri", "Bi", "Di"};
    public final static String[] etq_larga = {"UP", "LEFT", "FRONT", "RIGHT", "BACK", "DOWN","UP inverso", "LEFT inverso", "FRONT inverso", "RIGHT inverso", "BACK inverso", "DOWN inverso"};
    public final static Mover[] movimientosPosibles = {
        new Mover(Mover.U), new Mover(Mover.L),
        new Mover(Mover.F), new Mover(Mover.R),
        new Mover(Mover.B), new Mover(Mover.D),
        new Mover(Mover.Ui), new Mover(Mover.Li),
        new Mover(Mover.Fi), new Mover(Mover.Ri),
        new Mover(Mover.Bi), new Mover(Mover.Di)
    };
    public byte tipo;
    public Mover(byte tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return (Mover.etq_corta[this.tipo]);
    }
}
