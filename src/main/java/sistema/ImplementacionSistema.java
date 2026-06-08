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



    @Override
    public Retorno inicializarSistema(int maxCentros) {
        if (maxCentros <= 3) {
            return Retorno.error1("El máximo de centros debe ser mayor a 3");
        }

        this.arbolesPorCategoria = new ListaImp<ABB<Mercaderia>>();
        this.grafoConCentros = new Grafo(maxCentros, true);
        arbolGralMercaderia = new ABB<Mercaderia>();
        arbolGralMercaderiaPorCodigo = new ABB<WMercaderiaPorCodigo>();
        crearArbolesEnCadaCategoria(arbolesPorCategoria);
        return Retorno.ok("El sistema pudo ser inicializado correctamente");


    }


    @Override
    public Retorno registrarMercaderia(String id, String codigo, String descripcion, boolean fragil,
                                       Categoria categoria) {

        //Error 1
        if (id == null || id.isBlank() || codigo == null || codigo.isBlank() ||
                descripcion == null || descripcion.isBlank() || categoria == null) {
            return Retorno.error1("No puede haber campos vacíos o en null");
        }

        //Error 2
        if (!(formatoValidoCodigo(codigo))) {
            return Retorno.error2("Debe cumplir con el formato estipulado");
        }

        //Error 3
        Mercaderia mercaderiaParaChequeoXId = new Mercaderia(id, null, null, false, null);
        if (arbolGralMercaderia.pertenece(mercaderiaParaChequeoXId)) {
            return Retorno.error3("Ya existe una mercadería con ese Id");
        }

        //Error 4
        Mercaderia mercaderiaParaChequeoXCP = new Mercaderia(null, codigo, null, false, null);
        WMercaderiaPorCodigo wrapperFantasma = new WMercaderiaPorCodigo(mercaderiaParaChequeoXCP);
        if (arbolGralMercaderiaPorCodigo.pertenece(wrapperFantasma)) {
            return Retorno.error4("Ya existe una mercadería con ese codigo");
        }

        //Caso feliz
        Mercaderia mer = new Mercaderia(id, codigo, descripcion, fragil, categoria);
        arbolGralMercaderia.insertar(mer);
        WMercaderiaPorCodigo wMer = new WMercaderiaPorCodigo(mer);
        arbolGralMercaderiaPorCodigo.insertar(wMer);
        insertarMercaderiaEnArbolDeSuCategoria(mer);

        return Retorno.ok("Mercadería ingresada correctamente");
    }




    @Override
    public Retorno buscarMercaderiaPorId(String id) {

        //Error 1
        if (id == null || id.isBlank()) {
            return Retorno.error1("No puede haber campos vacíos o en null");
        }

        Mercaderia mercaderiaParaChequeo = new Mercaderia(id, null, null, false, null);
        int[] contador=new int[1];
        Mercaderia mercaderiaAMostrar = obtenerMercaderiaPorId(mercaderiaParaChequeo, arbolGralMercaderia.getRaiz(),contador);

        //Error 2
        if (mercaderiaAMostrar == null) {
            return Retorno.error2("No existe una mercadería con ese Id");
        }

        //Caso feliz
        String retorna=mercaderiaAMostrar.getId() + ";" + mercaderiaAMostrar.getCodigoPostal() + ";" + mercaderiaAMostrar.getDescripcion()
        + ";" + mercaderiaAMostrar.isFragil() + ";" + mercaderiaAMostrar.getCategoria().getTexto();

        return Retorno.ok(contador[0],retorna);

    }

    @Override
    public Retorno listarMercaderiasPorIdAscendente() {

        //Caso feliz
        ListaImp<Mercaderia> listaMer = new ListaImp<Mercaderia>();
        cargarListaAsc(listaMer, arbolGralMercaderia.getRaiz());
        String salida = "";
        Nodo<Mercaderia> actual = listaMer.getInicio();

        while (actual != null) {
            salida += actual.getDato().getId() + ";" + actual.getDato().getCodigoPostal() +
                    ";" + actual.getDato().getDescripcion() + ";" + actual.getDato().isFragil() +
                    ";" + actual.getDato().getCategoria().getTexto() + "|";

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
        //Caso feliz
        ListaImp<Mercaderia> listaMer = new ListaImp<Mercaderia>();
        cargarListaDesc(listaMer, arbolGralMercaderia.getRaiz());
        String salida = "";
        Nodo<Mercaderia> actual = listaMer.getInicio();

        while (actual != null) {
            salida += actual.getDato().getId() + ";" + actual.getDato().getCodigoPostal() +
                    ";" + actual.getDato().getDescripcion() + ";" + actual.getDato().isFragil() +
                    ";" + actual.getDato().getCategoria().getTexto() + "|";

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

        //Error 1
        if (codigo == null || codigo.isBlank()) {
            return Retorno.error1("No puede haber campos vacíos o en null");
        }
        Mercaderia mercaderiaParaChequeo = new Mercaderia(null, codigo, null, false, null);
        //ARRANCA EN UNO PORQUE ARANCAMOS POR LA RAIZ Y ESE VA A ESTAR VISITADO O RECORRIDO.
        int[] cont=new int[1];

        WMercaderiaPorCodigo wMercaderiaParaChequeo = new WMercaderiaPorCodigo(mercaderiaParaChequeo);
        Mercaderia mercaderiaAMostrar = obtenerMercaderíaPorCodigo(wMercaderiaParaChequeo, arbolGralMercaderiaPorCodigo.getRaiz(), cont);
        //Error 2
        if (mercaderiaAMostrar == null) {
            return Retorno.error2("No existe una mercadería con ese código");

        }

        String ret=mercaderiaAMostrar.getId() + ";" + mercaderiaAMostrar.getCodigoPostal() +
                ";" + mercaderiaAMostrar.getDescripcion()+ ";" + mercaderiaAMostrar.isFragil() +
                ";" + mercaderiaAMostrar.getCategoria().getTexto();

        return Retorno.ok(cont[0],ret);

    }


    @Override
    public Retorno listarMercaderiasPorCodigoAscendente() {

        //Camino feliz
        ListaImp<WMercaderiaPorCodigo> listaWMer = new ListaImp<WMercaderiaPorCodigo>();
        cargarListaAsc(listaWMer, arbolGralMercaderiaPorCodigo.getRaiz());
        String salida = "";
        Nodo<WMercaderiaPorCodigo> actual = listaWMer.getInicio();

        while (actual != null) {
            salida += actual.getDato().getMercaderia().getId()+";"+actual.getDato().getMercaderia().getCodigoPostal()+
                    ";"+actual.getDato().getMercaderia().getDescripcion()+";"+actual.getDato().getMercaderia().isFragil()+
                    ";"+actual.getDato().getMercaderia().getCategoria().getTexto() + "|";

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

        // Camino feliz

        int indiceCategoria = unaCategoria.getIndice();

        // Recuperamos el NODO de la lista que contiene el arbol
        Nodo<ABB<Mercaderia>> nodoContenedor = arbolesPorCategoria.recuperar(indiceCategoria);

        // Extraemos el ÁRBOL (ABB) de mercaderías que está almacenado dentro del nodo
        ABB<Mercaderia> arbolDeLaCategoria = nodoContenedor.getDato();

        //Pasamos a lista ascendente los datos del arbol.
        ListaImp<Mercaderia> arbolCatALista=arbolDeLaCategoria.obtenerAsc();

        //Chequeamos que no sea vacia para no tener problemas dentro del while
        // y que se rompa cuando trabajamos con subString
        if (arbolCatALista.esVacia()) {
            return Retorno.ok("");
        }
        Nodo<Mercaderia> actual = arbolCatALista.getInicio();
        String ret="";

        //formateamos el string según formato esperado.
        while (actual != null) {
            Mercaderia m = actual.getDato();
            ret += m.getId() + ";" + m.getCodigoPostal() + ";" + m.getDescripcion() + ";" +m.isFragil() + ";" + m.getCategoria().getTexto() + "|";
            actual=actual.getSig();
        }
        //Quitamos el | útlimo que no corresponde
        ret=ret.substring(0, ret.length() - 1);


        return Retorno.ok(ret);

    }


    @Override
    public Retorno registrarCentroLogistico(String codigo, String nombre, String departamento, String direccion) {

        //Error 1
        if (grafoConCentros.getCantActual()>= grafoConCentros.getTope()) {
            return Retorno.error1("Se han alcanzado el numero máximo de centros");
        }

        //Error 2
        if (codigo == null || codigo.isBlank() || nombre == null || nombre.isBlank() || departamento == null || departamento.isBlank() || direccion == null|| direccion.isBlank()) {
            return Retorno.error2("No puede haber campos vacíos o en null");
        }

        CentroLogistico centroAAgregar=new CentroLogistico(codigo,  nombre,  departamento,  direccion);

        //Error 3
        if (grafoConCentros.existeVertice(centroAAgregar)) {
            return Retorno.error3("Ya existe una centro Logísitico con ese código");

        }

        //Camino feliz
        grafoConCentros.agregarVertice(centroAAgregar);
        return Retorno.ok();
    }



    @Override
        public Retorno registrarConexion (String codigoOrigen, String codigoDestino,int distancia, int tiempo){

        //Error 1
        if (codigoOrigen == null || codigoOrigen.isBlank() || codigoDestino == null || codigoDestino.isBlank()) {
            return Retorno.error1("No puede haber campos vacíos o en null");
        }


        CentroLogistico centroOrigenFantasma= new CentroLogistico(codigoOrigen, null, null, null);

        //Error 2
        if (!(grafoConCentros.existeVertice(centroOrigenFantasma))) {
            return Retorno.error2("El centro de origeno no existe.");
        }

        CentroLogistico centroDestinoFantasma= new CentroLogistico(codigoDestino, null, null, null);

        //Error 3
        if (!(grafoConCentros.existeVertice(centroDestinoFantasma))) {
            return Retorno.error3("El centro de destino no existe.");
        }

        //Error 4
        if (distancia<=0) {
            return Retorno.error4("La distancia debe ser mayor a 0.");
        }

        //Error 5
        if (tiempo<=0) {
            return Retorno.error5("El tiempo debe ser mayor a 0.");
        }

        //Error 6
        if (grafoConCentros.sonAdyacentes(centroOrigenFantasma,centroDestinoFantasma)) {
            return Retorno.error6("La conexión ya existe en el grafo");
        }


        //Camino feliz
        Conexion conexion=new Conexion(codigoOrigen,  codigoDestino,  distancia,  tiempo);
        grafoConCentros.agregarArista(centroOrigenFantasma, centroDestinoFantasma, conexion);

        return Retorno.ok();
        }







        @Override
        public Retorno redCentrosPorCantidadDeConexiones (String codigoOrigen,int cantidad){

            //Error 1
            if (cantidad<0) {
                return Retorno.error1("ingrese una cantidad mayor a cero");
            }

            //Error 2
            if (codigoOrigen==null||codigoOrigen.isBlank()) {
                return Retorno.error2("Debe introducir un codigo de origen.");
            }

            CentroLogistico centroLogFantasma= new CentroLogistico(codigoOrigen, null, null, null);

            //Error 3
            if (!(grafoConCentros.existeVertice(centroLogFantasma))) {
                return Retorno.error3("El centro logistico no existe.");
            }

            //Camino feliz

            ListaImp<CentroLogistico> listaPasarAString=grafoConCentros.bfsConNivelYCantidadDeNiveles(centroLogFantasma, cantidad);

            String retorno=ordenaListaDevuelveString(listaPasarAString);


            return Retorno.ok(retorno);
        }

        @Override
        public Retorno viajeCostoMinimoDistancia (String codigoOrigen, String codigoDestino){

            //Error 1
            if (codigoOrigen==null||codigoOrigen.isBlank()||codigoDestino==null||codigoDestino.isBlank()) {
                return Retorno.error1("Los datos a ingresar no pueden estar vacios");
            }


            CentroLogistico centroOrigenFantasma= new CentroLogistico(codigoOrigen, null, null, null);

            //Error 2
            if (!(grafoConCentros.existeVertice(centroOrigenFantasma))) {
                return Retorno.error2("El centro logistico no existe.");
            }

            CentroLogistico centroDestinoFantasma= new CentroLogistico(codigoDestino, null, null, null);

            //Error 3
            if (!(grafoConCentros.existeVertice(centroDestinoFantasma))) {
                return Retorno.error3("El centro de destino no existe.");
            }
            int [] contador=new int[1];
            ListaImp<CentroLogistico> camino = grafoConCentros.obtenerCaminoMasCorto(centroOrigenFantasma, centroDestinoFantasma, "DISTANCIA", contador);

            //Error 4
            if (camino == null) {
                return Retorno.error4("No existe conexión entre los vértices.");
            }


            //Camino feliz
            String caminoFinal = armarStringCamino(camino);

            return Retorno.ok(contador[0], caminoFinal);
        }

        @Override
        public Retorno viajeCostoMinimoTiempo (String codigoOrigen, String codigoDestino){
            //Error 1
            if (codigoOrigen==null||codigoOrigen.isBlank()||codigoDestino==null||codigoDestino.isBlank()) {
                return Retorno.error1("Los datos a ingresar no pueden estar vacios");
            }


            CentroLogistico centroOrigenFantasma= new CentroLogistico(codigoOrigen, null, null, null);

            //Error 2
            if (!(grafoConCentros.existeVertice(centroOrigenFantasma))) {
                return Retorno.error2("El centro logistico no existe.");
            }

            CentroLogistico centroDestinoFantasma= new CentroLogistico(codigoDestino, null, null, null);

            //Error 3
            if (!(grafoConCentros.existeVertice(centroDestinoFantasma))) {
                return Retorno.error3("El centro de destino no existe.");
            }
            int [] contador=new int[1];
            ListaImp<CentroLogistico> camino = grafoConCentros.obtenerCaminoMasCorto(centroOrigenFantasma, centroDestinoFantasma, "TIEMPO", contador);

            //Error 4
            if (camino == null) {
                return Retorno.error4("No existe conexión entre los vértices.");
            }



            //Camino feliz
            String caminoFinal = armarStringCamino(camino);

            return Retorno.ok(contador[0], caminoFinal);
        }




//----------------------------------******************----------------------------------
//----------------------------------******************----------------------------------
//----------------------------------METODOS ACCESORIOS----------------------------------
//----------------------------------******************----------------------------------
//----------------------------------******************----------------------------------



    private void insertarMercaderiaEnArbolDeSuCategoria(Mercaderia mer) {

        //Nos quedamos con el indice de la categoria de la mercadería a agregar
        int indiceCategoria = mer.getCategoria().getIndice();


        //Buscamos y recuperamos el NODO de la lista en esa posición específica(coincide porque así fue diseñado)
        Nodo<ABB<Mercaderia>> nodoContenedor = arbolesPorCategoria.recuperar(indiceCategoria);

        //Extraemos el ÁRBOL (ABB) que está guardado adentro de ese nodo
        ABB<Mercaderia> arbolDeLaCategoria = nodoContenedor.getDato();

        //Insertamos la mercadería en su árbol correspondiente
        arbolDeLaCategoria.insertar(mer);
    }

    private String ordenaListaDevuelveString(ListaImp<CentroLogistico> lista) {
        // Caso de borde: si no hay elementos, devolvemos un String vacío sin romper nada
        if (lista == null || lista.esVacia()) {
            return "";
        }

        // 1. Delegamos el ordenamiento al método especializado
        ordenarListaPorSeleccion(lista);

        // 2. Construimos el String con la lista YA ordenada y con todos los campos
        String ret = "";
        for (int i = 0; i < lista.largo(); i++) {
            CentroLogistico centro = lista.recuperar(i).getDato();

            // Formato exacto de la letra: id;nombre;departamento;direccion
            ret += centro.getCodigo() + ";" +
                    centro.getNombre() + ";" +
                    centro.getDepartamento() + ";" +
                    centro.getDireccion();

            // Agregamos el separador "|" solo si quedan más elementos por procesar
            if (i < lista.largo() - 1) {
                ret += "|";
            }
        }

        return ret;
    }

    private void ordenarListaPorSeleccion(ListaImp<CentroLogistico> lista) {
        // Algoritmo de selección directo sobre la lista enlazada
        for (int i = 0; i < lista.largo() - 1; i++) {
            int indiceMin = i;

            for (int j = i + 1; j < lista.largo(); j++) {
                if (lista.recuperar(j).getDato()
                        .compareTo(lista.recuperar(indiceMin).getDato()) < 0) {
                    indiceMin = j;
                }
            }

            // Si encontramos un elemento menor, intercambiamos los datos de los nodos
            if (indiceMin != i) {
                CentroLogistico temp = lista.recuperar(i).getDato();
                lista.recuperar(i).setDato(lista.recuperar(indiceMin).getDato());
                lista.recuperar(indiceMin).setDato(temp);
            }
        }
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




    private Mercaderia obtenerMercaderiaPorId(Mercaderia mercaderia, NodoGen<Mercaderia> nodo, int[] contador) {
        if (nodo == null) {
            return null;
        }

        contador[0]++;

        if (nodo.getDato().compareTo(mercaderia) == 0) {
            return nodo.getDato();
        } else
            if (nodo.getDato().compareTo(mercaderia) < 0) {
                return obtenerMercaderiaPorId(mercaderia, nodo.getDer(), contador);
            }
            else {
                return obtenerMercaderiaPorId(mercaderia, nodo.getIzq(), contador);
        }
    }




    private Mercaderia obtenerMercaderíaPorCodigo(WMercaderiaPorCodigo mercaderia, NodoGen<WMercaderiaPorCodigo> nodo, int[] cont) {

        if (nodo == null) {
            return null;
        } else {
            cont[0]++;

            if (nodo.getDato().compareTo(mercaderia) == 0) {
                return nodo.getDato().getMercaderia();
            } else if (nodo.getDato().compareTo(mercaderia) < 0) {
                return obtenerMercaderíaPorCodigo(mercaderia, nodo.getDer(), cont);
            } else {
                return obtenerMercaderíaPorCodigo(mercaderia, nodo.getIzq(), cont);
            }
        }


    }


    private <T> void cargarListaAsc(ListaImp<T> listaMer, NodoGen<T> nodo) {
        if (nodo == null) {
            return;
        } else {
            cargarListaAsc(listaMer, nodo.getDer());
            listaMer.insertarAlInicio(nodo.getDato());
            cargarListaAsc(listaMer, nodo.getIzq());

        }

    }

    private void cargarListaDesc(ListaImp<Mercaderia> listaMer, NodoGen<Mercaderia> nodo) {
        if (nodo == null) {
            return;
        } else {
            cargarListaDesc(listaMer, nodo.getIzq());
            listaMer.insertarAlInicio(nodo.getDato());
            cargarListaDesc(listaMer, nodo.getDer());

        }

    }


    private String armarStringCamino(ListaImp<CentroLogistico> listaCamino) {
        // Caso de borde: si la lista está vacía, evitamos problemas y devolvemos ""
        if (listaCamino == null || listaCamino.esVacia()) {
            return "";
        }

        String resultado = "";

        // CORREGIDO: Recorrido estándar desde 0 hasta largo - 1
        for (int i = 0; i < listaCamino.largo(); i++) {
            CentroLogistico centro = listaCamino.recuperar(i).getDato();

            String datosCentro = centro.getCodigo() + ";" +
                    centro.getNombre() + ";" +
                    centro.getDepartamento() + ";" +
                    centro.getDireccion();

            // ¡Tu truco del orden inverso! Se mantiene intacto porque funciona genial
            if (resultado.isEmpty()) {
                resultado = datosCentro;
            } else {
                resultado =  resultado + "|" + datosCentro ;
            }
        }

        return resultado;
    }



}
