/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Buscar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;
/**
 *
 * @author YEISON
 */
public class BusqAncho implements Busca{
    
    private LinkedList<Ancho> abiertos;
    private HashMap<Estado, Ancho> cerrados;
    
    public BusqAncho() {
        abiertos = new LinkedList<Ancho>();
        cerrados = new HashMap<Estado, Ancho>();
    }
    public Vector<Operar> buscarSolucion(Estado inicial) {
        Ancho nodoActual = null;
        Estado actual, hijo;
        boolean solucion = false;

        abiertos.clear();
        cerrados.clear();
        abiertos.add(new Ancho(inicial, null, null));
        while (!solucion && !abiertos.isEmpty()) {
            nodoActual = abiertos.poll();
            actual = nodoActual.getEstado();
            
            if (actual.esFinal()) {
                solucion = true;
            } else {
                cerrados.put(actual, nodoActual);             

                for (Operar operador : actual.operadoresAplicables()) {
                    hijo = actual.aplicarOperador(operador);
                    if (!cerrados.containsKey(hijo)) {
                        abiertos.addLast(new Ancho(hijo, nodoActual, operador));
                    }
                }
            }
        }
        
        if (solucion) {
            Vector<Operar> lista = new Vector<Operar>();
            Ancho nodo = nodoActual;

            while (nodo.getPadre() != null) {
                lista.insertElementAt(nodo.getOperador(), 0);
                nodo = (Ancho) nodo.getPadre();
            }
            return (lista);
        } else {
            return ((Vector<Operar>) null);
        }
    }
}
