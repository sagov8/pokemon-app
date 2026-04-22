package util;

import model.Inventario;
import model.Objeto;
import model.Objeto.TipoObjeto;

import java.util.List;
import java.util.Optional;

public class ObjetoCatalogo {

    private ObjetoCatalogo() {}

    // Instancias compartidas — seguro porque Objeto es record (inmutable)
    public static final Objeto POCION       = new Objeto(1, "Poción",
            "Restaura 20 HP al pokemon activo",       TipoObjeto.POCION,       200);
    public static final Objeto SUPER_POCION = new Objeto(2, "Super Poción",
            "Restaura 50 HP al pokemon activo",       TipoObjeto.SUPER_POCION, 500);
    public static final Objeto REVIVIR      = new Objeto(3, "Revivir",
            "Revive un pokemon debilitado con 50% HP", TipoObjeto.REVIVIR,     1500);
    public static final Objeto POKEBOLA     = new Objeto(4, "Pokébola",
            "Captura un pokemon salvaje",              TipoObjeto.POKEBOLA,     200);

    public static List<Objeto> getTodos() {
        return List.of(POCION, SUPER_POCION, REVIVIR, POKEBOLA);
    }

    /** Inventario con el que arranca un entrenador nuevo. */
    public static Inventario inventarioInicial() {
        Inventario inv = new Inventario();
        inv.agregarObjeto(POCION);
        inv.agregarObjeto(POCION);
        inv.agregarObjeto(POCION);
        inv.agregarObjeto(POKEBOLA);
        return inv;
    }

    public static Optional<Objeto> porId(int id) {
        return getTodos().stream()
                .filter(o -> o.id() == id)
                .findFirst();
    }
}
