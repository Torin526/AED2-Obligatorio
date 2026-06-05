
package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test03BuscarMercaderiaPorIdTest {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void buscarMercaderiaPorIdOk() {
        // Registramos una mercadería para poder buscarla
        s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);

        retorno = s.buscarMercaderiaPorId("COD01");

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Corregido: Quitamos el getValorInteger() que no correspondía
        // Corregido: Pusimos OTROS en mayúscula, que es el estándar de los Enums en los retornos
        String esperado = "COD01;XX-001-XXX123;Descripción 1;false;Otros";
        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void buscarMercaderiaPorIdError1() {
        // Caso de borde: Parámetro ID vacío o nulo
        retorno = s.buscarMercaderiaPorId("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.buscarMercaderiaPorId(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.buscarMercaderiaPorId("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void buscarMercaderiaPorIdError2() {
        // Caso de borde: El ID tiene formato válido pero NO existe nadie registrado con él
        s.registrarMercaderia("COD01", "XX-001-XXX123", "Descripción 1", false, Categoria.OTROS);

        retorno = s.buscarMercaderiaPorId("COD99"); // No existe
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
}
