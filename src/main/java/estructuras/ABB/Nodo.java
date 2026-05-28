package estructuras.ABB;

public class NodoGen<T> {
    private T dato;
    private NodoGen<T> der;
    private NodoGen<T> izq;

    public NodoGen(T dato) {
        this.dato = dato;
    }

    public NodoGen(T dato, NodoGen<T> der, NodoGen<T> izq) {
        this.dato = dato;
        this.der = der;
        this.izq = izq;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoGen<T> getDer() {
        return der;
    }

    public void setDer(NodoGen<T> der) {
        this.der = der;
    }

    public NodoGen<T> getIzq() {
        return izq;
    }

    public void setIzq(NodoGen<T> izq) {
        this.izq = izq;
    }
}
