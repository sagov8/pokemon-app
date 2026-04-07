package service;

import factory.CreadorMovimiento;
import factory.CreadorMovimientoAgua;
import factory.CreadorMovimientoFuego;
import factory.CreadorMovimientoPsiquico;
import model.pokemon.Movimiento;

import java.util.ArrayList;
import java.util.List;

public class MovimientoService {

    public List<Movimiento> obtenerMovimientosDemo() {
        List<Movimiento> movimientos = new ArrayList<>();

        CreadorMovimiento creadorFuego = new CreadorMovimientoFuego();
        CreadorMovimiento creadorAgua = new CreadorMovimientoAgua();
        CreadorMovimiento creadorPsiquico = new CreadorMovimientoPsiquico();

        movimientos.add(creadorFuego.prepararMovimiento());
        movimientos.add(creadorAgua.prepararMovimiento());
        movimientos.add(creadorPsiquico.prepararMovimiento());

        return movimientos;
    }
}
