package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test05ListarMercaderiasPorIdDescendenteTest {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void listarMercaderiasPorIdDescendenteListaVacia() {
        // Caso de borde: No hay ninguna mercadería en el sistema
        retorno = s.listarMercaderiasPorIdDescendente();

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    void listarMercaderiasPorIdDescendenteUnSoloElemento() {
        // Caso límite: Un único elemento registrado (no debe incluir el separador "|")
        s.registrarMercaderia("12345", "MN-001-ABC123", "Batería de cocina", false, Categoria.OTROS);

        retorno = s.listarMercaderiasPorIdDescendente();

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345;MN-001-ABC123;Batería de cocina;false;OTROS", retorno.getValorString());
    }

    @Test
    void listarMercaderiasPorIdDescendenteOkVarias() {
        // Caso estándar exitoso: Se ingresan en desorden y debe ordenarlas de Mayor a Menor (descendente)
        s.registrarMercaderia("11111", "XX-001-AAA111", "Mercaderia A", true, Categoria.OTROS);
        s.registrarMercaderia("33333", "XX-001-CCC333", "Mercaderia C", false, Categoria.OTROS);
        s.registrarMercaderia("22222", "XX-001-BBB222", "Mercaderia B", false, Categoria.OTROS);

        retorno = s.listarMercaderiasPorIdDescendente();

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Estricto orden lexicográfico decreciente: 33333 -> 22222 -> 11111
        String esperado = "33333;XX-001-CCC333;Mercaderia C;false;OTROS|" +
                "22222;XX-001-BBB222;Mercaderia B;false;OTROS|" +
                "11111;XX-001-AAA111;Mercaderia A;true;OTROS";

        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void listarMercaderiasPorIdDescendenteLimiteCapacidad() {
        // Caso límite: El sistema está lleno (alcanza el tope de la inicialización)
        s.registrarMercaderia("A", "XX-001-AAA111", "M1", false, Categoria.OTROS);
        s.registrarMercaderia("C", "XX-001-CCC333", "M3", false, Categoria.OTROS);
        s.registrarMercaderia("B", "XX-001-BBB222", "M2", false, Categoria.OTROS);

        retorno = s.listarMercaderiasPorIdDescendente();

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        String esperado = "C;XX-001-CCC333;M3;false;OTROS|" +
                "B;XX-001-BBB222;M2;false;OTROS|" +
                "A;XX-001-AAA111;M1;false;OTROS";

        assertEquals(esperado, retorno.getValorString());
    }

}
