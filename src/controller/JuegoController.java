package controller;

import model.Inventario;
import model.Objeto;
import model.pokemon.*;
import persistence.GameRepository;
import util.*;
import view.ConsoleView;

import java.io.IOException;
import java.util.List;

public class JuegoController {
    private final ConsoleView vista;
    private final BatallaController batallaController;
    private final GameRepository repositorio;

    private Entrenador jugador;

    public JuegoController(ConsoleView vista, BatallaController batallaController, GameRepository repositorio) {
        this.vista             = vista;
        this.batallaController = batallaController;
        this.repositorio = repositorio;
    }

    // Entrada principal

    public void iniciar() {
        vista.mostrarBienvenida();

        if (repositorio.existeGuardado()) {
            int op = vista.pedirOpcionMenu("Se encontró una partida guardada",
                    "Continuar partida", "Nueva partida", "Salir");
            switch (op) {
                case 1 -> cargarPartida();
                case 2 -> nuevaPartida();
                default -> vista.mostrarMensaje("Opción inválida. Iniciando nueva partida...");
            }
        } else {
            int op = vista.pedirOpcionMenu("Menú principal",
                    "Nueva partida", "Salir");
            if (op == 1) {
                nuevaPartida();
            } else {
                vista.mostrarMensaje("opción invalida");
            }
        }
    }

    // Nueva partida

    private void nuevaPartida() {
        String nombre = vista.pedirTexto("¿Cuál es tu nombre, entrenador?");
        jugador = new Entrenador(1, nombre, 1000);

        asignarInventarioInicial();

        Pokemon starter = elegirStarter();
        jugador.agregarPokemonEquipo(starter);
        vista.mostrarMensaje("\n¡" + starter.getNombre() + ", te elijo a ti!\n");

        bucleJuego();
    }

    private void asignarInventarioInicial() {
        Inventario inicial = ObjetoCatalogo.inventarioInicial();
        inicial.getObjetos().forEach(o -> jugador.getInventario().agregarObjeto(o));
    }

    // Elección de starter

    private Pokemon elegirStarter() {
        List<Pokemon> starters = PokemonCatalogo.getStarters();
        vista.mostrarStarters(starters);
        int eleccion = vista.leerOpcion(starters.size());

        // Crea instancia fresca para evitar compartir la misma referencia
        return PokemonCatalogo.crearPorId(starters.get(eleccion - 1).getId());
    }

    // Bucle de juego

    private void bucleJuego() {
        boolean corriendo = true;
        while (corriendo) {
            int opcion = vista.pedirOpcionMenu("¿Qué quieres hacer?",
                    "Explorar (nueva batalla)",
                    "Ver equipo",
                    "Inventario",
                    "Usar objeto",
                    "Guardar partida",
                    "Salir");

            switch (opcion) {
                case 1 -> explorar();
                case 2 -> verEquipo();
                case 3 -> verInventario();
                case 4 -> usarObjetoFueraBatalla();
                case 5 -> guardarPartida();
                case 6 -> corriendo = false;
                default -> vista.mostrarMensaje("Opción no válida. Intenta de nuevo.");
            }
        }
        vista.mostrarMensaje("\n¡Hasta la próxima, " + jugador.getNombre() + "!");
    }

    private void explorar() {
        if (!jugador.tieneEquipoVivo()) {
            vista.mostrarMensaje("No tienes pokemon disponibles para batallar.");
            return;
        }
        Entrenador rival = elegirRival();
        boolean gano = batallaController.iniciarBatalla(jugador, rival);
        manejarPostBatalla(gano);
    }

    private void manejarPostBatalla(boolean gano) {
        if (gano) {
            vista.mostrarMensaje("\n¡Ganaste $500!");
            verificarEvoluciones();
            guardarPartida(); // guardado automático
        } else {
            double penalizacion = Math.min(200, jugador.getDinero());
            jugador.gastarDinero(penalizacion);
            vista.mostrarMensaje(String.format(
                    "%nPerdiste la batalla. Te descontaron $%.0f.", penalizacion));
        }
    }

    private void verificarEvoluciones() {
        // Recopilar candidatos antes de modificar el equipo
        List<Pokemon> candidatos = jugador.getEquipoActivo().stream()
                .filter(p -> p.getEvolucion() != null && p.getNivel() >= 16)
                .toList();

        for (Pokemon p : candidatos) {
            boolean acepta = vista.pedirConfirmacionEvolucion(p, p.getEvolucion());
            if (acepta) {
                String nombreSiguiente = p.getEvolucion().getNombre();
                jugador.evolucionarPokemon(p);
                vista.mostrarMensaje("¡" + p.getNombre() + " evolucionó a " + nombreSiguiente + "!");
            }
        }
    }

    // Elección de rival

    private Entrenador elegirRival() {
        List<Entrenador> rivales = EntrenadorCatalogo.getRivalesPredefinidos();
        vista.mostrarRivales(rivales);

        int opcion = vista.leerOpcion(rivales.size() + 1);

        if (opcion == rivales.size() + 1) {
            return EntrenadorCatalogo.crearRivalEscalado(jugador.nivelPromedio());
        }

        return EntrenadorCatalogo.crearRival(opcion - 1);
    }

    // ── Ver equipo ────────────────────────────────────────────────────────

    private void verEquipo() {
        vista.mostrarEquipoDetallado(jugador);
    }

// ── Ver inventario ────────────────────────────────────────────────────

    private void verInventario() {
        vista.mostrarInventarioDetallado(jugador.getInventario());
    }

// ── Usar objeto fuera de batalla ──────────────────────────────────────

    private void usarObjetoFueraBatalla() {
        Inventario inv = jugador.getInventario();

        if (inv.estaVacio()) {
            vista.mostrarMensaje("No tienes objetos en el inventario.");
            return;
        }

        Objeto objeto = vista.pedirObjetoInventario(inv);
        if (objeto == null) return;

        if (objeto.tipo() == Objeto.TipoObjeto.POKEBOLA) {
            vista.mostrarMensaje("Las pokébolas solo se usan en batalla.");
            return;
        }

        List<Pokemon> equipo = jugador.getEquipoActivo();
        Pokemon objetivo = elegirPokemonObjetivo(objeto, equipo);
        if (objetivo == null) return;

        boolean exito = jugador.usarObjeto(objeto.id(), objetivo);
        if (exito) {
            vista.mostrarMensaje("\n" + objeto.nombre() + " usado en "
                    + objetivo.getNombre() + " correctamente.");
            vista.mostrarBarraVida(objetivo);
        } else {
            vista.mostrarMensaje("No se pudo usar " + objeto.nombre()
                    + " en " + objetivo.getNombre()
                    + ". Verifica que sea necesario.");
        }
    }

    private Pokemon elegirPokemonObjetivo(Objeto objeto, List<Pokemon> equipo) {
        String titulo = switch (objeto.tipo()) {
            case POCION, SUPER_POCION -> "¿A cuál pokemon usar " + objeto.nombre() + "?";
            case REVIVIR               -> "¿A cuál pokemon debilitado revivir?";
            default                    -> "Elegir pokemon";
        };

        // Si es Revivir, filtrar solo debilitados para orientar al jugador
        List<Pokemon> candidatos = switch (objeto.tipo()) {
            case REVIVIR -> equipo.stream().filter(Pokemon::estaDebilitado).toList();
            default      -> equipo.stream().filter(Pokemon::estaActivo).toList();
        };

        if (candidatos.isEmpty()) {
            vista.mostrarMensaje("No hay pokemon válidos para usar ese objeto.");
            return null;
        }

        return vista.pedirPokemonDelEquipo(candidatos, titulo);
    }

    // Métodos nuevos
    private void cargarPartida() {
        try {
            jugador = repositorio.cargar();
            vista.mostrarMensaje("\n¡Bienvenido de vuelta, " + jugador.getNombre() + "!");
            bucleJuego();
        } catch (IOException e) {
            vista.mostrarMensaje("Error al cargar la partida. Iniciando nueva...");
            nuevaPartida();
        }
    }

    private void guardarPartida() {
        try {
            repositorio.guardar(jugador);
            vista.mostrarMensaje("Partida guardada correctamente.");
        } catch (IOException e) {
            vista.mostrarMensaje("Error al guardar la partida: " + e.getMessage());
        }
    }

    // Actualizar manejarPostBatalla() para guardar automáticamente al ganar

}
