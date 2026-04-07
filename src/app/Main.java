package app;

import factory.CreadorMovimiento;
import factory.CreadorMovimientoAgua;
import factory.CreadorMovimientoFuego;
import factory.CreadorMovimientoPsiquico;
import model.pokemon.Movimiento;

public class Main {

    public static void main(String[] args) {
        // Creador Fuego
        CreadorMovimiento creadorFuego = new CreadorMovimientoFuego();
        Movimiento movimientoFuego = creadorFuego.prepararMovimiento();

        System.out.println("Nombre: " + movimientoFuego.getNombre());
        System.out.println("Tipo: " + movimientoFuego.getTipo());
        System.out.println("Poder: " + movimientoFuego.getPoder());
        movimientoFuego.usarMovimiento();
        System.out.println("Daño calculado: " + movimientoFuego.calcularDanio());

        System.out.println("\n");

        // Creador Agua
        CreadorMovimiento creadorAgua = new CreadorMovimientoAgua();
        Movimiento movimientoAgua = creadorAgua.prepararMovimiento();

        System.out.println("Nombre: " + movimientoAgua.getNombre());
        System.out.println("Tipo: " + movimientoAgua.getTipo());
        System.out.println("Poder: " + movimientoAgua.getPoder());
        movimientoAgua.usarMovimiento();
        System.out.println("Daño calculado: " + movimientoAgua.calcularDanio());

        // Creador Psiquico
        System.out.println("\n");
        CreadorMovimiento creadorPsiquico = new CreadorMovimientoPsiquico();
        Movimiento movimientoPsiquico = creadorPsiquico.prepararMovimiento();

        System.out.println("Nombre: " + movimientoPsiquico.getNombre());
        System.out.println("Tipo: " + movimientoPsiquico.getTipo());
        System.out.println("Poder: " + movimientoPsiquico.getPoder());
        movimientoPsiquico.usarMovimiento();
        System.out.println("Daño calculado: " + movimientoPsiquico.calcularDanio());

    }
}
