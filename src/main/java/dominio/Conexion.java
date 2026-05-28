package dominio;

import java.util.Objects;

public class Conexion {
    private String codOrigen;
    private String codDestino;
    private int distancia;
    private int tiempo;

    public Conexion() {
    }


    public Conexion(String codOrigen, String codDestino, int distancia, int tiempo) {
        this.codOrigen = codOrigen;
        this.codDestino = codDestino;
        this.distancia = distancia;
        this.tiempo = tiempo;
    }

    public String getCodOrigen() {
        return codOrigen;
    }

    public void setCodOrigen(String codOrigen) {
        this.codOrigen = codOrigen;
    }

    public String getCodDestino() {
        return codDestino;
    }

    public void setCodDestino(String codDestino) {
        this.codDestino = codDestino;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Conexion conexion)) return false;
        return Objects.equals(codOrigen, conexion.codOrigen) && Objects.equals(codDestino, conexion.codDestino);
    }

/*      @Override
    public int hashCode() {
      return Objects.hash(codOrigen, codDestino);
  }
  */

}
