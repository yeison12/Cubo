/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Forma;
import java.util.Vector;
/**
 *
 * @author YEISON
 */
public class Fcubo {
    public final static byte UP = 0;
    public final static byte LEFT = 1;
    public final static byte FRONT = 2;
    public final static byte RIGHT = 3;
    public final static byte BACK = 4;
    public final static byte DOWN = 5;
    public final static byte[] ids_colores = {0, 1, 2, 3, 4, 5};
    public final static String[] etq_colores = {"W", "Y", "O", "R", "G", "B"};
    public static final byte[] vecinoNorte = {4, 0, 0, 0, 0, 2};
    public static final byte[] vecinoEste  = {3, 2, 3, 4, 1, 3};
    public static final byte[] vecinoSur   = {2, 5, 5, 5, 5, 4};
    public static final byte[] vecinoOeste = {1, 4, 1, 2, 3, 1};
    public static final byte[][] idxNorte = {{2, 1, 0},{0, 7, 6},{6, 5, 4},{4, 3, 2},{2, 1, 0},{6, 5, 4}};
    public static final byte[][] idxEste = {{2, 1, 0},{0, 7, 6},{0, 7, 6},{0, 7, 6},{0, 7, 6},{6, 5, 4}};
    public static final byte[][] idxSur = {{2, 1, 0},{0, 7, 6},{2, 1, 0},{4, 3, 2},{6, 5, 4},{6, 5, 4}};
    public static final byte[][] idxOeste = {{2, 1, 0},{4, 3, 2},{4, 3, 2},{4, 3, 2},{4, 3, 2},{6, 5, 4}};
    public Cara[] caras;
    public Fcubo() {
        caras = new Cara[6];
        for (byte i = 0; i < 6; i++) {
            caras[i] = new Cara(i);
        }
    }
    public boolean esConfiguracionFinal() {
        for (Cara c: caras) {
            for (byte i=0; i< c.casillas.length; i++) {
                if (c.casillas[i].getColor() != c.color) {
                    return(false);
                }
            }
        }
        return(true);
    }
    public void inicialiar() {
        for (byte i = 0; i < 6; i++) {
            caras[i].inicializar();
        }
    }
    public Vector<Mover> mezclar(){
        return(mezclar((int) Math.round(20*Math.random())));
    }
    public Vector<Mover> mezclar(int pasos){
        Vector<Mover> listaMovimientos = new Vector<Mover>(pasos);
        int idMovimiento;
        
        for (int i=0; i < pasos; i++) {
            idMovimiento = (int) Math.round(Math.random() * 
                    (Mover.movimientosPosibles.length-1));
            mover(Mover.movimientosPosibles[idMovimiento]);
            listaMovimientos.add(Mover.movimientosPosibles[idMovimiento]);
        }
        return(listaMovimientos);
    }
    public void mover(Mover m) {
        if (m.tipo < 6) {
            girarHorario(m.tipo);
        } else {
            girarAntiHorario((byte) (m.tipo - 6));
        }
    }
    public void mover(Vector<Mover> v) {
        for (Mover m : v) {
            this.mover(m);
        }
    }
    public Mover[] movientosPosibles() {
        return (Mover.movimientosPosibles);
    }
    private void girarHorario(byte idxCara) {
        Casilla aux1, aux2, aux3;

        girarCaraHorario(caras[idxCara]);

        for (byte i = 0; i < 3; i++) {
            aux1 = caras[vecinoEste[idxCara]].casillas[idxEste[idxCara][i]];
            caras[vecinoEste[idxCara]].casillas[idxEste[idxCara][i]] =
                    caras[vecinoNorte[idxCara]].casillas[idxNorte[idxCara][i]];
            aux2 = caras[vecinoSur[idxCara]].casillas[idxSur[idxCara][i]];
            caras[vecinoSur[idxCara]].casillas[idxSur[idxCara][i]] = aux1;
            aux3 = caras[vecinoOeste[idxCara]].casillas[idxOeste[idxCara][i]];
            caras[vecinoOeste[idxCara]].casillas[idxOeste[idxCara][i]] = aux2;
            caras[vecinoNorte[idxCara]].casillas[idxNorte[idxCara][i]] = aux3;
        }
    }
    private void girarAntiHorario(byte idxCara) {
        Casilla aux1, aux2, aux3;

        girarCaraAntiHorario(caras[idxCara]);

        for (byte i = 0; i < 3; i++) {
            aux1 = caras[vecinoOeste[idxCara]].casillas[idxOeste[idxCara][i]];
            caras[vecinoOeste[idxCara]].casillas[idxOeste[idxCara][i]] =
                    caras[vecinoNorte[idxCara]].casillas[idxNorte[idxCara][i]];
            aux2 = caras[vecinoSur[idxCara]].casillas[idxSur[idxCara][i]];
            caras[vecinoSur[idxCara]].casillas[idxSur[idxCara][i]] = aux1;
            aux3 = caras[vecinoEste[idxCara]].casillas[idxEste[idxCara][i]];
            caras[vecinoEste[idxCara]].casillas[idxEste[idxCara][i]] = aux2;
            caras[vecinoNorte[idxCara]].casillas[idxNorte[idxCara][i]] = aux3;
        }
    }
    private void girarCaraHorario(Cara cara) {
        Casilla[] copia = new Casilla[9];
        System.arraycopy(cara.casillas, 0, copia, 0, cara.casillas.length);
        for (byte i = 0; i < 8; i++) {
            cara.casillas[(i + 2) % 8] = copia[i];
        }
    }
    private void girarCaraAntiHorario(Cara cara) {
        Casilla[] copia = new Casilla[9];
        System.arraycopy(cara.casillas, 0, copia, 0, cara.casillas.length);
        for (byte i = 0; i < 8; i++) {
            cara.casillas[i] = copia[(i + 2) % 8];
        }
    }
    @Override
    public Fcubo clone() {
        Fcubo c = new Fcubo();

        for (byte i = 0; i < 6; i++) {
            c.caras[i].color = this.caras[i].color;
            System.arraycopy(this.caras[i].casillas, 0,
                             c.caras[i].casillas, 0, this.caras[i].casillas.length);
        }

        return (c);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (this.getClass() != o.getClass())) {
            return false;
        }
        Fcubo c = (Fcubo) o;
        for (byte i = 0; i < 6; i++) {
            if (!this.caras[i].equals(c.caras[i])) {
                return false;
            }
        }
        return true;
    }
    @Override
    public int hashCode() {
        int highorder;
        int hash = this.caras[0].hashCode();
        for (byte i = 1; i < 6; i++) {
            highorder = hash & 0xf8000000;
            hash = hash << 5;
            hash = hash ^ (highorder >> 27);
            hash = hash ^ this.caras[i].hashCode();
        }
        return (hash);
    }
    @Override
    public String toString() {
        String resultado;
        resultado = "    " + stringFila1(caras[0]) + "\n" +
                "    " + stringFila2(caras[0]) + "\n" +
                "    " + stringFila3(caras[0]) + "\n\n";
        resultado += stringFila1(caras[1]) + " " + stringFila1(caras[2]) + " " +
                stringFila1(caras[3]) + " " + stringFila1(caras[4]) + "\n" +
                stringFila2(caras[1]) + " " + stringFila2(caras[2]) + " " +
                stringFila2(caras[3]) + " " + stringFila2(caras[4]) + "\n" +
                stringFila3(caras[1]) + " " + stringFila3(caras[2]) + " " +
                stringFila3(caras[3]) + " " + stringFila3(caras[4]) + "\n\n";
        resultado += "    " + stringFila1(caras[5]) + "\n" +
                "    " + stringFila2(caras[5]) + "\n" +
                "    " + stringFila3(caras[5]) + "\n\n";
        return (resultado);
    }
    private String stringFila1(Cara cara) {
        return (etq_colores[cara.casillas[0].getColor()] +
                etq_colores[cara.casillas[1].getColor()] +
                etq_colores[cara.casillas[2].getColor()]);
    }

    private String stringFila2(Cara cara) {
        return (etq_colores[cara.casillas[7].getColor()] +
                etq_colores[cara.casillas[8].getColor()] +
                etq_colores[cara.casillas[3].getColor()]);
    }

    private String stringFila3(Cara cara) {
        return (etq_colores[cara.casillas[6].getColor()] +
                etq_colores[cara.casillas[5].getColor()] +
                etq_colores[cara.casillas[4].getColor()]);
    }
}
