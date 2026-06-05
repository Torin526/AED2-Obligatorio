package estructuras.Cola;

import estructuras.Lista.Nodo;



public class Cola<T> implements ICola<T>{

    private Nodo<T> inicio;
    private Nodo<T> fin;
    private int cant;


    @Override
    public void encolar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<T>(dato, null);
        cant++;

        // CORREGIDO: Evaluamos si la cola está vacía
        if (this.inicio == null) {
            // Si está vacía, el nuevo nodo es el primero y también el último
            this.inicio = nuevoNodo;
            this.fin = nuevoNodo;
        } else {
            // Si ya tenía elementos, lo enganchamos al final de forma normal
            this.fin.setSig(nuevoNodo);
            this.fin = nuevoNodo; // El fin avanza al nuevo nodo
        }
    }

    @Override
    public T desencolar() {
        if (this.inicio == null) {
            return null;
        }

        T dato = this.inicio.getDato();
        this.inicio = this.inicio.getSig();
        if (this.inicio == null) {
            this.fin = null;
        }

        return dato;
    }

    @Override
    public boolean esVacia() {
        return inicio==null;
    }



    @Override
    public int cantElementos() {
        return cant;
    }

    @Override
    public T frente() {
        if (this.inicio == null) {
            return null;
        }
        return this.inicio.getDato();
    }

    @Override
    public void imprimirDatos() {

    }
}
