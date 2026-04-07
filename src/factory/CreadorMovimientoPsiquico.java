package factory;

import model.pokemon.Movimiento;
import model.pokemon.MovimientoPsiquico;

public class CreadorMovimientoPsiquico extends CreadorMovimiento {

    @Override
    protected Movimiento crearMovimiento() {
        return new MovimientoPsiquico("Psíquico", 38);
    }
}
