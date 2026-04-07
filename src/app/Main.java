package app;

import factory.CreadorMovimiento;
import factory.CreadorMovimientoAgua;
import factory.CreadorMovimientoFuego;
import factory.CreadorMovimientoPsiquico;
import factory.CreadorMovimientoTierra;
import factory.CreadorMovimientoAire;
import factory.CreadorMovimientoElectrico;
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



        System.out.println("\n");

        //Tierra
        CreadorMovimiento creadorTierra = new CreadorMovimientoTierra();
        Movimiento movimientoTierra = creadorTierra.prepararMovimiento();

        System.out.println("Nombre: " + movimientoTierra.getNombre());
        System.out.println("Tipo: " + movimientoTierra.getTipo());
        System.out.println("Poder: " + movimientoTierra.getPoder());
        movimientoTierra.usarMovimiento();
        System.out.println("Daño calculado: " + movimientoTierra.calcularDanio());

        System.out.println("\n");

        //Aire
        CreadorMovimiento creadorAire = new  CreadorMovimientoAire();
        Movimiento movimientoAire = creadorAire.prepararMovimiento();

        System.out.println("Nombre: " + movimientoAire.getNombre());
        System.out.println("Tipo: " + movimientoAire.getTipo());
        System.out.println("Poder: " + movimientoAire.getPoder());
        movimientoAire.usarMovimiento();
        System.out.println("Daño calculado: " + movimientoAire.calcularDanio());

        System.out.println("\n");

        //Eléctrico

        CreadorMovimiento creadorElectrico = new  CreadorMovimientoElectrico();
        Movimiento movimientoElectrico = creadorElectrico.prepararMovimiento();

        System.out.println("Nombre: " + movimientoElectrico.getNombre());
        System.out.println("Tipo: " + movimientoElectrico.getTipo());
        System.out.println("Poder: " + movimientoElectrico.getPoder());
        movimientoElectrico.usarMovimiento();
        System.out.println("Daño calculado: " + movimientoElectrico.calcularDanio());



    }
}
