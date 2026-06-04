
package sistema;

import interfaz.Categoria;
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

        s.registrarCentroLogistico("A", "Centro A", "MVD", "Dir A");
        s.registrarCentroLogistico("B", "Centro B", "CAN", "Dir B");
        s.registrarCentroLogistico("C", "Centro C", "MAL", "Dir C");
        s.registrarCentroLogistico("D", "Centro D", "SJO", "Dir D");
        s.registrarCentroLogistico("E", "Centro E", "COL", "Dir E");
    }
    @Test
    void redCentrosPorCantidadDeConexionesOkCeroConexiones() {
        // Caso de borde límite: Hasta 0 conexiones significa que solo "llego" a mí mismo.
        // La letra suele excluir al propio origen o incluirlo según la interpretación,
        // pero si tu implementación o la letra tradicional de la FiNG no se incluye a sí misma, debería dar vacío "".
        // Si se incluye a sí mismo, adaptá el string esperado con los datos de "A".
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

        // Pedimos hasta 1 conexión. Debe listar B y C. Debe ignorar D porque requiere 2 saltos.
        retorno = s.redCentrosPorCantidadDeConexiones("A", 1);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Ordenados de forma creciente: B -> C
        String esperado = "B;Centro B;Canelones;Dir B|" +
                "C;Centro C;Maldonado;Dir C";
        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void redCentrosPorCantidadDeConexionesOkVariosSaltosConCiclos() {
        // Creamos un grafo con caminos alternativos y ciclos para verificar que no repita elementos:
        // A -> B (1 salto)
        // B -> C (2 saltos desde A)
        // A -> C (1 salto desde A - camino más corto)
        // C -> D (2 saltos desde A)
        // D -> B (Ciclo hacia atrás)
        s.registrarConexion("A", "B", 10, 10);
        s.registrarConexion("B", "C", 10, 10);
        s.registrarConexion("A", "C", 10, 10);
        s.registrarConexion("C", "D", 10, 10);
        s.registrarConexion("D", "B", 10, 10);

        // Pedimos hasta 2 conexiones. Debe alcanzar B, C y D.
        // No debe haber duplicados en el String final a pesar del ciclo y del doble camino hacia C.
        retorno = s.redCentrosPorCantidadDeConexiones("A", 2);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Alfabéticamente ordenados y sin repetidos: B -> C -> D
        String esperado = "B;Centro B;Canelones;Dir B|" +
                "C;Centro C;Maldonado;Dir C|" +
                "D;Centro D;San José;Dir D"; // Asumiendo mapeo de SJO o el nombre que le diste
        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void redCentrosPorCantidadDeConexionesOkIslaInalcanzable() {
        // El centro E está registrado pero completamente aislado (no tiene aristas de entrada)
        s.registrarConexion("A", "B", 10, 10);

        // Buscamos desde A con nivel alto de saltos. E nunca debe aparecer.
        retorno = s.redCentrosPorCantidadDeConexiones("A", 5);

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        String esperado = "B;Centro B;Canelones;Dir B";
        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void redCentrosPorCantidadDeConexionesError1() {
        // Caso de borde: Cantidad negativa
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
        // Caso de borde: Código null
        retorno = s.redCentrosPorCantidadDeConexiones(null, 2);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        // Caso de borde: Código vacío
        retorno = s.redCentrosPorCantidadDeConexiones("", 2);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        // Caso de borde: Código con espacios
        retorno = s.redCentrosPorCantidadDeConexiones("   ", 2);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 3: Centro de origen no registrado
    // =========================================================================

    @Test
    void redCentrosPorCantidadDeConexionesError3() {
        // Caso de borde: Los parámetros son válidos, pero el centro no existe en el sistema
        retorno = s.redCentrosPorCantidadDeConexiones("FANTASMA", 2);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }


}
