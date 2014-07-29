/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Buscar;

import java.util.Vector;

/**
 *
 * @author YEISON
 */
public class Problema {
    private Estado inicio;

    private Busca busca;

    public Problema(Estado inicia, Busca busca) {
        this.inicio = inicia;
        this.busca = busca;
    }

    public void setBuscador(Busca busca) {
        this.busca = busca;
    }

    public void setInicial(Estado inicia) {
        this.inicio = inicia;
    }
    public Vector<Operar> obtenerSolucion() {
        return(busca.buscarSolucion(inicio));
    }
}
