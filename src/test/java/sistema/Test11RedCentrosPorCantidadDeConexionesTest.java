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
        // CORREGIDO: Ahora incluye de forma obligatoria al centro de origen "A" (nivel 0).
        retorno = s.redCentrosPorCantidadDeConexiones("A", 0);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("A;Centro A;MVD;Dir A", retorno.getValorString());
    }

    @Test
    void redCentrosPorCantidadDeConexionesOkUnSalto() {
        // Creamos conexiones: A -> B (1 salto), A -> C (1 salto), B -> D (2 saltos)
        s.registrarConexion("A", "B", 10, 10);
        s.registrarConexion("A", "C", 15, 15);
        s.registrarConexion("B", "D", 20, 20);

        // Pedimos hasta 1 conexión. Debe listar B y C con todos sus campos e INCLUIR A (origen).
        // Debe ignorar D porque requiere 2 saltos.
        retorno = s.redCentrosPorCantidadDeConexiones("A", 1);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // CORREGIDO: Formato completo ordenado de forma creciente (A -> B -> C)
        String esperado = "A;Centro A;MVD;Dir A|" +
                "B;Centro B;CAN;Dir B|" +
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

        // Pedimos hasta 2 conexiones. Debe alcanzar el origen A, B, C y D sin duplicados.
        retorno = s.redCentrosPorCantidadDeConexiones("A", 2);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // CORREGIDO: Formato completo ordenado alfabéticamente (A -> B -> C -> D)
        String esperado = "A;Centro A;MVD;Dir A|" +
                "B;Centro B;CAN;Dir B|" +
                "C;Centro C;MAL;Dir C|" +
                "D;Centro D;SJO;Dir D";
        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void redCentrosPorCantidadDeConexionesOkIslaInalcanzable() {
        // El centro E está registrado pero completamente aislado
        s.registrarConexion("A", "B", 10, 10);

        // Buscamos desde A con nivel alto de saltos. E nunca debe aparecer, pero A y B sí.
        retorno = s.redCentrosPorCantidadDeConexiones("A", 5);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

}
        // CORREGIDO: Formato completo ordenado que incluye al origen (A -> B)