package dominio;

//Clase creada unicamente para poder comparar por códigoPostal los objetos mercadería. Esto para listar por codigoPostal, etc

public class WMercaderiaPorCodigo implements Comparable<WMercaderiaPorCodigo>{

   private  Mercaderia mercaderia;

    public WMercaderiaPorCodigo(Mercaderia mercaderia) {
        this.mercaderia = mercaderia;
    }


    public Mercaderia getMercaderia() {
        return mercaderia;
    }

    @Override
    public int compareTo(WMercaderiaPorCodigo o) {
        if (o == null || this.mercaderia == null || o.getMercaderia() == null) return 0;

        String cpInterno = this.mercaderia.getCodigoPostal();
        String cpExterno = o.getMercaderia().getCodigoPostal();

        if (cpInterno == null && cpExterno == null) return 0;
        if (cpInterno == null) return -1;
        if (cpExterno == null) return 1;

        return cpInterno.compareTo(cpExterno);
    }
}
