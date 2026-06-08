
package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test09EnAdelanteORT {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    private void crearCentros() {
        s.registrarCentroLogistico("1", "Centro1", "Depto1", "Dire1");
        s.registrarCentroLogistico("2", "Centro2", "Depto2", "Dire2");
        s.registrarCentroLogistico("3", "Centro3", "Depto3", "Dire3");
        s.registrarCentroLogistico("4", "Centro4", "Depto4", "Dire4");
        s.registrarCentroLogistico("5", "Centro5", "Depto5", "Dire5");
    }

    @Test
    void verificacionInicial() {
        retorno = s.registrarCentroLogistico("1", "Centro1", "Depto1", "Dire1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarCentroLogistico("2", "Centro2", "Depto2", "Dire2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        retorno = s.registrarConexion("1", "2", 5, 10);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void redCentrosPorCantidadDeConexionesSinConexiones() {
        crearCentros();
        retorno = s.redCentrosPorCantidadDeConexiones("1",0);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("1;Centro1;Depto1;Dire1", retorno.getValorString());
        retorno = s.redCentrosPorCantidadDeConexiones("1",3);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("1;Centro1;Depto1;Dire1", retorno.getValorString());
    }

    @Test
    void redCentrosPorCantidadDeConexionesLinealOrdenado() {
        crearCentros();
        s.registrarConexion("1", "2", 1, 1);
        s.registrarConexion("2", "3", 1, 1);
        s.registrarConexion("3", "4", 1, 1);
        retorno = s.redCentrosPorCantidadDeConexiones("1",0);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("1;Centro1;Depto1;Dire1", retorno.getValorString());
        retorno = s.redCentrosPorCantidadDeConexiones("1",1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("1;Centro1;Depto1;Dire1|2;Centro2;Depto2;Dire2", retorno.getValorString());
        retorno = s.redCentrosPorCantidadDeConexiones("1",2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("1;Centro1;Depto1;Dire1|2;Centro2;Depto2;Dire2|3;Centro3;Depto3;Dire3", retorno.getValorString());

        retorno = s.redCentrosPorCantidadDeConexiones("3",2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("3;Centro3;Depto3;Dire3|4;Centro4;Depto4;Dire4", retorno.getValorString());

        s.registrarConexion("3", "2", 1, 1);
        s.registrarConexion("2", "1", 1, 1);
        retorno = s.redCentrosPorCantidadDeConexiones("3",2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("1;Centro1;Depto1;Dire1|2;Centro2;Depto2;Dire2|3;Centro3;Depto3;Dire3|4;Centro4;Depto4;Dire4", retorno.getValorString());
    }



    @Test
    void viajeCostoMinimoSinCaminoError4() {
        crearCentros();
        retorno = s.viajeCostoMinimoDistancia("1","2");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());

        retorno = s.viajeCostoMinimoTiempo("1","2");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());

        s.registrarConexion("2", "1", 3, 5);

        retorno = s.viajeCostoMinimoDistancia("1","2");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());

        retorno = s.viajeCostoMinimoTiempo("1","2");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }

    @Test
    void viajeCostoMinimoDirectoArista() {
        crearCentros();
        s.registrarConexion("1", "2", 3, 5);

        retorno = s.viajeCostoMinimoDistancia("1","2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(3, retorno.getValorInteger());
        assertEquals("1;Centro1;Depto1;Dire1|2;Centro2;Depto2;Dire2", retorno.getValorString());

        retorno = s.viajeCostoMinimoTiempo("1","2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(5, retorno.getValorInteger());
        assertEquals("1;Centro1;Depto1;Dire1|2;Centro2;Depto2;Dire2", retorno.getValorString());
    }

    @Test
    void viajeCostoMinimoSeleccionarCostoMinimo() {
        crearCentros();
        s.registrarConexion("1", "2", 1, 2);
        s.registrarConexion("2", "3", 3, 5);
        s.registrarConexion("1", "3", 10, 10);

        retorno = s.viajeCostoMinimoDistancia("1","3");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(4, retorno.getValorInteger());
        assertEquals("1;Centro1;Depto1;Dire1|2;Centro2;Depto2;Dire2|3;Centro3;Depto3;Dire3", retorno.getValorString());

        retorno = s.viajeCostoMinimoTiempo("1","3");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(7, retorno.getValorInteger());
        assertEquals("1;Centro1;Depto1;Dire1|2;Centro2;Depto2;Dire2|3;Centro3;Depto3;Dire3", retorno.getValorString());
    }

    @Test
    void viajeCostoMinimoSinAristaDirecta() {
        crearCentros();
        s.registrarConexion("1", "2", 1, 2);
        s.registrarConexion("2", "3", 3, 5);

        retorno = s.viajeCostoMinimoDistancia("1","3");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(4, retorno.getValorInteger());
        assertEquals("1;Centro1;Depto1;Dire1|2;Centro2;Depto2;Dire2|3;Centro3;Depto3;Dire3", retorno.getValorString());

        retorno = s.viajeCostoMinimoTiempo("1","3");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(7, retorno.getValorInteger());
        assertEquals("1;Centro1;Depto1;Dire1|2;Centro2;Depto2;Dire2|3;Centro3;Depto3;Dire3", retorno.getValorString());
    }


}
