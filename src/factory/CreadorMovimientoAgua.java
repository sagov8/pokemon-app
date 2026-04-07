package factory;

import model.pokemon.Movimiento;
import model.pokemon.MovimientoAgua;

public class CreadorMovimientoAgua extends CreadorMovimiento {

    @Override
    protected Movimiento crearMovimiento() {
        return new MovimientoAgua("Pistola Agua", 35);
    }
}
