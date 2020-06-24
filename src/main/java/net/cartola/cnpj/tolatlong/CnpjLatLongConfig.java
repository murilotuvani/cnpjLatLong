package net.cartola.cnpj.tolatlong;

import java.util.Set;

/**
 * 23/06/2020 22:53:54
 *
 * @author murilotuvani
 */
public class CnpjLatLongConfig {

    private String apiKey;
    private Set<Integer> cnaes;
    private Set<String> cidades;

    private String bdHost;
    private String bdBancoDados;
    private String bdUsuario;
    private String bdSenha;

    private String buffer;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Set<Integer> getCnaes() {
        return cnaes;
    }

    public void setCnaes(Set<Integer> cnaes) {
        this.cnaes = cnaes;
    }

    public Set<String> getCidades() {
        return cidades;
    }

    public void setCidades(Set<String> cidades) {
        this.cidades = cidades;
    }

    public String getBdHost() {
        return bdHost;
    }

    public void setBdHost(String bdHost) {
        this.bdHost = bdHost;
    }

    public String getBdBancoDados() {
        return bdBancoDados;
    }

    public void setBdBancoDados(String bdBancoDados) {
        this.bdBancoDados = bdBancoDados;
    }

    public String getBdUsuario() {
        return bdUsuario;
    }

    public void setBdUsuario(String bdUsuario) {
        this.bdUsuario = bdUsuario;
    }

    public String getBdSenha() {
        return bdSenha;
    }

    public void setBdSenha(String bdSenha) {
        this.bdSenha = bdSenha;
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

}
