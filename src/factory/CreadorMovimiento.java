package factory;

import model.pokemon.Movimiento;

public abstract class CreadorMovimiento {
    public Movimiento prepararMovimiento() {
        Movimiento movimiento = crearMovimiento();
        System.out.println("Movimiento creado correctamente.");
        return movimiento;
    }

    protected abstract Movimiento crearMovimiento();
}
