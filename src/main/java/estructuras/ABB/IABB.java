package estructuras.ABB;

import estructuras.Lista.ListaImp;

public interface IABB<T> {
    void insertar(T valor);

    boolean pertenece(T valor);

    void listarAsc();

    ListaImp<T> obtenerAsc();





}
