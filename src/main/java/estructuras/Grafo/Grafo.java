package estructuras.Grafo;

import estructuras.Lista.ListaImp;
import estructuras.Cola.Cola;
import estructuras.Tupla.Tupla;

public class Grafo<V, P> implements IGrafo<V, P> {

    private int tope;
    private int cantActual;
    private boolean esDirigido;

    private V[] vertices;
    private Arista[][] matAdy;

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

        // Casteamos. Necesario en java, sino se rompe
        this.vertices = (V[]) new Object[tope];
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

    private int obtenerPosVertice(V vertice) {
        for (int i = 0; i < tope; i++) {
            if (this.vertices[i] != null && this.vertices[i].equals(vertice)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void agregarVertice(V vert) {
        int posLibre = this.obtenerPosLibre();
        if (posLibre != -1) {
            this.vertices[posLibre] = vert;
            this.cantActual++;
        }
    }

    @Override
    public void agregarArista(V vOrigen, V vDestino, P peso) {

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
    public void borrarVertice(V vertice) {
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
    public void borrarArista(V vOrigen, V vDestino) {
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
    public ListaImp<V> verticesAdyacentes(V vertice) {
        ListaImp<V> adyacentes = new ListaImp<>();
        int posVertice = this.obtenerPosVertice(vertice);
        if (posVertice != -1) {
            for (int i = 0; i < this.tope; i++) {
                if (this.matAdy[posVertice][i].isExiste()) {
                    adyacentes.insertar(this.vertices[i]);
                }
            }
        }
        return adyacentes;
    }

    @Override
    public ListaImp<V> verticesIncidentes(V vertice) {
        ListaImp<V> incidentes = new ListaImp<>();
        int posVertice = this.obtenerPosVertice(vertice);
        if (posVertice != -1) {
            for (int i = 0; i < this.tope; i++) {
                if (this.matAdy[i][posVertice].isExiste()) {
                    incidentes.insertar(this.vertices[i]);
                }
            }
        }
        return incidentes;
    }

    @Override
    public boolean sonAdyacentes(V vOrigen, V vDestino) {

        int posOrig = this.obtenerPosVertice(vOrigen);
        int posDest = this.obtenerPosVertice(vDestino);
        if (posOrig != -1 && posDest != -1) {
            return this.matAdy[posOrig][posDest].isExiste();
        }
        return false;
    }

    @Override
    public boolean existeVertice(V vertice) {
        return this.obtenerPosVertice(vertice) > -1;
    }




    public ListaImp<V> bfsConNivelYCantidadDeNiveles(V vert, int cantidad) {
        ListaImp<V> listaRet=new ListaImp<V>();
        boolean[] visitados = new boolean[tope];
        int inicio = obtenerPosVertice(vert);
        Cola<Tupla> cola = new Cola<>();
        visitados[inicio] = true;
        cola.encolar(new Tupla(inicio, 0));
        boolean llegoANivel=false;
        while (!cola.esVacia()&&!llegoANivel) {
            Tupla tupla = cola.desencolar();
            int pos = tupla.getPos();
            int nivel = tupla.getNivel();
            listaRet.insertar(vertices[pos]);
            if (nivel < cantidad) {
                for (int j = 0; j < tope; j++) {
                    if (matAdy[pos][j].isExiste() && !visitados[j]) {
                        cola.encolar(new Tupla(j, nivel + 1));
                        visitados[j] = true;
                    }
                }
            }
        }
    return listaRet;
    }
}