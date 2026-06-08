package estructuras.ABB;

import estructuras.Lista.ListaImp;

public class ABB<T extends Comparable<T>> implements IABB<T> {
    private NodoGen<T> raiz;

    public ABB() {
    }

    public NodoGen<T> getRaiz() {
        return raiz;
    }

    @Override
    public void insertar(T valor) {
        if (this.raiz == null) {
            this.raiz = new NodoGen<>(valor);
        } else {
            insertar(this.raiz, valor);
        }
    }

    private void insertar(NodoGen<T> nodo, T valor) {
        if (nodo != null) {
            if (valor.compareTo(nodo.getDato()) > 0) {

                if (nodo.getDer() != null) {
                    insertar(nodo.getDer(), valor);
                } else {
                    nodo.setDer(new NodoGen<>(valor));
                }
            } else {

                if (nodo.getIzq() != null) {
                    insertar(nodo.getIzq(), valor);
                } else {
                    nodo.setIzq(new NodoGen<>(valor));
                }
            }
        }
    }

    @Override
    public boolean pertenece(T valor) {

        return pertenece(this.raiz, valor);
    }

    private boolean pertenece(NodoGen<T> nodo, T valor) {
        if (nodo != null) {
            if (nodo.getDato().compareTo(valor) == 0) {
                return true;
            } else if (valor.compareTo(nodo.getDato()) > 0) {
                return pertenece(nodo.getDer(), valor);
            } else {
                return pertenece(nodo.getIzq(), valor);
            }
        }
        return false;
    }



    @Override
    public void listarAsc() {

        listarAsc(this.raiz);
    }

    private void listarAsc(NodoGen<T> nodo) {
        if (nodo != null) {
            listarAsc(nodo.getIzq());
            System.out.print("- " + nodo.getDato().toString());
            listarAsc(nodo.getDer());
        }
    }



    @Override
    public ListaImp<T> obtenerAsc() {
        ListaImp<T> lista=new ListaImp<T>();
        obtenerAsc(lista, this.raiz);
        return lista;
    }

    private void obtenerAsc(ListaImp<T> lista, NodoGen<T> nodo) {
        if(nodo==null){
            return;
        }else {
            obtenerAsc(lista,nodo.getIzq());
            lista.insertarAlFinal(nodo.getDato());
            obtenerAsc(lista,nodo.getDer());

        }
    }


}
