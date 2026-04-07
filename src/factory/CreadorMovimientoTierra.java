package factory;

import model.pokemon.Movimiento;
import model.pokemon.MovimientoTierra;

public class CreadorMovimientoTierra extends CreadorMovimiento {

    @Override
    protected Movimiento crearMovimiento() {
        return new MovimientoTierra("Terremoto", 80);
    }
}
