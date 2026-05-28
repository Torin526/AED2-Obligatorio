package estructuras.AB;

public class Nodo<T> {
    private T dato;
    private Nodo<T> izq;
    private Nodo<T> der;

    public Nodo(T dato) {
        this.dato = dato;
        this.izq = null;
        this.der = null;
    }

    public Nodo(T dato, Nodo<T> izq, Nodo<T> der) {
        this.dato = dato;
        this.izq = izq;
        this.der = der;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public Nodo<T> getIzq() {
        return izq;
    }

    public void setIzq(Nodo<T> izq) {
        this.izq = izq;
    }

    public Nodo<T> getDer() {
        return der;
    }

    public void setDer(Nodo<T> der) {
        this.der = der;
    }

    public boolean isHoja() {
        return this.der == null && this.izq == null;
    }
}
