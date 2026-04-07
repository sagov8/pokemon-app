package factory;

import model.pokemon.Movimiento;
import model.pokemon.MovimientoElectrico;

public class CreadorMovimientoElectrico extends CreadorMovimiento {

    @Override
    protected Movimiento crearMovimiento() {
        return new MovimientoElectrico("Rayo", 90);
    }
}