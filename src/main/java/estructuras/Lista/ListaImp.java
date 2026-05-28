package estructuras.Lista;

public class ListaImp<T> implements ILista<T> {

    private Nodo<T> inicio;
    private Nodo<T> fin;
    private int cant;


    public Nodo<T> getInicio() {
        return inicio;
    }

    @Override
    public void insertar(T dato) {

        Nodo<T> nuevoNodo = new Nodo<T>(dato, inicio);
        cant++;
        if (inicio == null) {
            this.fin = nuevoNodo;
        }
        this.inicio = nuevoNodo;

    }

    public void insertarAlFinal(T dato) {
        Nodo<T> nuevoNodo = new Nodo<T>(dato, null);
        cant++;
        this.fin.setSig(nuevoNodo);
        this.fin = nuevoNodo;

    }


    @Override
    public int largo() {
        return cant;
    }

    @Override
    public boolean existe(T dato) {

        Nodo<T> nodo = inicio;
        while (nodo != null) {
            if (nodo.getDato().equals(dato)) {
                return true;
            }
            nodo = nodo.getSig();
        }
        return false;

    }

    @Override
    public Nodo<T> recuperar(T dato) {
        Nodo<T> nodo = inicio;
        while (nodo != null) {
            if (nodo.getDato().equals(dato)) {
                return nodo;
            }
            nodo = nodo.getSig();
        }
        return null;

    }

    @Override
    public Nodo<T> recuperar(int indice) {
        if (indice < 0 || indice >= cant) {
            return null;
        }
        Nodo<T> nodo = inicio;
        int cont = 0;
        while (nodo != null) {
            if (cont == indice) {
                return nodo;
            }
            nodo = nodo.getSig();
            cont++;
        }
        return null;


}

@Override
public void mostrarIter() {
    Nodo<T> nodo = inicio;
    while (nodo != null) {
        System.out.println(nodo.getDato());
        nodo = nodo.getSig();
    }
}


@Override
public boolean esVacia() {
    return inicio == null;
}

}
