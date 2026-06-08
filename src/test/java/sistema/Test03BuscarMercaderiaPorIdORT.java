
package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test03BuscarMercaderiaPorIdORT {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void buscarMercaderiaOk() {
        s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        retorno = s.buscarMercaderiaPorId("COD01");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(1, retorno.getValorInteger());
        assertEquals("COD01;XX-001-XXX123;Descripción 1;false;Otros", retorno.getValorString());
    }

    @Test
    void buscarMercaderiaOkValorInt() {
        s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        s.registrarMercaderia("COD02", "XX-001-XXX121", "Descripción 2", false, Categoria.OTROS);
        s.registrarMercaderia("COD03", "XX-001-XXX122", "Descripción 3", false, Categoria.OTROS);
        retorno = s.buscarMercaderiaPorId("COD01");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(1, retorno.getValorInteger());
        assertEquals("COD01;XX-001-XXX123;Descripción 1;false;Otros", retorno.getValorString());
        retorno = s.buscarMercaderiaPorId("COD02");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(2, retorno.getValorInteger());
        assertEquals("COD02;XX-001-XXX121;Descripción 2;false;Otros", retorno.getValorString());
        retorno = s.buscarMercaderiaPorId("COD03");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(3, retorno.getValorInteger());
        assertEquals("COD03;XX-001-XXX122;Descripción 3;false;Otros", retorno.getValorString());
    }

    @Test
    void buscarMercaderiaError1() {
        retorno = s.buscarMercaderiaPorId("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.buscarMercaderiaPorId("    ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.buscarMercaderiaPorId(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void buscarMercaderiaError2() {
        retorno = s.buscarMercaderiaPorId("COD01");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        retorno = s.buscarMercaderiaPorId("COD02");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

}
