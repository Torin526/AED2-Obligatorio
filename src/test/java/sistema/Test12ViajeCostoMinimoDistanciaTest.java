
package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test12ViajeCostoMinimoDistanciaTest {

    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
        // Registramos varios centros logísticos para armar nuestra red de caminos
        s.registrarCentroLogistico("A", "Centro A", "MVD", "Dir A");
        s.registrarCentroLogistico("B", "Centro B", "CAN", "Dir B");
        s.registrarCentroLogistico("C", "Centro C", "MAL", "Dir C");
        s.registrarCentroLogistico("D", "Centro D", "SJO", "Dir D");
        s.registrarCentroLogistico("E", "Centro E", "COL", "Dir E");
    }


    @Test
    void viajeCostoMinimoDistanciaOkConexionDirecta() {
        // Caso límite: Existe una conexión directa que además es la única
        s.registrarConexion("A", "B", 35, 40); // 35 km

        retorno = s.viajeCostoMinimoDistancia("A", "B");

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // Debe sumar la distancia total en valorEntero
        assertEquals(35, retorno.getValorInteger());

        // El formato del string debe incluir Origen y Destino separados por "|"
        String esperado = "A;Centro A;MVD;Dir A|B;Centro B;CAN;Dir B";
        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void viajeCostoMinimoDistanciaOkSeleccionaCaminoMasCorto() {
        // Caso estándar de Dijkstra: Hay dos rutas para ir de A a D.
        // Ruta 1 (Directa larga): A -> D (Distancia = 100 km)
        s.registrarConexion("A", "D", 100, 30);

        // Ruta 2 (Indirecta más corta en km pero con más conexiones y más tiempo):
        // A -> B (20 km) -> C (30 km) -> D (15 km) = Total 65 km
        s.registrarConexion("A", "B", 20, 10);
        s.registrarConexion("B", "C", 30, 15);
        s.registrarConexion("C", "D", 15, 10);

        retorno = s.viajeCostoMinimoDistancia("A", "D");

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        // Dijkstra debe elegir la Ruta 2 porque prioriza los kilómetros mínimos (20 + 30 + 15 = 65)
        assertEquals(65, retorno.getValorInteger());

        String esperado = "A;Centro A;MVD;Dir A|" +
                "B;Centro B;CAN;Dir B|" +
                "C;Centro C;MAL;Dir C|" +
                "D;Centro D;SJO;Dir D";
        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void viajeCostoMinimoDistanciaOkMismoOrigenYDestino() {
        // Caso de borde extremo: El origen y el destino son el mismo centro logístico.
        // La distancia recorrida debería ser 0 y el string contener únicamente a ese centro.
        retorno = s.viajeCostoMinimoDistancia("A", "A");

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(0, retorno.getValorInteger());
        assertEquals("A;Centro A;MVD;Dir A", retorno.getValorString());
    }



    // =========================================================================
    // TESTS PARA ERROR 1: Parámetros vacíos o nulos
    // =========================================================================

    @Test
    void viajeCostoMinimoDistanciaError1() {
        // Casos de borde: Parámetros nulos
        retorno = s.viajeCostoMinimoDistancia(null, "B");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.viajeCostoMinimoDistancia("A", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        // Casos de borde: Parámetros vacíos o con espacios
        retorno = s.viajeCostoMinimoDistancia("", "B");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.viajeCostoMinimoDistancia("A", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 2 y 3: Inexistencia de origen o destino
    // =========================================================================

    @Test
    void viajeCostoMinimoDistanciaError2OrigenNoExiste() {
        // El destino existe pero el origen no está en el sistema
        retorno = s.viajeCostoMinimoDistancia("FANTASMA", "B");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    void viajeCostoMinimoDistanciaError3DestinoNoExiste() {
        // El origen existe pero el destino no está en el sistema
        retorno = s.viajeCostoMinimoDistancia("A", "FANTASMA");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 4: No existe ningún camino posible
    // =========================================================================

    @Test
    void viajeCostoMinimoDistanciaError4NoHayCamino() {
        // El centro E está completamente aislado del resto
        s.registrarConexion("A", "B", 50, 30);
        s.registrarConexion("B", "C", 40, 25);

        // Intentamos ir de A hacia E. Como no hay forma de llegar, debe retornar ERROR_4
        retorno = s.viajeCostoMinimoDistancia("A", "E");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }




}
