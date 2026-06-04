package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test10RegistrarConexion {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void registrarConexionOk() {
        retorno = s.registrarConexion("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void registrarConexionError1() {
        retorno = s.registrarConexion("", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarConexion("COD01", "", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarConexion("COD01", "XX-001-XXX123", "", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarConexion("COD01", "XX-001-XXX123", "Descripción 1", false, null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());


        retorno = s.registrarConexion(null, "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarConexion("COD01", null, "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarConexion("COD01", "XX-001-XXX123", null, false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());


        retorno = s.registrarConexion("  ", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarConexion("COD01", "   ", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarConexion("COD01", "XX-001-XXX123", "   ", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void registrarConexionError2() {
        retorno = s.registrarConexion("COD01", "X1-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarConexion("COD01", "1X-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarConexion("COD01", "11-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());


        retorno = s.registrarConexion("COD01", "XX-A01-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarConexion("COD01", "XX-0B1-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarConexion("COD01", "XX-00C-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());


        retorno = s.registrarConexion("COD01", "XX-001-XX111X123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarConexion("COD01", "X1X-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarConexion("COD01", "XX-0001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    void registrarConexionError3() {
        s.registrarConexion("COD01", "XX-001-XXX234", "Descripción 1", false, Categoria.OTROS);
        retorno = s.registrarConexion("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void registrarConexionError4() {
        s.registrarConexion("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        retorno = s.registrarConexion("COD02", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void registrarConexionError5() {
        s.registrarConexion("COD01", "XX-001-XXX234", "Descripción 1", false, Categoria.OTROS);
        retorno = s.registrarConexion("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void registrarConexionError6() {
        s.registrarConexion("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        retorno = s.registrarConexion("COD02", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

}
