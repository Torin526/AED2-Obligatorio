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
        return this.mercaderia.getCodigoPostal().compareTo(o.getMercaderia().getCodigoPostal());
    }
}
