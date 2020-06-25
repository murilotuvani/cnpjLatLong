package net.cartola.cnpj.tolatlong;

import java.io.Serializable;
import java.math.BigDecimal;

public class Cnpj implements Serializable {

    private long cnpjId;
    private int cnae;
    private long cnpj;
    private String tipoLogradouro;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String municipio;
    private String uf;
    private int cep;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String placeId;

    public long getCnpjId() {
        return cnpjId;
    }

    public void setCnpjId(long cnpjId) {
        this.cnpjId = cnpjId;
    }

    public int getCnae() {
        return cnae;
    }

    public void setCnae(int cnae) {
        this.cnae = cnae;
    }

    public long getCnpj() {
        return cnpj;
    }

    public void setCnpj(long cnpj) {
        this.cnpj = cnpj;
    }

    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(String tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public int getCep() {
        return cep;
    }

    public void setCep(int cep) {
        this.cep = cep;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public boolean setLatitude(BigDecimal latitude) {
        boolean alterado = false;
        if ((this.latitude == null && latitude != null)
                || (this.latitude != null && !this.latitude.equals(latitude))) {
            this.latitude = latitude;
            alterado = true;
        }
        return alterado;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public boolean setLongitude(BigDecimal longitude) {
        boolean alterado = false;
        if ((this.longitude == null && longitude != null)
                || (this.longitude != null && !this.longitude.equals(longitude))) {
            this.longitude = longitude;
            alterado = true;
        }
        return alterado;
    }

    public String getPlaceId() {
        return placeId;
    }

    public boolean setPlaceId(String placeId) {
        boolean alterado = false;
        if ((this.placeId == null && placeId != null)
                || (this.placeId != null && !this.placeId.equals(placeId))) {
            this.placeId = placeId;
            alterado = true;
        }
        return alterado;
    }
    
    

}
