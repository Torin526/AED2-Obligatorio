package interfaz;

import java.util.Arrays;
import java.util.Objects;

public enum Categoria {
    ELECTRONICA(0, "Electrónica"),
    ALIMENTOS(1, "Alimentos"),
    DOCUMENTACION(2, "Documentación"),
    TEXTIL(3, "Textil"),
    OTROS(4, "Otros");

    private final int indice;
    private final String texto;

    Categoria(int indice, String texto) {
        this.indice = indice;
        this.texto = texto;
    }

    public int getIndice() {
        return indice;
    }

    public String getTexto() {
        return texto;
    }

    public static Categoria fromTexto(String texto) {
        return Arrays.stream(Categoria.values())
                .filter(a -> Objects.equals(a.texto, texto))
                .findFirst()
                .orElse(null);
    }

}
