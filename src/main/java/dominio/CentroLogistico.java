package dominio;

import interfaz.Categoria;

public class CentroLogistico implements Comparable<CentroLogistico>{
private String codigo;
private String nombre;
private String departamento;
private String direccion;


    public CentroLogistico() {

    }

    public CentroLogistico(String codigo, String nombre, String departamento, String direccion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.departamento = departamento;
        this.direccion = direccion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public int compareTo(CentroLogistico o) {
        return this.codigo.compareTo(o.codigo);
    }
}



