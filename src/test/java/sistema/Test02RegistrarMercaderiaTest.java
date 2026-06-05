package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test02RegistrarMercaderiaTest {
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

        // Variación de Mayúsculas/Minúsculas en el ID

        retorno = s.registrarMercaderia("cod01", "XX-001-AAA111", "Descripción minúscula", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void registrarMercaderiaError1() {
        // Cadenas vacías
        retorno = s.registrarMercaderia("", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "XX-001-XXX123", "", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        // Categoría nula
        retorno = s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        // Parámetros nulos (Corregido de OK a ERROR_1)
        retorno = s.registrarMercaderia(null, "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", null, "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "XX-001-XXX123", null, false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        // Cadenas con espacios en blanco (Corregido de OK a ERROR_1)
        retorno = s.registrarMercaderia("  ", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "   ", "Descripción 1", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarMercaderia("COD01", "XX-001-XXX123", "   ", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void registrarMercaderiaError2() {
        // Formato de código inválido
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
        // Caso estándar: Ya existe una mercadería con el mismo ID exacto
        s.registrarMercaderia("COD01", "XX-001-XXX234", "Descripción 1", false, Categoria.OTROS);
        retorno = s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 2", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());

        // Caso de borde: Reintentar otra duplicación del mismo ID pero con distintos datos
        retorno = s.registrarMercaderia("COD01", "XX-001-ZZZ999", "Descripción 3", true, Categoria.TEXTIL);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());


    }

    @Test
    void registrarMercaderiaError4() {
        // Caso estándar: Ya existe una mercadería con el mismo código de barras exacto
        s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);
        retorno = s.registrarMercaderia("COD02", "XX-001-XXX123", "Descripción 2", false, Categoria.OTROS);
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());

        // Caso de borde: Intentar duplicar el mismo código por tercera vez con otro ID diferente
        retorno = s.registrarMercaderia("COD03", "XX-001-XXX123", "Descripción 3", true, Categoria.ALIMENTOS);
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());


    }


}