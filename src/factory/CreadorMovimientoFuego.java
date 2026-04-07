package factory;

import model.pokemon.Movimiento;
import model.pokemon.MovimientoFuego;

public class CreadorMovimientoFuego extends CreadorMovimiento {

    @Override
    protected Movimiento crearMovimiento() {
        return new MovimientoFuego("Lanzallamas", 40);
    }
}
