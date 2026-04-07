package view;

import model.pokemon.Movimiento;

import java.util.List;

public class MovimientoView {

    public void mostrarMovimientos(List<Movimiento> movimientos) {
        for (Movimiento movimiento : movimientos) {
            System.out.println();
            System.out.println("Nombre: " + movimiento.getNombre());
            System.out.println("Tipo: " + movimiento.getTipo());
            System.out.println("Poder: " + movimiento.getPoder());
            movimiento.usarMovimiento();
            System.out.println("Daño calculado: " + movimiento.calcularDanio());
        }
    }
}
