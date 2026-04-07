package service;

import factory.*;
import model.pokemon.Movimiento;

import java.util.ArrayList;
import java.util.List;

public class MovimientoService {

    public List<Movimiento> obtenerMovimientosDemo() {
        List<Movimiento> movimientos = new ArrayList<>();

        CreadorMovimiento creadorFuego = new CreadorMovimientoFuego();
        CreadorMovimiento creadorAgua = new CreadorMovimientoAgua();
        CreadorMovimiento creadorPsiquico = new CreadorMovimientoPsiquico();
        CreadorMovimiento creadorTierra = new CreadorMovimientoTierra();
        CreadorMovimiento creadorAire = new CreadorMovimientoAire();
        CreadorMovimiento creadorElectrico = new CreadorMovimientoElectrico();

        movimientos.add(creadorFuego.prepararMovimiento());
        movimientos.add(creadorAgua.prepararMovimiento());
        movimientos.add(creadorPsiquico.prepararMovimiento());
        movimientos.add(creadorTierra.prepararMovimiento());
        movimientos.add(creadorAire.prepararMovimiento());
        movimientos.add(creadorElectrico.prepararMovimiento());

        return movimientos;
    }
}
