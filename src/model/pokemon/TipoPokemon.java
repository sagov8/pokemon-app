package model.pokemon;

public enum TipoPokemon {
    FUEGO, AGUA, TIERRA, AIRE, ELECTRICO, PSIQUICO;

    /**
     * Retorna el multiplicador de daño cuando este tipo ataca al tipo defensor.
     * 2.0 = súper efectivo, 0.5 = poco efectivo, 0.0 = inmune, 1.0 = normal
     */
    public double efectividadContra(TipoPokemon defensor) {
        return switch (this) {
            case FUEGO     -> switch (defensor) {
                case AGUA   -> 0.5;
                case TIERRA -> 2.0;
                default     -> 1.0;
            };
            case AGUA      -> switch (defensor) {
                case FUEGO  -> 2.0;
                case TIERRA -> 0.5;
                default     -> 1.0;
            };
            case TIERRA    -> switch (defensor) {
                case ELECTRICO -> 2.0;
                case FUEGO     -> 0.5;
                default        -> 1.0;
            };
            case AIRE      -> switch (defensor) {
                case TIERRA    -> 2.0;
                case ELECTRICO -> 0.5;
                default        -> 1.0;
            };
            case ELECTRICO -> switch (defensor) {
                case AGUA   -> 2.0;
                case TIERRA -> 0.0;
                default     -> 1.0;
            };
            case PSIQUICO  -> 1.0;
        };
    }
}
