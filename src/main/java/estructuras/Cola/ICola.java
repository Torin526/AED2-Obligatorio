package estructuras.Cola;

public interface ICola<T> {

    void encolar(T dato); //simil push insertamos el elemento en la cola

    T desencolar(); //simil pop extraemos el elemento de la cola y lo devolvemos

    boolean esVacia();


    int cantElementos();

    T frente(); //simil top devolvemos el primer elemento de nuestra cola

    void imprimirDatos();
}