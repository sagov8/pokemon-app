package persistence;

import model.Inventario;
import model.pokemon.*;
import model.Objeto;
import util.ObjetoCatalogo;
import util.PokemonCatalogo;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class GameRepository {

    private static final String DIR  = "saves/";
    private static final String FILE = DIR + "partida.txt";

    public GameRepository() {
        new File(DIR).mkdirs();
    }

    // ── Guardar ───────────────────────────────────────────────────────────

    public void guardar(Entrenador entrenador) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("ENTRENADOR,")
                .append(entrenador.getId()).append(",")
                .append(entrenador.getNombre()).append(",")
                .append(entrenador.getDinero()).append("\n");

        for (Pokemon p : entrenador.getEquipoActivo()) {
            sb.append("EQUIPO,").append(serializar(p)).append("\n");
        }

        for (Pokemon p : entrenador.getAlmacenamiento()) {
            sb.append("ALMACEN,").append(serializar(p)).append("\n");
        }

        sb.append("INVENTARIO");
        for (Objeto o : entrenador.getInventario().getObjetos()) {
            sb.append(",").append(o.id());
        }
        sb.append("\n");

        Files.writeString(Path.of(FILE), sb.toString());
    }

    // Cargar
    public Entrenador cargar() throws IOException {
        List<String> lineas = Files.readAllLines(Path.of(FILE));

        String[] header   = lineas.getFirst().split(",");
        int id            = Integer.parseInt(header[1]);
        String nombre     = header[2];
        double dinero     = Double.parseDouble(header[3]);

        Entrenador entrenador = new Entrenador(id, nombre, dinero);

        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i).trim();
            if (linea.isBlank()) continue;

            String[] partes = linea.split(",");
            switch (partes[0]) {
                case "EQUIPO"     -> entrenador.agregarPokemonEquipo(
                        deserializar(Arrays.copyOfRange(partes, 1, partes.length)));
                case "ALMACEN"    -> entrenador.agregarAlAlmacenamiento(
                        deserializar(Arrays.copyOfRange(partes, 1, partes.length)));
                case "INVENTARIO" -> cargarInventario(
                        entrenador.getInventario(), partes);
                default -> System.err.println("Línea desconocida en el archivo de guardado: " + linea);
            }
        }

        return entrenador;
    }

    public boolean existeGuardado() {
        return Files.exists(Path.of(FILE));
    }

    // Serialización
    private String serializar(Pokemon p) {
        int evolucionId = p.getEvolucion() != null ? p.getEvolucion().getId() : -1;
        return String.join(",",
                String.valueOf(p.getId()),
                p.getNombre(),
                p.getTipo().name(),
                String.valueOf(p.getNivel()),
                String.valueOf(p.getVidaActual()),
                String.valueOf(p.getVidaMaxima()),
                String.valueOf(p.getAtaque()),
                String.valueOf(p.getDefensa()),
                String.valueOf(p.getVelocidad()),
                p.getEstado().name(),
                String.valueOf(evolucionId)
        );
    }

    private Pokemon deserializar(String[] d) {
        int           id        = Integer.parseInt(d[0]);
        String        nombre    = d[1];
        TipoPokemon   tipo      = TipoPokemon.valueOf(d[2]);
        int           nivel     = Integer.parseInt(d[3]);
        int           vidaActual= Integer.parseInt(d[4]);
        int           vidaMaxima= Integer.parseInt(d[5]);
        int           ataque    = Integer.parseInt(d[6]);
        int           defensa   = Integer.parseInt(d[7]);
        int           velocidad = Integer.parseInt(d[8]);
        EstadoPokemon estado    = EstadoPokemon.valueOf(d[9]);
        int           evolId    = Integer.parseInt(d[10]);

        Pokemon p = new Pokemon(id, nombre, tipo, nivel,
                vidaMaxima, ataque, defensa, velocidad);

        // Restaurar HP — el constructor lo pone en vidaMaxima
        if (vidaActual < vidaMaxima) {
            p.recibirDanio(vidaMaxima - vidaActual);
        }
        // Si estaba debilitado pero vidaActual quedó en 0 el estado ya es DEBILIDAD.
        // Si el archivo dice DEBILIDAD y vidaActual > 0 (caso raro), forzamos:
        if (estado == EstadoPokemon.DEBILIDAD && p.estaActivo()) {
            p.recibirDanio(p.getVidaActual());
        }

        PokemonCatalogo.asignarMovimientosPorTipo(p);

        if (evolId != -1) {
            p.setEvolucion(PokemonCatalogo.crearEvolucionPorId(evolId));
        }

        return p;
    }

    private void cargarInventario(Inventario inventario, String[] partes) {
        // partes[0] = "INVENTARIO", partes[1..n] = ids de objetos
        for (int i = 1; i < partes.length; i++) {
            int id = Integer.parseInt(partes[i].trim());
            ObjetoCatalogo.porId(id).ifPresent(inventario::agregarObjeto);
        }
    }
}