
package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test13ViajeCostoMinimoTiempo {

    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
        // Registramos los centros logísticos para armar la red de caminos
        s.registrarCentroLogistico("A", "Centro A", "MVD", "Dir A");
        s.registrarCentroLogistico("B", "Centro B", "CAN", "Dir B");
        s.registrarCentroLogistico("C", "Centro C", "MAL", "Dir C");
        s.registrarCentroLogistico("D", "Centro D", "SJO", "Dir D");
        s.registrarCentroLogistico("E", "Centro E", "COL", "Dir E");
    }


    @Test
    void viajeCostoMinimoTiempoOkConexionDirecta() {
        // Caso límite: Existe una única conexión directa entre ambos puntos
        s.registrarConexion("A", "B", 100, 45); // 45 minutos

        retorno = s.viajeCostoMinimoTiempo("A", "B");

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // Debe retornar la suma de los minutos en valorEntero
        assertEquals(45, retorno.getValorEntero());

        // Formato esperado de Origen y Destino separados por "|"
        String esperado = "A;Centro A;MVD;Dir A|B;Centro B;CAN;Dir B";
        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void viajeCostoMinimoTiempoOkSeleccionaCaminoMasRapido() {
        // Caso estándar de Dijkstra por Tiempo: Hay dos rutas para ir de A a D.
        // Ruta 1 (Directa pero lenta en tiempo): A -> D (Distancia = 30 km, Tiempo = 80 min)
        s.registrarConexion("A", "D", 30, 80);

        // Ruta 2 (Más larga en kilómetros, pero por autopista va mucho más rápido):
        // A -> B (10 min) -> C (15 min) -> D (20 min) = Total 45 minutos
        s.registrarConexion("A", "B", 50, 10);
        s.registrarConexion("B", "C", 60, 15);
        s.registrarConexion("C", "D", 40, 20);

        retorno = s.viajeCostoMinimoTiempo("A", "D");

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // Dijkstra debe priorizar el tiempo mínimo (10 + 15 + 20 = 45) a pesar de tener más conexiones y km
        assertEquals(45, retorno.getValorInteger());

        String esperado = "A;Centro A;MVD;Dir A|" +
                "B;Centro B;CAN;Dir B|" +
                "C;Centro C;MAL;Dir C|" +
                "D;Centro D;SJO;Dir D";
        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void viajeCostoMinimoTiempoOkMismoOrigenYDestino() {
        // Caso de borde extremo: El origen y el destino coinciden.
        // El tiempo invertido debe ser 0 y el string contener solo los datos de ese centro.
        retorno = s.viajeCostoMinimoTiempo("A", "A");

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(0, retorno.getValorInteger());
        assertEquals("A;Centro A;MVD;Dir A", retorno.getValorString());
    }



    @Test
    void viajeCostoMinimoTiempoError1() {
        // Casos de borde: Parámetros nulos
        retorno = s.viajeCostoMinimoTiempo(null, "B");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.viajeCostoMinimoTiempo("A", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        // Casos de borde: Parámetros vacíos o con espacios
        retorno = s.viajeCostoMinimoTiempo("", "B");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.viajeCostoMinimoTiempo("A", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 2 y 3: Inexistencia de origen o destino
    // =========================================================================

    @Test
    void viajeCostoMinimoTiempoError2OrigenNoExiste() {
        // El destino existe pero el origen no está registrado en el sistema
        retorno = s.viajeCostoMinimoTiempo("FANTASMA", "B");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    void viajeCostoMinimoTiempoError3DestinoNoExiste() {
        // El origen existe pero el destino no está registrado en el sistema
        retorno = s.viajeCostoMinimoTiempo("A", "FANTASMA");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 4: No existe ningún camino posible
    // =========================================================================

    @Test
    void viajeCostoMinimoTiempoError4NoHayCamino() {
        // El centro E está completamente desconectado de los demás
        s.registrarConexion("A", "B", 30, 20);
        s.registrarConexion("B", "C", 25, 15);

        // Intentamos ir de A hacia E. Al no existir ruta, debe retornar ERROR_4
        retorno = s.viajeCostoMinimoTiempo("A", "E");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }

}
