package view;

import model.Inventario;
import model.Objeto;
import model.pokemon.Entrenador;
import model.pokemon.Movimiento;
import model.pokemon.Pokemon;
import service.BatallaObserver;

import java.util.List;
import java.util.Scanner;

public class ConsoleView implements BatallaObserver{
    private final Scanner scanner = new Scanner(System.in);

    public void mostrarBienvenida() {
        System.out.println("""
            
            ╔══════════════════════════════════════════╗
            ║          POKEMON CONSOLE  v1.0           ║
            ║     Atrapa, entrena y conquista          ║
            ╚══════════════════════════════════════════╝
            """);
    }

    public String pedirTexto(String prompt) {
        System.out.print(prompt + " ");
        String entrada = scanner.nextLine().trim();
        while (entrada.isBlank()) {
            System.out.print("No puede estar vacío. " + prompt + " ");
            entrada = scanner.nextLine().trim();
        }
        return entrada;
    }

    public void mostrarStarters(List<Pokemon> starters) {
        System.out.println("\n¿Cuál será tu primer pokemon?\n");
        for (int i = 0; i < starters.size(); i++) {
            Pokemon p = starters.get(i);
            String movs = p.getMovimientos().stream()
                    .map(Movimiento::getNombre)
                    .collect(java.util.stream.Collectors.joining(", "));
            System.out.printf("  %d. %-12s [%-10s] Nv.%-3d HP:%-5d ATK:%-4d DEF:%-4d%n",
                    i + 1,
                    p.getNombre(),
                    p.getTipo(),
                    p.getNivel(),
                    p.getVidaMaxima(),
                    p.getAtaque(),
                    p.getDefensa());
            System.out.printf("     Movimientos: %s%n%n", movs);
        }
        System.out.print("Elige: ");
    }

    public void mostrarRivales(List<Entrenador> rivales) {
        System.out.println("\n¿Contra quién quieres batallar?\n");
        for (int i = 0; i < rivales.size(); i++) {
            Entrenador r = rivales.get(i);
            System.out.printf("  %d. %-12s — Nv. promedio: %d | Pokemon: %d%n",
                    i + 1,
                    r.getNombre(),
                    r.nivelPromedio(),
                    r.getEquipoActivo().size());
        }
        System.out.printf("  %d. Rival aleatorio (escalado a tu nivel)%n%n",
                rivales.size() + 1);
        System.out.print("Elige: ");
    }

    public void mostrarEquipoResumen(Entrenador entrenador) {
        System.out.println("\n=== Equipo de " + entrenador.getNombre() + " ===");
        entrenador.getEquipoActivo().forEach(p -> {
            String estado = p.estaDebilitado() ? " [DEBILITADO]" : "";
            System.out.printf("  %-12s [%-10s] Nv.%d  %s%s%n",
                    p.getNombre(), p.getTipo(), p.getNivel(),
                    barraVida(p), estado);
        });
        System.out.printf("  Dinero: $%.0f%n", entrenador.getDinero());
    }

    public void mostrarEquipoDetallado(Entrenador entrenador) {
        System.out.println("\n=== Equipo de " + entrenador.getNombre() + " ===");
        System.out.printf("Dinero: $%.0f%n%n", entrenador.getDinero());

        List<Pokemon> equipo = entrenador.getEquipoActivo();
        for (int i = 0; i < equipo.size(); i++) {
            Pokemon p = equipo.get(i);
            String estado = p.estaDebilitado() ? "DEBILITADO" : "Activo";

            System.out.printf("  %d. %s [%s] Nv.%d — %s%n",
                    i + 1, p.getNombre(), p.getTipo(), p.getNivel(), estado);
            System.out.printf("     HP:  [%s] %d/%d%n",
                    barraHp(p), p.getVidaActual(), p.getVidaMaxima());
            System.out.printf("     ATK: %-4d  DEF: %-4d  VEL: %-4d%n",
                    p.getAtaque(), p.getDefensa(), p.getVelocidad());

            List<Movimiento> movs = p.getMovimientos();
            System.out.print("     Movimientos: ");
            System.out.println(movs.stream()
                    .map(m -> m.getNombre() + "(pwr:" + m.getPoder() + ")")
                    .collect(java.util.stream.Collectors.joining(", ")));

            if (p.getEvolucion() != null && p.getNivel() >= 16) {
                System.out.println("     ** Puede evolucionar a "
                        + p.getEvolucion().getNombre() + " **");
            }
            System.out.println();
        }
    }

    private String barraHp(Pokemon p) {
        int llenas = (int) (p.porcentajeVida() * 10);
        return "█".repeat(llenas) + "░".repeat(10 - llenas);
    }

    public void mostrarInventarioDetallado(Inventario inventario) {
        System.out.println("\n=== Inventario ===");
        if (inventario.estaVacio()) {
            System.out.println("  No tienes objetos.");
            return;
        }
        inventario.getObjetos().forEach(o ->
                System.out.printf("  · %-14s — %s (valor: $%d)%n",
                        o.nombre(), o.descripcion(), o.valor()));
    }

    public Pokemon pedirPokemonDelEquipo(List<Pokemon> equipo, String titulo) {
        System.out.println("\n=== " + titulo + " ===");
        for (int i = 0; i < equipo.size(); i++) {
            Pokemon p = equipo.get(i);
            String estado = p.estaDebilitado() ? "[DEBILITADO]" : "";
            System.out.printf("  %d. %-12s Nv.%d  %d/%d HP %s%n",
                    i + 1, p.getNombre(), p.getNivel(),
                    p.getVidaActual(), p.getVidaMaxima(), estado);
        }
        System.out.printf("  %d. Cancelar%n", equipo.size() + 1);
        System.out.print("Elige: ");
        int op = leerOpcion(equipo.size() + 1);
        return op == equipo.size() + 1 ? null : equipo.get(op - 1);
    }

    private String barraVida(Pokemon p) {
        int llenas = (int) (p.porcentajeVida() * 10);
        return "[" + "█".repeat(llenas) + "░".repeat(10 - llenas) + "]"
                + " " + p.getVidaActual() + "/" + p.getVidaMaxima() + " HP";
    }

    // ── BatallaObserver ───────────────────────────────────────────────────

    @Override
    public void onBatallaIniciada(Entrenador primero, Entrenador segundo) {
        System.out.println("""
                
                ╔══════════════════════════════╗
                ║      ¡COMIENZA LA BATALLA!   ║
                ╚══════════════════════════════╝""");
        System.out.printf("%s  VS  %s%n", primero.getNombre(), segundo.getNombre());
        System.out.println("Mueve primero: " + primero.getNombre());
    }

    @Override
    public void onTurnoIniciado(Entrenador turnoActual) {
        System.out.println("\n--- Turno de " + turnoActual.getNombre() + " ---");
    }

    @Override
    public void onMovimientoUsado(Pokemon atacante, Pokemon defensor,
                                  Movimiento movimiento, int danioFinal, double efectividad) {
        System.out.printf("%n%s usó %s → %s recibe %d de daño%n",
                atacante.getNombre(), movimiento.getNombre(),
                defensor.getNombre(), danioFinal);

        if      (efectividad >= 2.0)                  System.out.println("¡Es súper efectivo!");
        else if (efectividad > 0 && efectividad < 1.0) System.out.println("No es muy efectivo...");
        else if (efectividad == 0.0)                   System.out.println("¡No afecta a " + defensor.getNombre() + "!");

        mostrarBarraVida(defensor);
    }

    @Override
    public void onPokemonDebilitado(Pokemon pokemon, Entrenador entrenador) {
        System.out.printf("%n*** ¡%s de %s se debilitó! ***%n",
                pokemon.getNombre(), entrenador.getNombre());
    }

    @Override
    public void onBatallaFinalizada(Entrenador ganador, Entrenador perdedor) {
        System.out.printf("""
                
                ╔══════════════════════════════╗
                ║  ¡%s ganó la batalla!
                ╚══════════════════════════════╝%n""", ganador.getNombre());
    }

    // ── Utilidades de consola ─────────────────────────────────────────────

    public void mostrarBarraVida(Pokemon p) {
        int llenas  = (int) (p.porcentajeVida() * 10);
        int vacias  = 10 - llenas;
        String barra = "█".repeat(llenas) + "░".repeat(vacias);
        System.out.printf("%-12s [%s] %d/%d HP%n",
                p.getNombre(), barra, p.getVidaActual(), p.getVidaMaxima());
    }

    public void mostrarEstadoBatalla(Entrenador turnoActual, Entrenador rival) {
        System.out.println("\n═══════════════════════════════");
        turnoActual.pokemonActivo().ifPresent(this::mostrarBarraVida);
        System.out.println("       ─── vs ───");
        rival.pokemonActivo().ifPresent(this::mostrarBarraVida);
        System.out.println("═══════════════════════════════");
    }

    public int pedirMovimiento(List<Movimiento> movimientos) {
        System.out.println("\nElige un movimiento:");
        for (int i = 0; i < movimientos.size(); i++) {
            Movimiento m = movimientos.get(i);
            System.out.printf("  %d. %-15s [Tipo: %-10s | Poder: %d]%n",
                    i + 1, m.getNombre(), m.getTipo(), m.getPoder());
        }
        System.out.print("Opción: ");
        return leerOpcion(movimientos.size());
    }

    public int pedirOpcionMenu(String titulo, String... opciones) {
        System.out.println("\n=== " + titulo + " ===");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println("  " + (i + 1) + ". " + opciones[i]);
        }
        System.out.print("Opción: ");
        return leerOpcion(opciones.length);
    }

    public int leerOpcion(int max) {
        while (true) {
            try {
                int op = Integer.parseInt(scanner.nextLine().trim());
                if (op >= 1 && op <= max) return op;
                System.out.print("Elige entre 1 y " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Solo números. Opción: ");
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public Objeto pedirObjetoInventario(Inventario inventario) {
        List<Objeto> objetos = inventario.getObjetos();
        System.out.println("\n=== Inventario ===");
        for (int i = 0; i < objetos.size(); i++) {
            Objeto o = objetos.get(i);
            System.out.printf("  %d. %-14s — %s%n", i + 1, o.nombre(), o.descripcion());
        }
        System.out.printf("  %d. Cancelar%n%n", objetos.size() + 1);
        System.out.print("Elige: ");
        int op = leerOpcion(objetos.size() + 1);
        return op == objetos.size() + 1 ? null : objetos.get(op - 1);
    }

    public Pokemon pedirPokemonParaCambio(List<Pokemon> equipo, Pokemon activo) {
        List<Pokemon> opciones = equipo.stream()
                .filter(p -> p.estaActivo() && !p.equals(activo))
                .toList();

        if (opciones.isEmpty()) {
            mostrarMensaje("No tienes otros pokemon disponibles.");
            return null;
        }

        System.out.println("\n=== Cambiar pokemon ===");
        for (int i = 0; i < opciones.size(); i++) {
            Pokemon p = opciones.get(i);
            System.out.printf("  %d. ", i + 1);
            mostrarBarraVida(p);
        }
        System.out.printf("  %d. Cancelar%n%n", opciones.size() + 1);
        System.out.print("Elige: ");
        int op = leerOpcion(opciones.size() + 1);
        return op == opciones.size() + 1 ? null : opciones.get(op - 1);
    }

    public boolean pedirConfirmacionEvolucion(Pokemon actual, Pokemon siguiente) {
        System.out.printf("%n¡%s puede evolucionar a %s!%n", actual.getNombre(), siguiente.getNombre());
        System.out.printf("  ATK: %d → %d  |  DEF: %d → %d  |  HP: %d → %d%n%n",
                actual.getAtaque(),  siguiente.getAtaque(),
                actual.getDefensa(), siguiente.getDefensa(),
                actual.getVidaMaxima(), siguiente.getVidaMaxima());
        return pedirOpcionMenu("¿Aceptas la evolución?",
                "Sí, evolucionar", "No, por ahora no") == 1;
    }
}
