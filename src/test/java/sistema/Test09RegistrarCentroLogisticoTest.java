package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test09RegistrarCentroLogisticoTest {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        // Inicializamos con un máximo de 3 centros para facilitar las pruebas de límite de capacidad (maxCentros)
        s.inicializarSistema(4);

    }

    @Test
    void registrarCentroLogisticoOk() {
        // Caso estándar: Datos correctos y primer registro
        retorno = s.registrarCentroLogistico("CEN01", "Centro MVD", "Montevideo", "Av. Italia 1234");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Registrar un segundo centro para verificar que conviven sin problemas
        retorno = s.registrarCentroLogistico("CEN02", "Centro CAN", "Canelones", "Ruta 5 Km 20");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 2: Parámetros nulos, vacíos o espacios (Validación temprana)
    // =========================================================================

    @Test
    void registrarCentroLogisticoError2Nulls() {
        // Casos de borde: Cada uno de los parámetros siendo null por separado
        retorno = s.registrarCentroLogistico(null, "Centro MVD", "Montevideo", "Av. Italia 1234");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarCentroLogistico("CEN01", null, "Montevideo", "Av. Italia 1234");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarCentroLogistico("CEN01", "Centro MVD", null, "Av. Italia 1234");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarCentroLogistico("CEN01", "Centro MVD", "Montevideo", null);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    void registrarCentroLogisticoError2Vacios() {
        // Casos de borde: Cada uno de los parámetros siendo un String vacío ""
        retorno = s.registrarCentroLogistico("", "Centro MVD", "Montevideo", "Av. Italia 1234");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarCentroLogistico("CEN01", "", "Montevideo", "Av. Italia 1234");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarCentroLogistico("CEN01", "Centro MVD", "", "Av. Italia 1234");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarCentroLogistico("CEN01", "Centro MVD", "Montevideo", "");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    void registrarCentroLogisticoError2Espacios() {
        // Caso de borde: Cadenas que solo contienen espacios en blanco
        retorno = s.registrarCentroLogistico("   ", "Centro MVD", "Montevideo", "Av. Italia 1234");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 3: Código ya existente (Unicidad)
    // =========================================================================

    @Test
    void registrarCentroLogisticoError3CodigoDuplicado() {
        // Registramos el primero con éxito
        s.registrarCentroLogistico("CEN01", "Centro MVD", "Montevideo", "Av. Italia 1234");

        // Intentamos registrar otro con el mismo código exacto (debe fallar aunque cambien los otros datos)
        retorno = s.registrarCentroLogistico("CEN01", "Otro Centro", "Canelones", "Otra Direccion");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 1: Supera maxCentros (Límites de Capacidad)
    // =========================================================================

    @Test
    void registrarCentroLogisticoError1MaxCapacidad() {
        // Inicializamos con tope 4 en el setUp. Registramos exactamente 3 centros (Límite máximo permitido)
        s.registrarCentroLogistico("CEN01", "Centro 1", "MVD", "Dir 1");
        s.registrarCentroLogistico("CEN02", "Centro 2", "CAN", "Dir 2");
        s.registrarCentroLogistico("CEN03", "Centro 3", "LAN", "Dir 3");
        retorno = s.registrarCentroLogistico("CEN04", "Centro 4", "MAL", "Dir 4");

        // El tercero todavía tiene que entrar bien (OK)
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // El cuarto centro supera la capacidad máxima (debe dar ERROR_1)
        retorno = s.registrarCentroLogistico("CEN05", "Centro 5 (Excedente)", "SJO", "Dir 5");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

}
