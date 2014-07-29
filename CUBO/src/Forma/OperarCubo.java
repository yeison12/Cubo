/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Forma;
import Buscar.Operar;
/**
 *
 * @author YEISON
 */
public class OperarCubo {
    private Mover movimiento;

    public OperarCubo(Mover movimiento) {
        this.movimiento = movimiento;
    }

    public Mover getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Mover movimiento) {
        this.movimiento = movimiento;
    }

    public String getEtiqueta() {
        return (movimiento.toString());
    }
    public double getCoste() {
        return (1);
    }
}
