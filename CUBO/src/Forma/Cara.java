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
public class Cara {
   public byte color;
   public Casilla[] casillas;
   
   public Cara(byte color) {
        this.color = color;
        this.casillas = new Casilla[9];
        this.inicializar();
    }
   public void inicializar() {
        for (byte i = 0; i < 9; i++) {
            this.casillas[i] = new Casilla(this.color, i);
        }
    }
   @Override
    public Cara clone() {
        Cara c = new Cara(this.color);
        System.arraycopy(this.casillas, 0,
                         c.casillas, 0, this.casillas.length);
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
        Cara c = (Cara) o;
        for (byte i = 0; i < this.casillas.length; i++) {
            if (this.casillas[i].getColor() != c.casillas[i].getColor()) {
                return false;
            }
        }
        return true;
    }
    @Override
    public int hashCode(){
        int hash=this.casillas[0].getColor();
        for (byte i=1; i< this.casillas.length-1; i++) {
            hash = hash << 3;
            hash = hash + this.casillas[i].getColor();
        }
        return(hash);
    }
}
