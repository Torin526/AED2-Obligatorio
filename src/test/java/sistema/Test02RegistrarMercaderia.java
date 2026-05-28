package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test02RegistrarMercaderia {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void registrarMercaderiaOk() {
        retorno = s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void registrarMercaderiaError1() {
        retorno = s.registrarMercaderia("", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "XX-001-XXX123", "", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());


        retorno = s.registrarMercaderia(null, "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", null, "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "XX-001-XXX123", null, false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());


        retorno = s.registrarMercaderia("  ", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "   ", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "XX-001-XXX123", "   ", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void registrarMercaderiaError2() {
        retorno = s.registrarMercaderia("COD01", "X1-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "1X-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "11-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());


        retorno = s.registrarMercaderia("COD01", "XX-A01-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "XX-0B1-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "XX-00C-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());


        retorno = s.registrarMercaderia("COD01", "XX-001-XX111X123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "X1X-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "XX-0001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    void registrarMercaderiaError3() {
        s.registrarMercaderia("COD01", "XX-001-XXX234", "Descripción 1", false, Categoria.OTROS);
        retorno = s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void registrarMercaderiaError4() {
        s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        retorno = s.registrarMercaderia("COD02", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

}
