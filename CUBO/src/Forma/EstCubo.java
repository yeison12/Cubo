/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Forma;
import Buscar.Estado;
import Buscar.Operar;
import java.util.Vector;
/**
 *
 * @author YEISON
 */
public class EstCubo implements Estado{
 private static final Vector<Operar> listaOperadoresAplicables =
            EstCubo.inicializarListaOperadoresAplicables();
    private static Vector<Operar> inicializarListaOperadoresAplicables() {
        Vector<Operar> lista = new Vector<Operar>(Mover.movimientosPosibles.length);
        for (Mover m : Mover.movimientosPosibles) {
            lista.add((Operar) new OperarCubo(m));// modificar asi  "lista.add(new OperarCubo(m));"
        }
        return (lista);
    }
    private Fcubo cubo;

    public EstCubo(Fcubo cubo) {
        this.cubo = cubo;
    }

    public Fcubo getFcubo() {
        return cubo;
    }

    public void setFcubo(Fcubo cubo) {
        this.cubo = cubo;
    }
    public Vector<Operar> operadoresAplicables() {
        return (EstCubo.listaOperadoresAplicables);
    }
    public boolean esFinal() {
        return (cubo.esConfiguracionFinal());
    }
    public Estado aplicarOperador(OperarCubo o) {
        Fcubo nuevo = this.cubo.clone();
        nuevo.mover(o.getMovimiento());
        return  (new EstCubo(nuevo));// modificar ---" return  (new EstCubo(nuevo));"
    }

    @Override
    public int hashCode() {
        return (cubo.hashCode());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (this.getClass() != o.getClass())) {
            return false;
        }
        EstCubo e = (EstCubo) o;        
        return (cubo.equals(e.getFcubo()));
    }

    @Override
    public String toString() {
        return (cubo.toString());
    }
    
    public Estado aplicarOperador(Operar o) {
        return(aplicarOperador((OperarCubo) o));
    }   
}
