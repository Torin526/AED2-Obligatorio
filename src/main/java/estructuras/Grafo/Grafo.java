package estructuras.Grafo;

import dominio.CentroLogistico;
import dominio.Conexion;
import estructuras.Lista.ListaImp;
import estructuras.Cola.Cola;
import estructuras.Tupla.Tupla;

public class Grafo implements IGrafo {

    private int tope;
    private int cantActual;
    private boolean esDirigido;

    private CentroLogistico[] vertices;
    private Arista<Conexion>[][] matAdy;

    public int getTope() {
        return tope;
    }

    public void setTope(int tope) {
        this.tope = tope;
    }

    public int getCantActual() {
        return cantActual;
    }

    public void setCantActual(int cantActual) {
        this.cantActual = cantActual;
    }

    public Grafo(int tope) {
        this(tope, true);
    }

    public Grafo(int tope, boolean esDirigido) {
        this.tope = tope;
        this.esDirigido = esDirigido;
        this.cantActual = 0;

        this.vertices = new CentroLogistico[tope];
        this.matAdy = new Arista[tope][tope];

        for (int i = 0; i < tope; i++) {
            for (int j = 0; j < tope; j++) {
                this.matAdy[i][j] = new Arista();
            }
        }
    }

    private int obtenerPosLibre() {
        for (int i = 0; i < tope; i++) {
            if (this.vertices[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private int obtenerPosVertice(CentroLogistico vertice) {
        for (int i = 0; i < tope; i++) {
            if (this.vertices[i] != null && this.vertices[i].compareTo(vertice) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void agregarVertice(CentroLogistico vert) {
        int posLibre = this.obtenerPosLibre();
        if (posLibre != -1) {
            this.vertices[posLibre] = vert;
            this.cantActual++;
        }
    }

    @Override
    public void agregarArista(CentroLogistico vOrigen, CentroLogistico vDestino, Conexion peso) {
        int posOrig = this.obtenerPosVertice(vOrigen);
        int posDest = this.obtenerPosVertice(vDestino);

        if (posOrig != -1 && posDest != -1) {
            this.matAdy[posOrig][posDest].setExiste(true);
            this.matAdy[posOrig][posDest].setPeso(peso);

            if (!this.esDirigido) {
                this.matAdy[posDest][posOrig].setExiste(true);
                this.matAdy[posDest][posOrig].setPeso(peso);
            }
        }
    }

    @Override
    public void borrarVertice(CentroLogistico vertice) {
        int posVert = this.obtenerPosVertice(vertice);
        if (posVert != -1) {
            this.vertices[posVert] = null;
            this.cantActual--;

            for (int i = 0; i < this.tope; i++) {
                this.matAdy[i][posVert] = new Arista();
                this.matAdy[posVert][i] = new Arista();
            }
        }
    }

    @Override
    public void borrarArista(CentroLogistico vOrigen, CentroLogistico vDestino) {
        int posOrig = this.obtenerPosVertice(vOrigen);
        int posDest = this.obtenerPosVertice(vDestino);
        if (posOrig != -1 && posDest != -1) {
            this.matAdy[posOrig][posDest] = new Arista();

            if (!this.esDirigido) {
                this.matAdy[posDest][posOrig] = new Arista();
            }
        }
    }

    @Override
    public ListaImp<CentroLogistico> verticesAdyacentes(CentroLogistico vertice) {
        ListaImp<CentroLogistico> adyacentes = new ListaImp<>();
        int posVertice = this.obtenerPosVertice(vertice);
        if (posVertice != -1) {
            for (int i = 0; i < this.tope; i++) {
                if (this.matAdy[posVertice][i].isExiste()) {
                    adyacentes.insertarAlInicio(this.vertices[i]);
                }
            }
        }
        return adyacentes;
    }

    @Override
    public ListaImp<CentroLogistico> verticesIncidentes(CentroLogistico vertice) {
        ListaImp<CentroLogistico> incidentes = new ListaImp<>();
        int posVertice = this.obtenerPosVertice(vertice);
        if (posVertice != -1) {
            for (int i = 0; i < this.tope; i++) {
                if (this.matAdy[i][posVertice].isExiste()) {
                    incidentes.insertarAlInicio(this.vertices[i]);
                }
            }
        }
        return incidentes;
    }

    @Override
    public boolean sonAdyacentes(CentroLogistico vOrigen, CentroLogistico vDestino) {
        int posOrig = this.obtenerPosVertice(vOrigen);
        int posDest = this.obtenerPosVertice(vDestino);
        if (posOrig != -1 && posDest != -1) {
            return this.matAdy[posOrig][posDest].isExiste();
        }
        return false;
    }

    @Override
    public boolean existeVertice(CentroLogistico vertice) {
        return this.obtenerPosVertice(vertice) > -1;
    }

    @Override
    public ListaImp<CentroLogistico> bfsConNivelYCantidadDeNiveles(CentroLogistico vert, int cantidad) {
        ListaImp<CentroLogistico> listaRet = new ListaImp<>();
        boolean[] visitados = new boolean[this.tope];

        int inicio = obtenerPosVertice(vert);
        if (inicio == -1) return listaRet;

        Cola<Tupla> cola = new Cola<>();
        visitados[inicio] = true;
        cola.encolar(new Tupla(inicio, 0));

        while (!cola.esVacia()) {
            Tupla tupla = cola.desencolar();
            int pos = tupla.getPos();
            int nivel = tupla.getNivel();

            if (nivel > cantidad) {
                break;
            }

            listaRet.insertarAlFinal(vertices[pos]);

            if (nivel < cantidad) {
                for (int j = 0; j < this.tope; j++) {
                    if (matAdy[pos][j].isExiste() && !visitados[j]) {
                        cola.encolar(new Tupla(j, nivel + 1));
                        visitados[j] = true;
                    }
                }
            }
        }
        return listaRet;
    }

    private void caminoMasCortoSegunCriterio(CentroLogistico verticeInicial, String criterio, int[] costos, CentroLogistico[] anteriores) {
        boolean[] visitados = new boolean[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            costos[i] = Integer.MAX_VALUE;
            visitados[i] = false;
            anteriores[i] = null;
        }

        int posVerticeInicial = this.obtenerPosVertice(verticeInicial);
        if (posVerticeInicial == -1) return;

        costos[posVerticeInicial] = 0;

        for (int i = 0; i < vertices.length; i++) {
            int posVerMenorCosto = this.obtenerPosVerticeMenorCosto(costos, visitados);
            if (posVerMenorCosto > -1) {
                visitados[posVerMenorCosto] = true;

                for (int j = 0; j < vertices.length; j++) {
                    // Validamos que el vértice de destino 'j' exista (no sea null por un borrado anterior)
                    if (this.vertices[j] != null && matAdy[posVerMenorCosto][j].isExiste() && !visitados[j]) {

                        int pesoArista = 0;
                        Conexion conexion = matAdy[posVerMenorCosto][j].getPeso();

                        if (criterio.equals("DISTANCIA")) {
                            pesoArista = conexion.getDistancia();
                        } else {
                            pesoArista = conexion.getTiempo();
                        }

                        if (costos[posVerMenorCosto] != Integer.MAX_VALUE && (costos[j] - pesoArista) > costos[posVerMenorCosto]) {
                            costos[j] = costos[posVerMenorCosto] + pesoArista;
                            anteriores[j] = vertices[posVerMenorCosto];
                        }
                    }
                }
            }
        }
    }

    @Override
    public ListaImp<CentroLogistico> obtenerCaminoMasCorto(CentroLogistico origen, CentroLogistico destino, String criterio, int[] contador) {
        int[] costos = new int[this.tope];
        CentroLogistico[] anteriores = new CentroLogistico[this.tope];

        caminoMasCortoSegunCriterio(origen, criterio, costos, anteriores);

        int posDestino = obtenerPosVertice(destino);


        if (posDestino == -1) {
            return null;
        }


        if (costos[posDestino] == Integer.MAX_VALUE) {
            return null;
        }

        contador[0] = costos[posDestino];

        ListaImp<CentroLogistico> camino = new ListaImp<>();
        int posActual = posDestino;
        int posOrigen = obtenerPosVertice(origen);

        while (posActual != posOrigen) {
            camino.insertarAlInicio(vertices[posActual]);
            CentroLogistico nomAnterior = anteriores[posActual];
            posActual = obtenerPosVertice(nomAnterior);
        }
        camino.insertarAlInicio(vertices[posOrigen]);

        return camino;
    }

    private int obtenerPosVerticeMenorCosto(int[] costos, boolean[] visitados) {
        int minPos = -1;
        int minCosto = Integer.MAX_VALUE;
        for (int i = 0; i < costos.length; i++) {
            if (costos[i] < minCosto && !visitados[i]) {
                minPos = i;
                minCosto = costos[i];
            }
        }
        return minPos;
    }

    @Override
    public CentroLogistico obtenerVerticePorCodigo(String codigo) {
        CentroLogistico verticeFantasma = new CentroLogistico(codigo, null, null, null);
        int pos = obtenerPosVertice(verticeFantasma);
        if (pos != -1) {
            return vertices[pos];
        }
        return null;
    }
}