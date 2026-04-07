package app;

import factory.CreadorMovimiento;
import factory.CreadorMovimientoAgua;
import factory.CreadorMovimientoFuego;
import model.pokemon.Movimiento;

public class Main {

    public static void main(String[] args) {
        CreadorMovimiento creadorFuego = new CreadorMovimientoFuego();
        Movimiento movimientoFuego = creadorFuego.prepararMovimiento();

        System.out.println("Nombre: " + movimientoFuego.getNombre());
        System.out.println("Tipo: " + movimientoFuego.getTipo());
        System.out.println("Poder: " + movimientoFuego.getPoder());
        movimientoFuego.usarMovimiento();
        System.out.println("Daño calculado: " + movimientoFuego.calcularDanio());

        System.out.println("\n");

        CreadorMovimiento creadorAgua = new CreadorMovimientoAgua();
        Movimiento movimientoAgua = creadorAgua.prepararMovimiento();

        System.out.println("Nombre: " + movimientoAgua.getNombre());
        System.out.println("Tipo: " + movimientoAgua.getTipo());
        System.out.println("Poder: " + movimientoAgua.getPoder());
        movimientoAgua.usarMovimiento();
        System.out.println("Daño calculado: " + movimientoAgua.calcularDanio());
    }
}
