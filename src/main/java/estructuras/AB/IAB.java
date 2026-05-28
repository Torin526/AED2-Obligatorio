package estructuras.AB;

public interface IAB<T> {

    int cantNodos();

    int cantHojas();

    int altura();

    boolean pertenece(T x);

    boolean iguales(AB<T> a);

    boolean equilibrado();
}