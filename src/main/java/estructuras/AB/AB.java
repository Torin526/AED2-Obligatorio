package estructuras.AB;

public class AB<T> implements IAB<T> {

    private final Nodo<T> raiz;

    public AB(Nodo<T> raiz) {
        this.raiz = raiz;
    }

    @Override
    public int cantNodos() {
        return cantNodos(this.raiz);
    }

    private int cantNodos(Nodo<T> nodo) {
        if (nodo != null) {
            return 1 + cantNodos(nodo.getIzq()) + cantNodos(nodo.getDer());
        }
        return 0;
    }

    @Override
    public int cantHojas() {
        return cantHojas(this.raiz);
    }

    private int cantHojas(Nodo<T> nodo) {
        if (nodo != null) {
            if (nodo.isHoja()) {
                return 1;
            }
            return cantHojas(nodo.getIzq()) + cantHojas(nodo.getDer());
        }
        return 0;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }




    private int altura(Nodo<T> nodo) {
        if (nodo != null) {
            if (nodo.isHoja()) {
                return 0;
            }
            return 1 + Math.max(altura(nodo.getIzq()), altura(nodo.getDer()));
        }
        return -1;
    }

 

 

    @Override
    public boolean iguales(AB<T> a) {
        return false;
    }

    @Override
    public boolean equilibrado() {
        return equilibrado(this.raiz);
    }

    private boolean equilibrado(Nodo<T> nodo) {
        if (nodo != null) {
            if (Math.abs(altura(nodo.getIzq()) - altura(nodo.getDer())) > 1) {
                return false;
            }
            return equilibrado(nodo.getIzq()) && equilibrado(nodo.getDer());
        }
        return true;
    }
    @Override
    public boolean pertenece(T x) {
        return pertenece(this.raiz, x);
    }


    private boolean pertenece(Nodo<T> nodo, T x) {
        if (nodo != null) {
            if (nodo.getDato().equals(x)) {
                return true;
            }
            return pertenece(nodo.getIzq(), x) || pertenece(nodo.getDer(), x);
        }
        return false;
    }
}