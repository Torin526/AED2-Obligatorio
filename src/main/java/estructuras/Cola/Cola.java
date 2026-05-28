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
        this.fin.setSig(nuevoNodo);
        this.fin = nuevoNodo;
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
