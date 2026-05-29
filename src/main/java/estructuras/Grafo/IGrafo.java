package estructuras.Grafo;

import dominio.CentroLogistico;
import dominio.Conexion;
import estructuras.Lista.ListaImp;

public interface IGrafo {

    void agregarVertice(CentroLogistico vert);

    void agregarArista(CentroLogistico vOrigen, CentroLogistico vDestino, Conexion peso);

    void borrarVertice(CentroLogistico vertice);

    void borrarArista(CentroLogistico vOrigen, CentroLogistico vDestino);

    ListaImp<CentroLogistico> verticesAdyacentes(CentroLogistico vertice);

    ListaImp<CentroLogistico> verticesIncidentes(CentroLogistico vertice);

    boolean sonAdyacentes(CentroLogistico vOrigen, CentroLogistico vDestino);

    boolean existeVertice(CentroLogistico vertice);
}