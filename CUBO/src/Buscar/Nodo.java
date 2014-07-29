/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Buscar;

/**
 *
 * @author YEISON
 */
public class Nodo {
    private Estado estado;
    private Nodo padre;

    public Nodo(Estado estado, Nodo padre) {
        this.estado = estado;
        this.padre = padre;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Nodo getPadre() {
        return padre;
    }

    public void setPadre(Nodo padre) {
        this.padre = padre;
    }
    
    @Override
    public String toString() {
        return("ESTADO:"+estado.toString()+"\nPADRE"+padre);
    }
}
