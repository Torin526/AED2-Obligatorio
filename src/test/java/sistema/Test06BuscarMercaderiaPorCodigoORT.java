
package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test06BuscarMercaderiaPorCodigoORT {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void buscarMedicamentoOk() {
        s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        retorno = s.buscarMercaderiaPorCodigo("XX-001-XXX123");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(1, retorno.getValorInteger());
        assertEquals("COD01;XX-001-XXX123;Descripción 1;false;Otros", retorno.getValorString());
    }

    @Test
    void buscarMedicamentoError1() {
        retorno = s.buscarMercaderiaPorCodigo("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.buscarMercaderiaPorCodigo("    ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        retorno = s.buscarMercaderiaPorCodigo(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void buscarMedicamentoError2() {
        retorno = s.buscarMercaderiaPorCodigo("XX-001-XXX123");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        retorno = s.buscarMercaderiaPorCodigo("XX-001-XXX121");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

}
