package estructuras.ABB;

import estructuras.Lista.ListaImp;

public interface IABB<T> {
    void insertar(T valor);

    boolean pertenece(T valor);

    T borrarMinimo();

    void listarAsc();

    void listarDesc();

    ListaImp<T> obtenerAsc();

    ListaImp<T> obtenerDesc();

    void imprimirElementosDeNivel(int nivel);

    //boolean existe(int id);

}
