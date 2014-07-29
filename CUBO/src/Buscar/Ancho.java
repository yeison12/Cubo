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
public class Ancho extends Nodo {
       private Operar operador;
    
    public Ancho(Estado estado, Nodo padre, Operar operador) {
        super(estado, padre);
        this.operador = operador;
    }

    public Operar getOperador() {
        return operador;
    }

    public void setOperador(Operar operador) {
        this.operador = operador;
    }
    
    @Override
    public String toString(){
        return (super.toString()+"\nOPERADOR:"+operador.getEtiqueta());
    } 
}
