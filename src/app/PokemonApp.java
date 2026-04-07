package app;

import controller.MovimientoController;
import service.MovimientoService;
import view.MovimientoView;

public class PokemonApp {

    public void iniciar() {
        MovimientoView view = new MovimientoView();
        MovimientoService service = new MovimientoService();
        MovimientoController controller = new MovimientoController(service, view);

        controller.mostrarDemo();
    }
}
