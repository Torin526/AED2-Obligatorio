
package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test04ListarMercaderiaPorIdCrecienteORT {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void listadoVacío() {
        retorno = s.listarMercaderiasPorIdAscendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    void listadoIngresoOrdenado() {
        s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        s.registrarMercaderia("COD02", "XX-001-XXX121", "Descripción 2", false, Categoria.OTROS);
        s.registrarMercaderia("COD03", "XX-001-XXX122", "Descripción 3", false, Categoria.OTROS);
        retorno = s.listarMercaderiasPorIdAscendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("COD01;XX-001-XXX123;Descripción 1;false;Otros|COD02;XX-001-XXX121;Descripción 2;false;Otros|COD03;XX-001-XXX122;Descripción 3;false;Otros", retorno.getValorString());
    }

    @Test
    void listadoIngresoDesordenado() {
        s.registrarMercaderia("COD03", "XX-001-XXX122", "Descripción 3", false, Categoria.OTROS);
        s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        s.registrarMercaderia("COD02", "XX-001-XXX121", "Descripción 2", false, Categoria.OTROS);
        retorno = s.listarMercaderiasPorIdAscendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("COD01;XX-001-XXX123;Descripción 1;false;Otros|COD02;XX-001-XXX121;Descripción 2;false;Otros|COD03;XX-001-XXX122;Descripción 3;false;Otros", retorno.getValorString());
    }

}
