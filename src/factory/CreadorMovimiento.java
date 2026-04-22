package factory;

import model.pokemon.Movimiento;

public abstract class CreadorMovimiento {
    public Movimiento prepararMovimiento() {
        return crearMovimiento();
    }

    protected abstract Movimiento crearMovimiento();
}
