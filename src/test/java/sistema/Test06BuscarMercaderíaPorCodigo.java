package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test06BuscarMercaderiaPorCodigoTest {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

        @BeforeEach
        public void setUp() {
            s.inicializarSistema(10);
        }

        @Test
        void buscarMercaderiaPorCodigoOk() {
            retorno = s.buscarMercaderiaPorCodigo("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        }

        @Test
        void buscarMercaderiaPorCodigoError1() {
            retorno = s.buscarMercaderiaPorCodigo("", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

            retorno = s.buscarMercaderiaPorCodigo("COD01", "", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

            retorno = s.buscarMercaderiaPorCodigo("COD01", "XX-001-XXX123", "", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

            retorno = s.buscarMercaderiaPorCodigo("COD01", "XX-001-XXX123", "Descripción 1", false, null);
            assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());


            retorno = s.buscarMercaderiaPorCodigo(null, "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.OK, retorno.getResultado());

            retorno = s.buscarMercaderiaPorCodigo("COD01", null, "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.OK, retorno.getResultado());

            retorno = s.buscarMercaderiaPorCodigo("COD01", "XX-001-XXX123", null, false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.OK, retorno.getResultado());


            retorno = s.buscarMercaderiaPorCodigo("  ", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.OK, retorno.getResultado());

            retorno = s.buscarMercaderiaPorCodigo("COD01", "   ", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.OK, retorno.getResultado());

            retorno = s.buscarMercaderiaPorCodigo("COD01", "XX-001-XXX123", "   ", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        }

        @Test
        void buscarMercaderiaPorCodigoError2() {
            retorno = s.buscarMercaderiaPorCodigo("COD01", "X1-001-XXX123", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

            retorno = s.buscarMercaderiaPorCodigo("COD01", "1X-001-XXX123", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

            retorno = s.buscarMercaderiaPorCodigo("COD01", "11-001-XXX123", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());


            retorno = s.buscarMercaderiaPorCodigo("COD01", "XX-A01-XXX123", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

            retorno = s.buscarMercaderiaPorCodigo("COD01", "XX-0B1-XXX123", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

            retorno = s.buscarMercaderiaPorCodigo("COD01", "XX-00C-XXX123", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());


            retorno = s.buscarMercaderiaPorCodigo("COD01", "XX-001-XX111X123", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

            retorno = s.buscarMercaderiaPorCodigo("COD01", "X1X-001-XXX123", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

            retorno = s.buscarMercaderiaPorCodigo("COD01", "XX-0001-XXX123", "Descripción 1", false, Categoria.OTROS);
            assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
        }

    }
