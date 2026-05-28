package dominio;

import interfaz.Categoria;

public class Mercaderia implements Comparable<Mercaderia> {
    private String id;
    private String codigoPostal;
    private String descripcion;
    private boolean fragil;
    private Categoria categoria;



    public Mercaderia(){

    }

public Mercaderia (String id,String codigoPostal,String descripcion,boolean fragil,Categoria categoria){

this.id=id;
this.codigoPostal=codigoPostal;
this.descripcion=descripcion;
this.fragil=fragil;
this.categoria=categoria;

}

public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

public String getCodigoPostal() {
    return codigoPostal;
}

public void setCodigoPostal(String codigoPostal) {
    this.codigoPostal = codigoPostal;
}

public String getDescripcion() {
    return descripcion;
}

public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
}

public boolean isFragil() {
    return fragil;
}

public void setFragil(boolean fragil) {
    this.fragil = fragil;
}

public Categoria getCategoria() {
    return categoria;
}

public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
}


    @Override
    public int compareTo(Mercaderia o) {
        return this.id.compareTo(o.id);
    }
}


