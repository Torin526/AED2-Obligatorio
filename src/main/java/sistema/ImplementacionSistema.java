package sistema;

import dominio.Conexion;
import dominio.Mercaderia;
import dominio.WMercaderiaPorCodigo;
import estructuras.Lista.Nodo;
import interfaz.Sistema;
import interfaz.Retorno;
import interfaz.Categoria;
import dominio.CentroLogistico;
import estructuras.Grafo.Grafo;
import estructuras.ABB.ABB;
import estructuras.ABB.NodoGen;
import estructuras.Lista.ListaImp;

public class ImplementacionSistema implements Sistema {


    private Grafo grafoConCentros;
    private ABB<Mercaderia> arbolGralMercaderia;
    private ABB<WMercaderiaPorCodigo> arbolGralMercaderiaPorCodigo;
    private ListaImp<ABB<Mercaderia>> arbolesPorCategoria;
    private CentroLogistico[] vectorCentros;


    @Override
    public Retorno inicializarSistema(int maxCentros) {
        if (maxCentros <= 3) {
            return Retorno.error1("El máximo de centros debe ser mayor a 3");
        }


        this.grafoConCentros = new Grafo(maxCentros, true);
        this.grafoConCentros.setTope(maxCentros);
        this.grafoConCentros.setCantActual(0);
        vectorCentros = new CentroLogistico[maxCentros];
        arbolGralMercaderia = new ABB<Mercaderia>();
        arbolGralMercaderiaPorCodigo = new ABB<WMercaderiaPorCodigo>();
        crearArbolesEnCadaCategoria(arbolesPorCategoria);
        return Retorno.ok("El sistema pudo ser inicializado correctamente");

    }


    @Override
    public Retorno registrarMercaderia(String id, String codigo, String descripcion, boolean fragil,
                                       Categoria categoria) {
        if (id == null || id.isBlank() || codigo == null || codigo.isBlank() || descripcion == null || descripcion.isBlank() || categoria == null) {
            return Retorno.error1("No puede haber campos vacíos o en null");
        }
        if (!(formatoValidoCodigo(codigo))) {
            return Retorno.error2("Debe cumplir con el formato estipulado");
        }
        //Debe ser hecho de esta manera porque devuelve un objeto de tipo retorno. De ese objeto usamos el metodo isOk()
        Mercaderia mercaderiaParaChequeoXId = new Mercaderia(id, null, null, false, null);
        if (arbolGralMercaderia.pertenece(mercaderiaParaChequeoXId)) {
            return Retorno.error3("Ya existe una mercadería con ese Id");
            //||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
            //vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
            // VER SI ESTÁ BIEN ESTA IMPLEMENTACIÓN!!!!!!!!!!!!!!!!!!!!!
        }
        //Debe ser hecho de esta manera porque devuelve un objeto de tipo retorno. De ese objeto usamos el metodo isOk()
        Mercaderia mercaderiaParaChequeoXCP = new Mercaderia(null, codigo, null, false, null);
        WMercaderiaPorCodigo wrapperFantasma = new WMercaderiaPorCodigo(mercaderiaParaChequeoXCP);
        if (arbolGralMercaderiaPorCodigo.pertenece(wrapperFantasma)) {
            return Retorno.error3("Ya existe una mercadería con ese Id");
            //||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
            //vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
            // VER SI ESTÁ BIEN ESTA IMPLEMENTACIÓN!!!!!!!!!!!!!!!!!!!!!
        }
        Mercaderia mer = new Mercaderia(id, codigo, descripcion, fragil, categoria);
        arbolGralMercaderia.insertar(mer);
        WMercaderiaPorCodigo wMer = new WMercaderiaPorCodigo(mer);
        arbolGralMercaderiaPorCodigo.insertar(wMer);

        // recuperar. busca el NODO de la lista en la posición que indica el índice de la categoría.
        // getDato() abre ese nodo para extraer el ÁRBOL (ABB) de mercaderías que guardamos adentro.
        // insertar(mer) mete la mercadería en ese árbol específico, ordenándola por su ID de forma eficiente.
        arbolesPorCategoria.recuperar(mer.getCategoria().getIndice()).getDato().insertar(mer);

        return Retorno.ok("Mercadería ingresada correctamente");
    }


    @Override
    public Retorno buscarMercaderiaPorId(String id) {

        if (id == null || id.isBlank()) {
            return Retorno.error1("No puede haber campos vacíos o en null");
        }

        Mercaderia mercaderiaParaChequeo = new Mercaderia(id, null, null, false, null);
        Mercaderia mercaderiaAMostrar = obtenerMercaderíaPorId(mercaderiaParaChequeo, arbolGralMercaderia.getRaiz());
        if (mercaderiaAMostrar == null) {
            return Retorno.error2("No existe una mercadería con ese Id");

        }


        return Retorno.ok(mercaderiaAMostrar.getId() + ";" + mercaderiaAMostrar.getCodigoPostal() + ";" + mercaderiaAMostrar.getDescripcion()
                + ";" + mercaderiaAMostrar.isFragil() + ";" + mercaderiaAMostrar.getCategoria().getTexto());

    }

    @Override
    public Retorno listarMercaderiasPorIdAscendente() {

        ListaImp<Mercaderia> listaMer = new ListaImp<Mercaderia>();
        cargarListaAsc(listaMer, arbolGralMercaderia.getRaiz());
        String salida = "";
        Nodo<Mercaderia> actual = listaMer.getInicio();

        while (actual != null) {
            salida += actual.getDato().getId() + ";" + actual.getDato().getCodigoPostal() + ";" + actual.getDato().getDescripcion() + ";" + actual.getDato().isFragil() + ";" + actual.getDato().getCategoria().getTexto() + "|";
            actual = actual.getSig();
        }
        //Hacemos así para que no se rompa si no habían Mercaderías.
        if (salida.length() > 0) {
            salida = salida.substring(0, salida.length() - 1);
        }

        return Retorno.ok(salida);
    }


    @Override
    public Retorno listarMercaderiasPorIdDescendente() {

        ListaImp<Mercaderia> listaMer = new ListaImp<Mercaderia>();
        cargarListaDesc(listaMer, arbolGralMercaderia.getRaiz());
        String salida = "";
        Nodo<Mercaderia> actual = listaMer.getInicio();

        while (actual != null) {
            salida += actual.getDato().getId() + ";" + actual.getDato().getCodigoPostal() + ";" + actual.getDato().getDescripcion() + ";" + actual.getDato().isFragil() + ";" + actual.getDato().getCategoria().getTexto() + "|";
            actual = actual.getSig();
        }
        //Hacemos así para que no se rompa si no habían Mercaderías.
        if (salida.length() > 0) {
            salida = salida.substring(0, salida.length() - 1);
        }

        return Retorno.ok(salida);
    }

    @Override
    public Retorno buscarMercaderiaPorCodigo(String codigo) {

        if (codigo == null || codigo.isBlank()) {
            return Retorno.error1("No puede haber campos vacíos o en null");
        }
        Mercaderia mercaderiaParaChequeo = new Mercaderia(null, codigo, null, false, null);
        WMercaderiaPorCodigo wMercaderiaParaChequeo = new WMercaderiaPorCodigo(mercaderiaParaChequeo);
        Mercaderia mercaderiaAMostrar = obtenerMercaderíaPorCodigo(wMercaderiaParaChequeo, arbolGralMercaderiaPorCodigo.getRaiz());
        if (mercaderiaAMostrar == null) {
            return Retorno.error2("No existe una mercadería con ese código");

        }


        return Retorno.ok(mercaderiaAMostrar.getId() + ";" + mercaderiaAMostrar.getCodigoPostal() + ";" + mercaderiaAMostrar.getDescripcion()
                + ";" + mercaderiaAMostrar.isFragil() + ";" + mercaderiaAMostrar.getCategoria().getTexto());

    }


    @Override
    public Retorno listarMercaderiasPorCodigoAscendente() {

        ListaImp<WMercaderiaPorCodigo> listaWMer = new ListaImp<WMercaderiaPorCodigo>();
        cargarListaAsc(listaWMer, arbolGralMercaderiaPorCodigo.getRaiz());
        String salida = "";
        Nodo<WMercaderiaPorCodigo> actual = listaWMer.getInicio();

        while (actual != null) {
            salida += actual.getDato().getMercaderia().getId() + ";" + actual.getDato().getMercaderia().getCodigoPostal() + ";" +
                    actual.getDato().getMercaderia().getDescripcion() + ";" + actual.getDato().getMercaderia().isFragil() + ";" +
                    actual.getDato().getMercaderia().getCategoria().getTexto() + "|";
            actual = actual.getSig();
        }
        //Hacemos así para que no se rompa si no habían Mercaderías.
        if (salida.length() > 0) {
            salida = salida.substring(0, salida.length() - 1);
        }

        return Retorno.ok(salida);
    }

    @Override
    public Retorno listarMercaderiasPorCategoria(Categoria unaCategoria) {


        ABB<Mercaderia> arbolDeLaCategoría=arbolesPorCategoria.recuperar(unaCategoria.getIndice()).getDato();
        ListaImp<Mercaderia> arbolCatALista=arbolDeLaCategoría.obtenerAsc();
        //Chequeamos que no sea vacia para no tener problemas dentro del while y que se rompa cuando trabajamos con subString
        if (arbolCatALista.esVacia()) {
            return Retorno.ok("");
        }
        Nodo<Mercaderia> actual = arbolCatALista.getInicio();
        String ret="";

        while (actual != null) {
            Mercaderia m = actual.getDato();
            ret += m.getId() + ";" + m.getCodigoPostal() + ";" + m.getDescripcion() + ";" +m.isFragil() + ";" + m.getCategoria() + "|";
            actual.getSig();
        }
        ret=ret.substring(0, ret.length() - 1);


        return Retorno.ok(ret);

    }


    @Override
    public Retorno registrarCentroLogistico(String codigo, String nombre, String departamento, String direccion) {
        if (grafoConCentros.getCantActual()>= grafoConCentros.getTope()) {
            return Retorno.error1("Se han alcanzado el numero máximo de centros");
        }
        if (codigo == null || codigo.isBlank() || nombre == null || nombre.isBlank() || departamento == null || departamento.isBlank() || direccion == null|| direccion.isBlank()) {
            return Retorno.error2("No puede haber campos vacíos o en null");
        }
        //Debe ser hecho de esta manera porque devuelve un objeto de tipo retorno. De ese objeto usamos el metodo isOk()
        CentroLogistico centroAAgregar=new CentroLogistico(codigo,  nombre,  departamento,  direccion);
        if (grafoConCentros.existeVertice(centroAAgregar)) {
            return Retorno.error3("Ya existe una centro Logísitico con ese código");

        }


        grafoConCentros.agregarVertice(centroAAgregar);
        return Retorno.ok();
    }



    @Override
        public Retorno registrarConexion (String codigoOrigen, String codigoDestino,int distancia, int tiempo){
        if (codigoOrigen == null || codigoOrigen.isBlank() || codigoDestino == null || codigoDestino.isBlank()) {
            return Retorno.error1("No puede haber campos vacíos o en null");
        }

        CentroLogistico centroOrigenFantasma= new CentroLogistico(codigoOrigen, null, null, null);
        if (!(grafoConCentros.existeVertice(centroOrigenFantasma))) {
            return Retorno.error2("El centro de origeno no existe.");
        }
        //Debe ser hecho de esta manera porque devuelve un objeto de tipo retorno. De ese objeto usamos el metodo isOk()
        CentroLogistico centroDestinoFantasma= new CentroLogistico(codigoDestino, null, null, null);
        if (!(grafoConCentros.existeVertice(centroDestinoFantasma))) {
            return Retorno.error3("El centro de destino no existe.");
        }

        if (distancia<=0) {
            return Retorno.error4("La distancia debe ser mayor a 0.");
        }

        if (tiempo<=0) {
            return Retorno.error5("El tiempo debe ser mayor a 0.");
        }

        if (grafoConCentros.sonAdyacentes(centroOrigenFantasma,centroDestinoFantasma)) {
            return Retorno.error6("La conexión ya existe en el grafo");
        }
        Conexion conexion=new Conexion(codigoOrigen,  codigoDestino,  distancia,  tiempo);
        grafoConCentros.agregarArista(centroOrigenFantasma, centroDestinoFantasma, conexion);

        return Retorno.ok();
        }







        @Override
        public Retorno redCentrosPorCantidadDeConexiones (String codigoOrigen,int cantidad){

            if (cantidad<0) {
                return Retorno.error1("ingrese una cantidad mayor a cero");
            }


            if (codigoOrigen.isBlank()||codigoOrigen==null) {
                return Retorno.error2("Debe introducir un codigo de origen.");
            }

            CentroLogistico centroLogFantasma= new CentroLogistico(codigoOrigen, null, null, null);
            if (!(grafoConCentros.existeVertice(centroLogFantasma))) {
                return Retorno.error3("El centro logistico no existe.");
            }

            ListaImp<CentroLogistico> listaPasarAString=grafoConCentros.bfsConNivelYCantidadDeNiveles(centroLogFantasma, cantidad);

            String retorno=ordenaListaDevuelveString(listaPasarAString);


            return Retorno.ok(retorno);
        }

        @Override
        public Retorno viajeCostoMinimoDistancia (String codigoOrigen, String codigoDestino){
            return Retorno.noImplementada();
        }

        @Override
        public Retorno viajeCostoMinimoTiempo (String codigoOrigen, String codigoDestino){
            return Retorno.noImplementada();
        }







//----------------------------------METODOS ACCESORIOS----------------------------------


    private String ordenaListaDevuelveString(ListaImp<CentroLogistico> lista) {
        String ret="";
        for (int i = 0; i < lista.largo() - 1; i++) {
            int indiceMin = i;

            for (int j = i + 1; j < lista.largo(); j++) {
                if (lista.recuperar(j).getDato()
                        .compareTo(lista.recuperar(indiceMin).getDato()) < 0) {
                    indiceMin = j;
                }
            }
            if (indiceMin != i) {
                CentroLogistico temp = lista.recuperar(i).getDato();
                lista.recuperar(i).setDato(lista.recuperar(indiceMin).getDato());
                lista.recuperar(indiceMin).setDato(temp);
            }
           ret+= lista.recuperar(i).getDato().getCodigo()+";"+lista.recuperar(i).getDato().getNombre()+"|";
        }
        ret+= lista.recuperar(lista.largo()-1).getDato().getCodigo()+";"+lista.recuperar(lista.largo()-1).getDato().getNombre();

        return ret;

    }

    private boolean formatoValidoCodigo(String codigo) {
        String formato = "[A-Za-z]{2}-[0-9]{3}-[A-Za-z0-9]{6}";
        return codigo.matches(formato);

    }

    private void crearArbolesEnCadaCategoria(ListaImp<ABB<Mercaderia>> lista) {

        for (Categoria cat : Categoria.values()) {

            lista.insertarAlFinal(new ABB<Mercaderia>());
        }
    }




    private Mercaderia obtenerMercaderíaPorId(Mercaderia mercaderia, NodoGen<Mercaderia> nodo) {
        if (nodo == null) {
            return null;
        } else {
            if (nodo.getDato().compareTo(mercaderia) == 0) {
                return nodo.getDato();
            } else if (nodo.getDato().compareTo(mercaderia) < 0) {
                return obtenerMercaderíaPorId(mercaderia, nodo.getDer());
            } else {
                return obtenerMercaderíaPorId(mercaderia, nodo.getIzq());
            }
        }


    }

    private Mercaderia obtenerMercaderíaPorCodigo(WMercaderiaPorCodigo mercaderia, NodoGen<WMercaderiaPorCodigo> nodo) {

        if (nodo == null) {
            return null;
        } else {
            if (nodo.getDato().compareTo(mercaderia) == 0) {
                return nodo.getDato().getMercaderia();
            } else if (nodo.getDato().compareTo(mercaderia) < 0) {
                return obtenerMercaderíaPorCodigo(mercaderia, nodo.getDer());
            } else {
                return obtenerMercaderíaPorCodigo(mercaderia, nodo.getIzq());
            }
        }


    }


    private <T> void cargarListaAsc(ListaImp<T> listaMer, NodoGen<T> nodo) {
        if (nodo == null) {
            return;
        } else {
            cargarListaAsc(listaMer, nodo.getDer());
            listaMer.insertar(nodo.getDato());
            cargarListaAsc(listaMer, nodo.getIzq());

        }

    }

    private void cargarListaDesc(ListaImp<Mercaderia> listaMer, NodoGen<Mercaderia> nodo) {
        if (nodo == null) {
            return;
        } else {
            cargarListaAsc(listaMer, nodo.getIzq());
            listaMer.insertar(nodo.getDato());
            cargarListaAsc(listaMer, nodo.getDer());

        }

    }



}
