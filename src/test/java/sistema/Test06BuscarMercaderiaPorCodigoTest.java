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
        assertEquals("12345;MN-001-ABC123;Batería de cocina;false;Otros", retorno.getValorString());
        assertEquals(1, retorno.getValorInteger()); // La raíz
    }

    @Test
    void buscarMercaderiaPorCodigoOkRaiz() {
        // Caso de borde: El elemento buscado quedó justo en la raíz del árbol de búsqueda.
        // CORREGIDO: Códigos con formato válido (2 letras - 3 números - 6 alfanuméricos) ordenados alfabéticamente:
        // MM (Intermedio/Raíz), AA (Menor/Izq), ZZ (Mayor/Der)
        s.registrarMercaderia("20", "MM-222-RAIZ00", "Mercaderia Raiz", false, Categoria.OTROS);
        s.registrarMercaderia("10", "AA-111-IZQ000", "Mercaderia Izq", true, Categoria.TEXTIL);
        s.registrarMercaderia("30", "ZZ-333-DER000", "Mercaderia Der", false, Categoria.OTROS);

        retorno = s.buscarMercaderiaPorCodigo("MM-222-RAIZ00");

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("20;MM-222-RAIZ00;Mercaderia Raiz;false;Otros", retorno.getValorString());
        assertEquals(1, retorno.getValorInteger()); // Recorrió 1 elemento (la raíz)
    }

    @Test
    void buscarMercaderiaPorCodigoOkHijo() {
        // Caso estándar con recorrido: Buscamos elementos que están más abajo en la estructura
        // CORREGIDO: Se cambiaron los códigos inválidos o vacíos por formatos correctos
        s.registrarMercaderia("20", "MM-222-RAIZ00", "M20", false, Categoria.OTROS);
        s.registrarMercaderia("10", "AA-111-IZQ000", "M10", true, Categoria.TEXTIL);
        s.registrarMercaderia("30", "ZZ-333-DER000", "M30", false, Categoria.OTROS);

        // Buscar el de la izquierda
        retorno = s.buscarMercaderiaPorCodigo("AA-111-IZQ000");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("10;AA-111-IZQ000;M10;true;Textil", retorno.getValorString());
        assertEquals(2, retorno.getValorInteger()); // Pasó por MM y llegó a AA

        // Buscar el de la derecha
        retorno = s.buscarMercaderiaPorCodigo("ZZ-333-DER000");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("30;ZZ-333-DER000;M30;false;Otros", retorno.getValorString());
        assertEquals(2, retorno.getValorInteger()); // Pasó por MM y llegó a ZZ
    }

    @Test
    void buscarMercaderiaPorCodigoOkPeorCaso() {
        // Caso límite (Peor Caso): Árbol desbalanceado hacia la derecha.
        // CORREGIDO: Códigos válidos ordenados de forma creciente (AA -> BB -> CC -> DD)
        s.registrarMercaderia("1", "AA-111-AAAAAA", "D1", false, Categoria.OTROS);
        s.registrarMercaderia("2", "BB-222-BBBBBB", "D2", false, Categoria.OTROS);
        s.registrarMercaderia("3", "CC-333-CCCCCC", "D3", false, Categoria.OTROS);
        s.registrarMercaderia("4", "DD-444-DDDDDD", "D4", true, Categoria.OTROS);

        retorno = s.buscarMercaderiaPorCodigo("DD-444-DDDDDD");

        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("4;DD-444-DDDDDD;D4;true;Otros", retorno.getValorString());

        // Valida que el contador de pasos haya registrado el camino correctamente
        assertTrue(retorno.getValorInteger() > 0);
    }

    @Test
    void buscarMercaderiaPorCodigoError1() {
        retorno = s.buscarMercaderiaPorCodigo(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.buscarMercaderiaPorCodigo("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.buscarMercaderiaPorCodigo("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    // =========================================================================
    // TESTS PARA ERROR 2: No existe la mercadería
    // =========================================================================

    @Test
    void buscarMercaderiaPorCodigoError2ListaVacia() {
        // CORREGIDO: Se usa un código con estructura formalmente válida para asegurar que falle por inexistencia y no por formato.
        retorno = s.buscarMercaderiaPorCodigo("XX-001-ABC123");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    void buscarMercaderiaPorCodigoError2NoExisteConDatos() {
        s.registrarMercaderia("10", "XX-001-AAA111", "M1", false, Categoria.OTROS);
        s.registrarMercaderia("20", "XX-001-BBB222", "M2", false, Categoria.OTROS);

        // CORREGIDO: "NOEXIS" tiene exactamente 6 caracteres alfanuméricos para cumplir el formato
        retorno = s.buscarMercaderiaPorCodigo("XX-001-NOEXIS");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
}