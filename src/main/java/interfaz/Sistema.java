package interfaz;

/**
 * Provee una interfaz para interactuar con el sistema
 */
public interface Sistema {

    Retorno inicializarSistema(int maxCentros);
    Retorno registrarMercaderia(String id, String codigo, String descripcion, boolean fragil, Categoria categoria);

    Retorno buscarMercaderiaPorId(String id);
    Retorno listarMercaderiasPorIdAscendente();
    Retorno listarMercaderiasPorIdDescendente();

    Retorno buscarMercaderiaPorCodigo(String codigo);
    Retorno listarMercaderiasPorCodigoAscendente();

    Retorno listarMercaderiasPorCategoria(Categoria unaCategoria);

    Retorno registrarCentroLogistico(String codigo, String nombre, String departamento, String direccion);
    Retorno registrarConexion(String codigoOrigen, String codigoDestino, int distancia, int tiempo);
    Retorno redCentrosPorCantidadDeConexiones(String codigoOrigen, int cantidad);

    Retorno viajeCostoMinimoDistancia(String codigoOrigen, String codigoDestino);
    Retorno viajeCostoMinimoTiempo(String codigoOrigen, String codigoDestino);



}
