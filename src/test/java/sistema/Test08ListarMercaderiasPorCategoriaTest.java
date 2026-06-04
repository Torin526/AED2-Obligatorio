
package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test08ListarMercaderiasPorCategoriaTest {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }



    @Test
    void listarMercaderiasPorCategoriaVacia() {
        // Caso de borde: No hay ninguna mercadería en el sistema para la categoría consultada
        s.registrarMercaderia("12345", "MN-001-ABC123", "Remera", false, Categoria.TEXTIL);

        // Buscamos otra categoría que está vacía
        retorno = s.listarMercaderiasPorCategoria(Categoria.ALIMENTOS);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    void listarMercaderiasPorCategoriaUnSoloElemento() {
        // Caso límite: Hay un solo elemento en esa categoría (no debe incluir el separador "|")
        s.registrarMercaderia("12345", "MN-001-ABC123", "Batería de cocina", false, Categoria.OTROS);
        s.registrarMercaderia("55555", "XX-999-ZZZ", "Fideos", true, Categoria.ALIMENTOS); // Otra categoría

        retorno = s.listarMercaderiasPorCategoria(Categoria.OTROS);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345;MN-001-ABC123;Batería de cocina;false;OTROS", retorno.getValorString());
    }

    @Test
    void listarMercaderiasPorCategoriaFiltradoYOrdenOk() {
        // Caso estándar: Se ingresan mercaderías de varias categorías en desorden de ID.
        // Debe ignorar las de otras categorías y ordenar las correctas por ID de forma CRECIENTE.
        s.registrarMercaderia("33333", "TXT-03", "Campera", false, Categoria.TEXTIL);
        s.registrarMercaderia("11111", "ALI-01", "Arroz", true, Categoria.ALIMENTOS); // De otra categoría (debe ignorarse)
        s.registrarMercaderia("44444", "TXT-04", "Pantalón", false, Categoria.TEXTIL);
        s.registrarMercaderia("22222", "TXT-02", "Medias", true, Categoria.TEXTIL);

        retorno = s.listarMercaderiasPorCategoria(Categoria.TEXTIL);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Estricto orden lexicográfico creciente de IDs para la categoría TEXTIL: 22222 -> 33333 -> 44444
        String esperado = "22222;TXT-02;Medias;true;TEXTIL|" +
                "33333;TXT-03;Campera;false;TEXTIL|" +
                "44444;TXT-04;Pantalón;false;TEXTIL";

        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void listarMercaderiasPorCategoriaParametroNull() {
        // Caso de borde: Si bien la letra dice que no hay errores, pasar un null por parámetro
        // no debería romper la aplicación (NullPointerException). El sistema debe responder correctamente.
        retorno = s.listarMercaderiasPorCategoria(null);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }


}
