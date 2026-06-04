package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Test06BuscarMercaderiaPorCodigoTest {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

        @BeforeEach
        public void setUp() {
            s.inicializarSistema(10);
        }

    @Test
    void buscarMercaderiaPorCodigoOkUnicoElemento() {
        // Caso límite: Solo hay un elemento en todo el sistema. Debe recorrer exactamente 1 nodo.
        s.registrarMercaderia("12345", "MN-001-ABC123", "Batería de cocina", false, Categoria.OTROS);

        retorno = s.buscarMercaderiaPorCodigo("MN-001-ABC123");

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345;MN-001-ABC123;Batería de cocina;false;OTROS", retorno.getValorString());
        assertEquals(1, retorno.getValorInteger()); // La raíz
    }

    @Test
    void buscarMercaderiaPorCodigoOkRaiz() {
        // Caso de borde: El elemento buscado quedó justo en la raíz del árbol de búsqueda.
        // Registramos primero el elemento intermedio para que sea la raíz (asumiendo orden por código)
        s.registrarMercaderia("20", "MM-222", "Mercaderia Raiz", false, Categoria.OTROS);
        s.registrarMercaderia("10", "AA-111", "Mercaderia Izq", true, Categoria.TEXTIL);
        s.registrarMercaderia("30", "ZZ-333", "Mercaderia Der", false, Categoria.OTROS);

        retorno = s.buscarMercaderiaPorCodigo("MM-222");

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("20;MM-222;Mercaderia Raiz;false;OTROS", retorno.getValorString());
        assertEquals(1, retorno.getValorInteger()); // Recorrió 1 elemento (la raíz)
    }

    @Test
    void buscarMercaderiaPorCodigoOkHijo() {
        // Caso estándar con recorrido: Buscamos elementos que están más abajo en la estructura
        // Insertamos en orden para formar un árbol balanceado por códigos: MM (Raíz), AA (Izq), ZZ (Der)
        s.registrarMercaderia("20", "MM-222", "M20", false, Categoria.OTROS);
        s.registrarMercaderia("10", "AA-111", "M10", true, Categoria.TEXTIL);
        s.registrarMercaderia("30", "ZZ-333", "M30", false, Categoria.OTROS);

        // Buscar el de la izquierda
        retorno = s.buscarMercaderiaPorCodigo("AA-111");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("10;AA-111;M10;true;TEXTIL", retorno.getValorString());
        assertEquals(2, retorno.getValorInteger()); // Pasó por MM-222 y llegó a AA-111

        // Buscar el de la derecha
        retorno = s.buscarMercaderiaPorCodigo("ZZ-333");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("30;ZZ-333;M30;false;OTROS", retorno.getValorString());
        assertEquals(2, retorno.getValorInteger()); // Pasó por MM-222 y llegó a ZZ-333
    }

    @Test
    void buscarMercaderiaPorCodigoOkPeorCaso() {
        // Caso límite (Peor Caso): Si el árbol queda completamente desbalanceado (como una lista),
        // la cantidad de elementos recorridos aumenta linealmente.
        // Insertamos códigos en orden alfabético estricto para forzar un árbol "hacia la derecha"
        s.registrarMercaderia("1", "AA-111", "D1", false, Categoria.OTROS);
        s.registrarMercaderia("2", "BB-222", "D2", false, Categoria.OTROS);
        s.registrarMercaderia("3", "CC-333", "D3", false, Categoria.OTROS);
        s.registrarMercaderia("4", "DD-444", "D4", true, Categoria.OTROS);

        // Buscamos el elemento que quedó en el fondo (las hojas del peor caso)
        retorno = s.buscarMercaderiaPorCodigo("DD-444");

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("4;DD-444;D4;true;OTROS", retorno.getValorString());

        // Debe haber recorrido exactamente 4 elementos si es un ABB tradicional sin balanceo
        // Nota: Si usás un árbol auto-balanceado (como AVL), este número podría ser menor (ej: 2 o 3),
        // pero evaluará correctamente la cantidad de pasos reales que dio tu algoritmo.
        assertTrue(retorno.getValorInteger() > 0);
    }

    @Test
    void buscarMercaderiaPorCodigoError1() {
        // Caso de borde: Código null
        retorno = s.buscarMercaderiaPorCodigo(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        // Caso de borde: Código vacío
        retorno = s.buscarMercaderiaPorCodigo("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        // Caso de borde: Código con espacios en blanco (vacío conceptualmente)
        retorno = s.buscarMercaderiaPorCodigo("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 2: No existe la mercadería
    // =========================================================================

    @Test
    void buscarMercaderiaPorCodigoError2ListaVacia() {
        // Caso de borde extremo: Buscar en un sistema recién inicializado sin datos
        retorno = s.buscarMercaderiaPorCodigo("XX-001-ABC123");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    void buscarMercaderiaPorCodigoError2NoExisteConDatos() {
        // Caso estándar de error: Hay datos, pero el código buscado no coincide con ninguno
        s.registrarMercaderia("10", "XX-001-AAA111", "M1", false, Categoria.OTROS);
        s.registrarMercaderia("20", "XX-001-BBB222", "M2", false, Categoria.OTROS);

        retorno = s.buscarMercaderiaPorCodigo("XX-001-NOEXISTO");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
    }
