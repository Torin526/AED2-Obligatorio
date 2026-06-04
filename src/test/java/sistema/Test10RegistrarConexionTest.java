package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test10RegistrarConexionTest {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
        // Registramos un par de centros válidos para usar en las pruebas
        s.registrarCentroLogistico("CEN01", "Centro MVD", "Montevideo", "Av. Italia");
        s.registrarCentroLogistico("CEN02", "Centro CAN", "Canelones", "Ruta 5");
        s.registrarCentroLogistico("CEN03", "Centro MAL", "Maldonado", "Ruta Interbalnearia");
    }

    @Test
    void registrarConexionOk() {
        // Caso estándar: Conexión válida de origen a destino con valores mayores a cero
        retorno = s.registrarConexion("CEN01", "CEN02", 50, 45);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // La nota dice que NO son navegables en ambos sentidos.
        // Por lo tanto, registrar la inversa (CEN02 -> CEN01) tiene que ser totalmente válido y dar OK.
        retorno = s.registrarConexion("CEN02", "CEN01", 50, 45);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 1: Parámetros nulos, vacíos o espacios
    // =========================================================================

    @Test
    void registrarConexionError1Nulls() {
        // Casos de borde: Códigos nulos
        retorno = s.registrarConexion(null, "CEN02", 50, 45);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarConexion("CEN01", null, 50, 45);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void registrarConexionError1Vacios() {
        // Casos de borde: Códigos vacíos o con espacios
        retorno = s.registrarConexion("", "CEN02", 50, 45);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarConexion("CEN01", "   ", 50, 45);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 2 y 3: Inexistencia de Centros
    // =========================================================================

    @Test
    void registrarConexionError2OrigenNoExiste() {
        // Caso de borde: El destino existe pero el origen no está registrado en el sistema
        retorno = s.registrarConexion("FANTASMA", "CEN02", 50, 45);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    void registrarConexionError3DestinoNoExiste() {
        // Caso de borde: El origen existe pero el destino no está registrado en el sistema
        retorno = s.registrarConexion("CEN01", "FANTASMA", 50, 45);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 4: Distancia inválida (Menor o igual a cero)
    // =========================================================================

    @Test
    void registrarConexionError4DistanciaInvalida() {
        // Caso límite: Distancia exactamente cero (invalida según la letra)
        retorno = s.registrarConexion("CEN01", "CEN02", 0, 45);
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());

        // Caso límite: Distancia negativa
        retorno = s.registrarConexion("CEN01", "CEN02", -10, 45);
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 5: Tiempo inválido (Menor o igual a cero)
    // =========================================================================

    @Test
    void registrarConexionError5TiempoInvalido() {
        // Caso límite: Tiempo exactamente cero (invalido según la letra)
        retorno = s.registrarConexion("CEN01", "CEN02", 50, 0);
        assertEquals(Retorno.Resultado.ERROR_5, retorno.getResultado());

        // Caso límite: Tiempo negativo
        retorno = s.registrarConexion("CEN01", "CEN02", 50, -5);
        assertEquals(Retorno.Resultado.ERROR_5, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 6: Conexión ya existente (Duplicados en el mismo sentido)
    // =========================================================================

    @Test
    void registrarConexionError6YaExiste() {
        // Registramos una primera conexión válida
        s.registrarConexion("CEN01", "CEN02", 50, 45);

        // Intentamos registrar exactamente la misma conexión (mismo origen y mismo destino).
        // Debe dar ERROR_6 aunque cambien la distancia o el tiempo.
        retorno = s.registrarConexion("CEN01", "CEN02", 100, 90);
        assertEquals(Retorno.Resultado.ERROR_6, retorno.getResultado());
    }

}
