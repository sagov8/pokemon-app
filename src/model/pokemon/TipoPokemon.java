package model.pokemon;

public enum TipoPokemon {
    FUEGO, AGUA, TIERRA, AIRE, ELECTRICO, PSIQUICO;

    /**
     * Convierte el String que retorna Movimiento.getTipo() a enum.
     * Así Batalla puede calcular efectividad sin cambiar la interfaz existente.
     */
    public static TipoPokemon desdeCadena(String tipo) {
        return switch (tipo.toUpperCase().trim()) {
            case "FUEGO"     -> FUEGO;
            case "AGUA"      -> AGUA;
            case "TIERRA"    -> TIERRA;
            case "AIRE"      -> AIRE;
            case "ELECTRICO" -> ELECTRICO;
            case "PSIQUICO"  -> PSIQUICO;
            default -> throw new IllegalArgumentException("Tipo desconocido: " + tipo);
        };
    }

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
