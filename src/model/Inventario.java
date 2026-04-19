package model;

import model.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Inventario {

    private final List<Objeto> objetos = new ArrayList<>();

    public void agregarObjeto(Objeto objeto) {
        objetos.add(objeto);
    }

    /**
     * Elimina y retorna el objeto si existe. Vacío si no lo encuentra.
     */
    public Optional<Objeto> tomarObjeto(int idObjeto) {
        Optional<Objeto> encontrado = objetos.stream()
                .filter(o -> o.id() == idObjeto)
                .findFirst();
        encontrado.ifPresent(objetos::remove);
        return encontrado;
    }

    /**
     * Usa el objeto sobre el pokemon objetivo y lo elimina del inventario si tiene éxito.
     */
    public boolean usarObjeto(int idObjeto, Pokemon objetivo) {
        Optional<Objeto> obj = tomarObjeto(idObjeto);
        if (obj.isEmpty()) return false;

        boolean exito = obj.get().usarObjeto(objetivo);
        if (!exito) {
            objetos.add(obj.get()); // devuelve el objeto si no se pudo usar
        }
        return exito;
    }

    public List<Objeto> getObjetos() {
        return Collections.unmodifiableList(objetos);
    }

    public boolean estaVacio() {
        return objetos.isEmpty();
    }

    public int cantidad() {
        return objetos.size();
    }

    @Override
    public String toString() {
        if (objetos.isEmpty()) return "Inventario vacío";
        StringBuilder sb = new StringBuilder("Inventario:\n");
        objetos.forEach(o -> sb.append("  [")
                .append(o.id()).append("] ")
                .append(o.nombre()).append(" — ")
                .append(o.descripcion()).append("\n"));
        return sb.toString();
    }
}
