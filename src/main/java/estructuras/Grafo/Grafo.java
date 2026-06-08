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

        // Casteamos. Necesario en java, sino se rompe
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




    public ListaImp<CentroLogistico> bfsConNivelYCantidadDeNiveles(CentroLogistico vert, int cantidad) {
        ListaImp<CentroLogistico> listaRet = new ListaImp<CentroLogistico>();
        boolean[] visitados = new boolean[tope];

        int inicio = obtenerPosVertice(vert);
        if (inicio == -1) return listaRet; // Control de seguridad

        Cola<Tupla> cola = new Cola<>();
        visitados[inicio] = true;
        cola.encolar(new Tupla(inicio, 0));

        while (!cola.esVacia()) {
            Tupla tupla = cola.desencolar();
            int pos = tupla.getPos();
            int nivel = tupla.getNivel();

            // Si el elemento desencolado ya superó el nivel máximo, no lo procesamos
            // y como la cola está ordenada por nivel en BFS, podemos terminar aquí.
            if (nivel > cantidad) {
                break;
            }

            // CORREGIDO: Insertamos TODOS los vértices que estén dentro del rango,
            // incluyendo al origen (inicio), tal como lo exige el caso cantidad = 0.
            listaRet.insertarAlFinal(vertices[pos]);

            // Si todavía no llegamos al límite de conexiones, seguimos expandiendo los vecinos
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

    private void caminoMasCortoSegunCriterio(CentroLogistico verticeInicial, String criterio, int[] costos, CentroLogistico[] anteriores) {
        boolean[] visitados = new boolean[vertices.length];

        // 1. Inicialización de los arrays que nos pasaron por parámetro
        for (int i = 0; i < vertices.length; i++) {
            costos[i] = Integer.MAX_VALUE;
            visitados[i] = false;
            anteriores[i] = null;
        }

        int posVerticeInicial = this.obtenerPosVertice(verticeInicial);
        // Control por si el origen no existe en el grafo
        if (posVerticeInicial == -1) return;

        costos[posVerticeInicial] = 0;

        // 2. Bucle principal de Dijkstra
        for (int i = 0; i < vertices.length; i++) {
            int posVerMenorCosto = this.obtenerPosVerticeMenorCosto(costos, visitados);
            if (posVerMenorCosto > -1) {
                visitados[posVerMenorCosto] = true;

                for (int j = 0; j < vertices.length; j++) {
                    if (matAdy[posVerMenorCosto][j].isExiste() && !visitados[j]) {

                        // Extraemos el peso según el criterio
                        int pesoArista = 0;
                        Conexion conexion = matAdy[posVerMenorCosto][j].getPeso();

                        if (criterio.equals("DISTANCIA")) {
                            pesoArista = conexion.getDistancia();
                        } else {
                            pesoArista = conexion.getTiempo();
                        }

                        // Etapa de relajación (modifica directamente los arrays del parámetro)
                        // CORREGIDO  contra desbordamientos:
                        if (costos[posVerMenorCosto] != Integer.MAX_VALUE && costos[j] > (costos[posVerMenorCosto] + pesoArista)) {
                            costos[j] = costos[posVerMenorCosto] + pesoArista;
                            anteriores[j] = vertices[posVerMenorCosto];
                        }
                    }
                }
            }
        }


   }
    public ListaImp<CentroLogistico> obtenerCaminoMasCorto(CentroLogistico origen, CentroLogistico destino, String criterio, int[] contador) {
        int[] costos = new int[tope];
        CentroLogistico[] anteriores = new CentroLogistico[tope];

        // 1. Corre el Dijkstra privado y llena los arrays internamente en el grafo
        caminoMasCortoSegunCriterio(origen, criterio, costos, anteriores);

        int posDestino = obtenerPosVertice(destino);

        if (costos[posDestino] == Integer.MAX_VALUE) {
            return null; // Devolvemos null indicando que el destino es inalcanzable
        }


        // Como 'costos' ya tiene el acumulado según el criterio que corrió Dijkstra,
        // asignamos directamente el valor final del destino al contador.
        // Si fue "DISTANCIA" guardará los km totales, si fue "TIEMPO" guardará los minutos totales.
        contador[0] = costos[posDestino];

        ListaImp<CentroLogistico> camino = new ListaImp<>();

        // 2. El propio grafo, que SÍ conoce las posiciones, reconstruye el camino hacia atrás
        int posActual = posDestino;
        int posOrigen = obtenerPosVertice(origen);

        while (posActual != posOrigen) {
            camino.insertarAlInicio(vertices[posActual]); // Lo mete en una lista
            CentroLogistico nomAnterior = anteriores[posActual];
            posActual = obtenerPosVertice(nomAnterior);
        }
        camino.insertarAlInicio(vertices[posOrigen]); // Mete el origen al inicio

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

    public CentroLogistico obtenerVerticePorCodigo(String codigo) {
        CentroLogistico verticeFantasma=new CentroLogistico(codigo, null, null, null);
        int pos = obtenerPosVertice(verticeFantasma);
        if (pos != -1) {
            return vertices[pos];
        }
        return null;
    }

}