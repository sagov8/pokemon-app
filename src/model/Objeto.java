package model;

import model.pokemon.Pokemon;

/**
 * Representa un objeto consumible del inventario.
 * Record: inmutable, equals/hashCode/toString generados automáticamente.
 */
public record Objeto(int id, String nombre, String descripcion, TipoObjeto tipo, int valor) {

    public enum TipoObjeto {
        POCION,       // restaura vida
        SUPER_POCION, // restaura más vida
        REVIVIR,      // revive pokemon debilitado
        POKEBOLA      // captura pokemon salvajes
    }

    /**
     * Aplica el efecto del objeto sobre el pokemon objetivo.
     * Retorna true si se usó con éxito.
     */
    public boolean usarObjeto(Pokemon objetivo) {
        return switch (tipo) {
            case POCION       -> restaurarVida(objetivo, 20);
            case SUPER_POCION -> restaurarVida(objetivo, 50);
            case REVIVIR      -> revivirPokemon(objetivo);
            case POKEBOLA     -> false; // se gestiona desde BatallaService
        };
    }

    private boolean restaurarVida(Pokemon p, int cantidad) {
        if (p.estaDebilitado()) return false;
        p.restaurarVida(cantidad);
        return true;
    }

    private boolean revivirPokemon(Pokemon p) {
        if (!p.estaDebilitado()) return false;
        p.revivir();
        return true;
    }
}
