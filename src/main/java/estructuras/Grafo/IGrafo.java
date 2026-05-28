package estructuras.Grafo;

import estructuras.Lista.ListaImp;

public interface IGrafo<V, P> {

    void agregarVertice(V vert);

    void agregarArista(V vOrigen, V vDestino, P peso);

    void borrarVertice(V vertice);

    void borrarArista(V vOrigen, V vDestino);

    ListaImp<V> verticesAdyacentes(V vertice);

    ListaImp<V> verticesIncidentes(V vertice);

    boolean sonAdyacentes(V vOrigen, V vDestino);

    boolean existeVertice(V vertice);
}