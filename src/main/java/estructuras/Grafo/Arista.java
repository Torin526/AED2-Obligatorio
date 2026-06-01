package estructuras.Grafo;

public class Arista<P> {
    private P peso;
    private boolean existe;

    public Arista() {
        this.peso = null;
        this.existe = false;
    }

    public Arista(P peso) {
        this.peso = peso;
        this.existe = true;
    }

    public P getPeso() {
        return peso;
    }

    public void setPeso(P peso) {
        this.peso = peso;
    }

    public boolean isExiste() {
        return existe;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
    }
}
