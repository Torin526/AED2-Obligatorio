package sistema;

import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test11RedCentrosPorCantidadDeConexionesTest {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);

        // Centros logísticos registrados para las pruebas
        s.registrarCentroLogistico("A", "Centro A", "MVD", "Dir A");
        s.registrarCentroLogistico("B", "Centro B", "CAN", "Dir B");
        s.registrarCentroLogistico("C", "Centro C", "MAL", "Dir C");
        s.registrarCentroLogistico("D", "Centro D", "SJO", "Dir D");
        s.registrarCentroLogistico("E", "Centro E", "COL", "Dir E");
    }

    @Test
    void redCentrosPorCantidadDeConexionesOkCeroConexiones() {
        // Caso de borde: Hasta 0 conexiones significa que no puedo moverme a ningún otro nodo.
        // Como el origen está excluido por el BFS, debe retornar un String vacío.
        retorno = s.redCentrosPorCantidadDeConexiones("A", 0);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    void redCentrosPorCantidadDeConexionesOkUnSalto() {
        // Creamos conexiones: A -> B (1 salto), A -> C (1 salto), B -> D (2 saltos)
        s.registrarConexion("A", "B", 10, 10);
        s.registrarConexion("A", "C", 15, 15);
        s.registrarConexion("B", "D", 20, 20);

        // Pedimos hasta 1 conexión. Debe listar B y C con todos sus campos.
        // Debe ignorar D porque requiere 2 saltos y excluir A (origen).
        retorno = s.redCentrosPorCantidadDeConexiones("A", 1);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // CORREGIDO: Formato completo id;nombre;departamento;direccion ordenados de forma creciente (B -> C)
        String esperado = "B;Centro B;CAN;Dir B|" +
                "C;Centro C;MAL;Dir C";
        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void redCentrosPorCantidadDeConexionesOkVariosSaltosConCiclos() {
        // Creamos un grafo con caminos alternativos y ciclos para verificar que no repita elementos:
        s.registrarConexion("A", "B", 10, 10);
        s.registrarConexion("B", "C", 10, 10);
        s.registrarConexion("A", "C", 10, 10);
        s.registrarConexion("C", "D", 10, 10);
        s.registrarConexion("D", "B", 10, 10);

        // Pedimos hasta 2 conexiones. Debe alcanzar B, C y D sin duplicados.
        retorno = s.redCentrosPorCantidadDeConexiones("A", 2);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // CORREGIDO: Formato completo ordenado alfabéticamente (B -> C -> D)
        String esperado = "B;Centro B;CAN;Dir B|" +
                "C;Centro C;MAL;Dir C|" +
                "D;Centro D;SJO;Dir D";
        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void redCentrosPorCantidadDeConexionesOkIslaInalcanzable() {
        // El centro E está registrado pero completamente aislado
        s.registrarConexion("A", "B", 10, 10);

        // Buscamos desde A con nivel alto de saltos. E nunca debe aparecer.
        retorno = s.redCentrosPorCantidadDeConexiones("A", 5);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // CORREGIDO: Formato completo y cambiado "Canelones" por el código "CAN" real del setUp
        String esperado = "B;Centro B;CAN;Dir B";
        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void redCentrosPorCantidadDeConexionesError1() {
        // CORREGIDO: El caso '0' se quitó de aquí porque es un valor válido. Solo van negativos.
        retorno = s.redCentrosPorCantidadDeConexiones("A", -1);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.redCentrosPorCantidadDeConexiones("A", -5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 2: Código vacío o null
    // =========================================================================

    @Test
    void redCentrosPorCantidadDeConexionesError2() {
        retorno = s.redCentrosPorCantidadDeConexiones(null, 2);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.redCentrosPorCantidadDeConexiones("", 2);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.redCentrosPorCantidadDeConexiones("   ", 2);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 3: Centro de origen no registrado
    // =========================================================================

    @Test
    void redCentrosPorCantidadDeConexionesError3() {
        retorno = s.redCentrosPorCantidadDeConexiones("FANTASMA", 2);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
}