package view;

import model.pokemon.Entrenador;
import model.pokemon.Movimiento;
import model.pokemon.Pokemon;
import service.BatallaObserver;

import java.util.List;
import java.util.Scanner;

public class ConsoleView implements BatallaObserver{
    private final Scanner scanner = new Scanner(System.in);

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

    private int leerOpcion(int max) {
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
}
