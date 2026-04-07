package factory;

import model.pokemon.Movimiento;
import model.pokemon.MovimientoAire;

public class CreadorMovimientoAire extends CreadorMovimiento {

    @Override
    protected Movimiento crearMovimiento() {
        return new MovimientoAire("Vendaval", 110);
    }
}