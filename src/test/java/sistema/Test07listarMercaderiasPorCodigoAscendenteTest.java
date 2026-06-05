
package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test07listarMercaderiasPorCodigoAscendenteTest {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void listarMercaderiasPorCodigoAscendenteListaVacia() {
        // Caso de borde: No hay ninguna mercadería en el sistema
        retorno = s.listarMercaderiasPorCodigoAscendente();

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    void listarMercaderiasPorCodigoAscendenteUnSoloElemento() {
        // Caso límite: Un único elemento registrado (no debe incluir el separador "|")
        s.registrarMercaderia("12345", "MN-001-ABC123", "Batería de cocina", false, Categoria.OTROS);

        retorno = s.listarMercaderiasPorCodigoAscendente();

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345;MN-001-ABC123;Batería de cocina;false;Otros", retorno.getValorString());
    }

    @Test
    void listarMercaderiasPorCodigoAscendenteOkVarias() {
        // Caso estándar: Se ingresan las mercaderías en desorden alfabético de código.
        // El listado debe ordenarlas de menor a mayor según el CÓDIGO (no por el ID).
        s.registrarMercaderia("99999", "ZZ-999-ZZZ321", "Mercaderia Z", false, Categoria.OTROS);
        s.registrarMercaderia("11111", "AA-111-AAA321", "Mercaderia A", true, Categoria.TEXTIL);
        s.registrarMercaderia("55555", "MM-555-MMM321", "Mercaderia M", false, Categoria.OTROS);

        retorno = s.listarMercaderiasPorCodigoAscendente();

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Estricto orden lexicográfico creciente de CÓDIGOS: AA... -> MM... -> ZZ...
        String esperado = "11111;AA-111-AAA321;Mercaderia A;true;Textil|" +
                "55555;MM-555-MMM321;Mercaderia M;false;Otros|" +
                "99999;ZZ-999-ZZZ321;Mercaderia Z;false;Otros";

        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void listarMercaderiasPorCodigoAscendenteLimiteCapacidad() {
        // Caso límite: El sistema llega al tope de su capacidad (suponiendo que metemos elementos hasta el tope)
        s.registrarMercaderia("3", "CC-333-LLL123", "M3", false, Categoria.OTROS);
        s.registrarMercaderia("1", "AA-111-LLL123", "M1", false, Categoria.OTROS);
        s.registrarMercaderia("2", "BB-222-LLL123", "M2", true, Categoria.OTROS);

        retorno = s.listarMercaderiasPorCodigoAscendente();

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        String esperado = "1;AA-111-LLL123;M1;false;Otros|" +
                "2;BB-222-LLL123;M2;true;Otros|" +
                "3;CC-333-LLL123;M3;false;Otros";

        assertEquals(esperado, retorno.getValorString());
    }

}
