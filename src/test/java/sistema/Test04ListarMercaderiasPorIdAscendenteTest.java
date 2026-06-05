package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test04ListarMercaderiasPorIdAscendenteTest {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void listarMercaderiasPorIdAscendenteOk() {
        s.registrarMercaderia("COD01", "XX-001-XXX120", "Descripción 1", false, Categoria.OTROS);
        s.registrarMercaderia("COD02", "XX-001-XXX121", "Descripción 1", false, Categoria.OTROS);
        s.registrarMercaderia("COD06", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        retorno = s.listarMercaderiasPorIdAscendente();
        String aDevolver ="COD01;XX-001-XXX120;Descripción 1;false;Otros|COD02;XX-001-XXX121;Descripción 1;false;Otros|COD06;XX-001-XXX123;Descripción 1;false;Otros";
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(aDevolver, retorno.getValorString());
    }

    @Test
    void listarMercaderiasPorIdAscendenteListaVacia() {
        // Escenario borde: El sistema se inicializa pero no tiene ninguna mercadería registrada
        retorno = s.listarMercaderiasPorIdAscendente();

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // Debe devolver un String vacío (o según indique la letra si lleva alguna estructura)
        assertEquals("", retorno.getValorString());
    }

    @Test
    void listarMercaderiasPorIdAscendenteUnSoloElemento() {
        // Escenario borde: Hay un solo elemento, por lo tanto no debe llevar el separador "|"
        s.registrarMercaderia("12345", "MN-001-ABC123", "Batería de cocina", false, Categoria.OTROS);

        retorno = s.listarMercaderiasPorIdAscendente();

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345;MN-001-ABC123;Batería de cocina;false;Otros", retorno.getValorString());
    }

    @Test
    void listarMercaderiasPorIdAscendenteValidarOrdenLexicografico() {
        // Escenario clave: Se registran en desorden para verificar que el método realmente las ordene de forma creciente
        s.registrarMercaderia("33333", "XX-001-AAA111", "Mercaderia C", false, Categoria.OTROS);
        s.registrarMercaderia("11111", "XX-001-BBB222", "Mercaderia A", true, Categoria.OTROS);
        s.registrarMercaderia("22222", "XX-001-CCC333", "Mercaderia B", false, Categoria.OTROS);

        retorno = s.listarMercaderiasPorIdAscendente();

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // El string esperado debe quedar estrictamente en orden: 11111 -> 22222 -> 33333
        String esperado = "11111;XX-001-BBB222;Mercaderia A;true;Otros|22222;XX-001-CCC333;Mercaderia B;false;Otros|33333;XX-001-AAA111;Mercaderia C;false;Otros";

        assertEquals(esperado, retorno.getValorString());
    }

}
