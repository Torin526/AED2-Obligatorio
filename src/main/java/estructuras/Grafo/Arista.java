package estructuras.Grafo;

public class Arista<T> {
    private T peso;
    private boolean existe;

    public Arista() {
        this.peso = null;
        this.existe = false;
    }

    public Arista(T peso) {
        this.peso = peso;
        this.existe = true;
    }

    public T getPeso() {
        return peso;
    }

    public void setPeso(T peso) {
        this.peso = peso;
    }

    public boolean isExiste() {
        return existe;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
    }
}
