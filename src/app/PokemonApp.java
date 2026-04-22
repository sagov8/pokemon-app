package app;

import controller.BatallaController;
import controller.JuegoController;
import factory.*;
import model.pokemon.*;
import persistence.GameRepository;
import view.ConsoleView;

public class PokemonApp {

    private final JuegoController juegoController;

    public PokemonApp() {
        ConsoleView vista = new ConsoleView();
        BatallaController batallaCtrl = new BatallaController(vista);
        GameRepository repo = new GameRepository();
        this.juegoController = new JuegoController(vista, batallaCtrl, repo);
    }

    public void iniciar() {
        juegoController.iniciar();
    }
}
