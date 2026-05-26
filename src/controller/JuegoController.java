package controller;

import model.Inventario;
import model.Objeto;
import model.pokemon.*;
import persistence.GameRepository;
import util.*;
import view.ConsoleView;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class JuegoController {
    private final ConsoleView vista;
    private final BatallaController batallaController;
    private final GameRepository repositorio;

    private static final String SALIR_TEXT = "Salir";

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
                    "Continuar partida", "Nueva partida", SALIR_TEXT);
            switch (op) {
                case 1 -> cargarPartida();
                case 2 -> nuevaPartida();
                default -> vista.mostrarMensaje("Opción inválida. Iniciando nueva partida...");
            }
        } else {
            int op = vista.pedirOpcionMenu("Menú principal",
                    "Nueva partida", SALIR_TEXT);
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
                    "Luchar vs Pokemon salvaje",
                    "Ver equipo",
                    "Inventario",
                    "Usar objeto",
                    "Guardar partida",
                    SALIR_TEXT);

            switch (opcion) {
                case 1 -> explorar();
                case 2 -> lucharVsPokemonSalvaje();
                case 3 -> verEquipo();
                case 4 -> verInventario();
                case 5 -> usarObjetoFueraBatalla();
                case 6 -> guardarPartida();
                case 7 -> corriendo = false;
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

    // Ver equipo
    private void verEquipo() {
        vista.mostrarEquipoDetallado(jugador);
    }

    // Ver inventario
    private void verInventario() {
        vista.mostrarInventarioDetallado(jugador.getInventario());
    }

    // Usar objeto fuera de batalla
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

    // Métodos para manejar carga y guardado de partida
    private void cargarPartida() {
        try {
            jugador = repositorio.cargar();
            vista.mostrarMensaje("\n¡Bienvenido de vuelta, " + jugador.getNombre() + "!");
            bucleJuego();
        } catch (IOException _) {
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


    private boolean intentarCaptura(Pokemon salvaje) {
        Inventario inventario = jugador.getInventario();

        Objeto pokebola = inventario.getObjetos().stream()
                .filter(o -> o.tipo() == Objeto.TipoObjeto.POKEBOLA)
                .findFirst()
                .orElse(null);

        if (pokebola == null) {
            vista.mostrarMensaje("No tienes Pokébolas.");
            return false;
        }

        inventario.eliminarObjeto(pokebola.id());

        double porcentajeVida = salvaje.porcentajeVida();

        double probabilidadCaptura;

        if (porcentajeVida <= 0.25) {
            probabilidadCaptura = 0.75;
        } else if (porcentajeVida <= 0.50) {
            probabilidadCaptura = 0.50;
        } else {
            probabilidadCaptura = 0.25;
        }

        return Math.random() < probabilidadCaptura;
    }

    private void lucharVsPokemonSalvaje() {
        if (!jugador.tieneEquipoVivo()) {
            vista.mostrarMensaje("No tienes pokemon disponibles para batallar.");
            return;
        }

        Pokemon salvaje = PokemonCatalogo.crearSalvaje();

        vista.mostrarMensaje("\n¡Un " + salvaje.getNombre() + " salvaje apareció!");

        Pokemon pokemonJugador = jugador.getPrimerPokemonVivo();

        boolean terminado = false;

        while (!terminado && !salvaje.estaDebilitado() && jugador.tieneEquipoVivo()) {
            vista.mostrarMensaje("\nTu pokemon:");
            vista.mostrarBarraVida(pokemonJugador);

            vista.mostrarMensaje("\nPokemon salvaje:");
            vista.mostrarBarraVida(salvaje);

            int opcion = vista.pedirOpcionMenu(
                    "¿Qué quieres hacer?",
                    "Atacar",
                    "Lanzar Pokébola",
                    "Cambiar Pokémon",
                    "Huir"
            );

            switch (opcion) {
                case 1 -> {
                    terminado = manejarAtaqueSalvaje(pokemonJugador, salvaje);

                    if (!terminado && pokemonJugador.estaDebilitado()) {
                        pokemonJugador = cambiarANuevoPokemon(pokemonJugador);

                        if (pokemonJugador == null) {
                            vista.mostrarMensaje("No tienes más pokemon disponibles.");
                            terminado = true;
                        }
                    }
                }

                case 2 -> {
                    boolean capturado = manejarCaptura(salvaje, pokemonJugador);

                    if (capturado) {
                        terminado = true;
                    } else if (pokemonJugador.estaDebilitado()) {
                        pokemonJugador = cambiarANuevoPokemon(pokemonJugador);

                        if (pokemonJugador == null) {
                            vista.mostrarMensaje("No tienes más pokemon disponibles.");
                            terminado = true;
                        }
                    }
                }

                case 3 -> pokemonJugador = cambiarPokemon(pokemonJugador);

                case 4 -> {
                    vista.mostrarMensaje("Escapaste del Pokémon salvaje.");
                    terminado = true;
                }

                default -> vista.mostrarMensaje("Opción no válida.");
            }
        }
    }

    private boolean manejarAtaqueSalvaje(Pokemon atacante, Pokemon salvaje) {
        atacarSalvaje(atacante, salvaje);

        if (salvaje.estaDebilitado()) {
            vista.mostrarMensaje("¡" + salvaje.getNombre() + " se debilitó!");
            atacante.ganarExperiencia(80);
            verificarEvoluciones();
            guardarPartida();
            return true;
        }

        ataqueDelSalvaje(salvaje, atacante);

        return false;
    }

    private boolean manejarCaptura(Pokemon salvaje, Pokemon pokemonJugador) {
        boolean capturado = intentarCaptura(salvaje);

        if (capturado) {
            jugador.agregarPokemonEquipo(salvaje);
            vista.mostrarMensaje("¡Capturaste a " + salvaje.getNombre() + "!");
            guardarPartida();
            return true;
        }

        vista.mostrarMensaje("¡Oh no! " + salvaje.getNombre() + " escapó.");

        ataqueDelSalvaje(salvaje, pokemonJugador);

        return false;
    }

    private void atacarSalvaje(Pokemon atacante, Pokemon salvaje) {
        List<Movimiento> movimientos = atacante.getMovimientos();

        int opcion = vista.pedirMovimiento(movimientos) - 1;

        Movimiento movimiento = movimientos.get(opcion);

        int danioBase = atacante.atacar(movimiento);

        int danioFinal = Math.max(
                1,
                danioBase + atacante.getAtaque() - salvaje.getDefensa()
        );

        salvaje.recibirDanio(danioFinal);

        vista.mostrarMensaje(
                atacante.getNombre() + " usó " + movimiento.getNombre()
                        + " e hizo " + danioFinal + " de daño."
        );

        vista.mostrarBarraVida(salvaje);
    }

    private void ataqueDelSalvaje(Pokemon salvaje, Pokemon pokemonJugador) {
        if (salvaje.estaDebilitado()) return;

        List<Movimiento> movimientos = salvaje.getMovimientos();

        if (movimientos.isEmpty()) {
            vista.mostrarMensaje(salvaje.getNombre() + " no tiene movimientos.");
            return;
        }

        Movimiento movimiento = movimientos.get(new Random().nextInt(movimientos.size()));

        int danioBase = salvaje.atacar(movimiento);

        int danioFinal = Math.max(
                1,
                danioBase + salvaje.getAtaque() - pokemonJugador.getDefensa()
        );

        pokemonJugador.recibirDanio(danioFinal);

        vista.mostrarMensaje(
                salvaje.getNombre() + " usó " + movimiento.getNombre()
                        + " e hizo " + danioFinal + " de daño."
        );

        vista.mostrarBarraVida(pokemonJugador);
    }

    private Pokemon cambiarPokemon(Pokemon pokemonActivo) {
        Pokemon elegido = vista.pedirPokemonParaCambio(
                jugador.getEquipoActivo(),
                pokemonActivo
        );

        if (elegido == null) {
            vista.mostrarMensaje("No cambiaste de Pokémon.");
            return pokemonActivo;
        }

        vista.mostrarMensaje("¡Adelante " + elegido.getNombre() + "!");
        return elegido;
    }

    private Pokemon cambiarANuevoPokemon(Pokemon pokemonActual) {
        if (!jugador.tieneEquipoVivo()) {
            return null;
        }

        Pokemon nuevo = vista.pedirPokemonParaCambio(
                jugador.getEquipoActivo(),
                pokemonActual
        );

        if (nuevo == null) {
            return jugador.getPrimerPokemonVivo();
        }

        vista.mostrarMensaje("¡Adelante " + nuevo.getNombre() + "!");
        return nuevo;
    }
}
