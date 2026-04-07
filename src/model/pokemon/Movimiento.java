package model.pokemon;

public interface Movimiento {
    String getNombre();
    int getPoder();
    String getTipo();

    void usarMovimiento();

    int calcularDanio();
}
