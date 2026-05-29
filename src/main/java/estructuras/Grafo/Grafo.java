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
        this.vertices = (CentroLogistico[]) new Object[tope];
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
            if (this.vertices[i] != null && this.vertices[i].equals(vertice)) {
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
                    adyacentes.insertar(this.vertices[i]);
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
                    incidentes.insertar(this.vertices[i]);
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




    public ListaImp<CentroLogistico> bfsConNivelYCantidadDeNiveles(CentroLogistico vert, int cantidad) {
        ListaImp<CentroLogistico> listaRet=new ListaImp<CentroLogistico>();
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



    public void dijkstra(String verticeInicial) {
        /*
        1-marcar arreglo de costos en inf.
        2-definir los visitados como false.
        3-definir anteriores como null.
        4- marcar vertice inicial como  costo 0

        Para cada vertice:

            5- Visitar vertice de menor costo no visitado
            6- A sus adyacentes no visitados actualizamos costos


        * */

        int[] costos = new int[vertices.length];
        boolean[] visitados = new boolean[vertices.length];
        String[] anteriores = new String[vertices.length];

        for (int i = 0; i < costos.length; i++) {
            costos[i] = Integer.MAX_VALUE;
            visitados[i] = false;
            anteriores[i] = "**";
        }

        int posVerticeInicial = this.obtenerPosVertice(verticeInicial);
        costos[posVerticeInicial] = 0;

        for (int i = 0; i < vertices.length; i++) {
            int posVerMenorCosto = this.obtenerPosVerticeMenorCosto(costos, visitados);
            if (posVerMenorCosto > -1) {
                visitados[posVerMenorCosto] = true;
                for (int j = 0; j < vertices.length; j++) {
                    if (matAdy[posVerMenorCosto][j].isExiste() && !visitados[j]) {
                        if (costos[j] > (costos[posVerMenorCosto] + matAdy[posVerMenorCosto][j].getPeso())) {
                            costos[j] = costos[posVerMenorCosto] + matAdy[posVerMenorCosto][j].getPeso();
                            anteriores[j] = vertices[posVerMenorCosto];
                        }
                    }
                }
            }
        }

        for (int i = 0; i < vertices.length; i++) {
            System.out.println(vertices[i] + " -> (" + costos[i] + "," + anteriores[i] + ")");
        }
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
}