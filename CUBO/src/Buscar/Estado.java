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
public interface Estado {
    
    public Vector<Operar> operadoresAplicables();
    
    public boolean esFinal();
    
    public Estado aplicarOperador(Operar o);
    
    @Override
    public int hashCode();
    
    @Override
    public boolean equals(Object o);
    
    @Override
    public String toString();
    
}
