package controller;

import model.pokemon.Movimiento;
import service.MovimientoService;
import view.MovimientoView;

import java.util.List;

public class MovimientoController {

    private final MovimientoService service;
    private final MovimientoView view;

    public MovimientoController(MovimientoService service, MovimientoView view) {
        this.service = service;
        this.view = view;
    }

    public void mostrarDemo() {
        List<Movimiento> movimientos = service.obtenerMovimientosDemo();
        view.mostrarMovimientos(movimientos);
    }
}
