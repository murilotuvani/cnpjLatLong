package net.cartola.cnpj.tolatlong;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 23/06/2020 12:19:06
 *
 * @author murilotuvani
 */
public class ConsultarLatLongDoCnpj {

    private final CnpjLatLongConfig config;
    private final ConnectionFactory connectionFactory;
    private GeoApiContext geoApiContext;

    public ConsultarLatLongDoCnpj(CnpjLatLongConfig config, ConnectionFactory cf) {
        this.config = config;
        this.connectionFactory = cf;
    }

    public static void main(String args[]) throws InterruptedException {
        File configFile = new File("config.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (configFile.exists()) {
            try (FileReader fileReader = new FileReader(configFile)) {
                CnpjLatLongConfig config = gson.fromJson(fileReader, CnpjLatLongConfig.class);
                System.out.println("API-KEY : " + config.getApiKey());
                System.out.println("CNAES   : " + config.getCnaes());
                System.out.println("Cidades : " + config.getCidades());

                ConnectionFactory cf = new ConnectionFactory(config);
                try (Connection conn = cf.getConnection()) {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT NOW()");
                    if (rs.next()) {
                        Timestamp agora = rs.getTimestamp(1);
                        String agoraStr = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(agora);
                        System.out.println("Horário do servidor : " + agoraStr + " banco conectado!");
                    }
                    rs.close();
                    stmt.close();

                    ConsultarLatLongDoCnpj latLongDoCnpj = new ConsultarLatLongDoCnpj(config, cf);
                    latLongDoCnpj.executar();
                }
            } catch (IOException ex) {
                System.out.println("Erro ao ler o arquivo de configuracao : " + ex.getMessage());
            } catch (SQLException ex) {
                System.out.println("Erro com o banco de dados : " + ex.getMessage());
            } catch (ApiException ex) {
                System.out.println("Erro com a API do Maps : " + ex.getMessage());
            }

        } else {

            try {
                String apiKey = "Coloque a chave aqui";
                Set<String> cidades = new TreeSet<>(Arrays.asList("São Paulo/SP", "Rio de Janeiro/RJ", "Belo Horizonte/MG"));
                Set<Integer> cnaes = new TreeSet<>(Arrays.asList(1000000, 2000000, 3000000));

                CnpjLatLongConfig llc = new CnpjLatLongConfig();
                llc.setApiKey(apiKey);
                llc.setCidades(cidades);
                llc.setCnaes(cnaes);

                llc.setBdUsuario("bancoDadosUsuario");
                llc.setBdSenha("bancoDadosSenha");
                llc.setBdHost("localhost:3306");
                llc.setBdBancoDados("cnpj");

                llc.setBuffer("/Users/murilotuvani/cnpj");
                String json = gson.toJson(llc);
                Files.write(configFile.toPath(), json.getBytes(Charset.forName("UTF-8")), StandardOpenOption.CREATE_NEW);
            } catch (IOException ex) {
                System.out.println("Erro ao tentar criar o arquivo de configuracao : " + ex.getMessage());
            }
        }
    }

    private void executar() throws SQLException, IOException, ApiException, InterruptedException {
        geoApiContext = new GeoApiContext.Builder()
                .apiKey(config.getApiKey())
                .build();

        for (String cidade : config.getCidades()) {
            String municipio = cidade.substring(0, cidade.indexOf("/"));
            String uf = cidade.substring(cidade.indexOf("/") + 1);
            try (Connection conn = this.connectionFactory.getConnection()) {
                StringBuilder sbWhere = new StringBuilder(
                                "\n WHERE c.latitude is null")
                       .append( "\n   and c.municipio='").append(municipio)
                       .append("'\n   and c.uf='").append(uf)
                       .append("'\n   and c.cnae in (");
                    
                boolean first = true;
                for (Integer cnae : config.getCnaes()) {
                    if (first) {
                        first = false;
                    } else {
                        sbWhere.append(",");
                    }
                    sbWhere.append(cnae);
                }
                sbWhere.append(")");
                
                boolean debug = Boolean.getBoolean(System.getProperty("debug", "true"));
                if (debug) {
                    sbWhere.append("\n limit 5");
                }
                
                CnpjDao cnpjDao = new CnpjDao(conn);
                String where = sbWhere.toString();
                List<Cnpj> cnpjs = cnpjDao.listar(where);
                
                List<Cnpj> atualizados = new ArrayList<>();
                for(Cnpj cnpj:cnpjs) {
                    if(preencheLatitudeLongitude(cnpj)) {
                        atualizados.add(cnpj);
                    }
                    if (atualizados.size() > 250) {
                        cnpjDao.update(atualizados);
                        atualizados = new ArrayList<>();
                    }
                }
                if (atualizados.size() > 0) {
                    cnpjDao.update(atualizados);
                }
            }

        }
    }

    private boolean preencheLatitudeLongitude(Cnpj cnpj) throws IOException, ApiException, InterruptedException {
        File diretorio = new File(config.getBuffer());
        boolean alterado = false;
        
        if (diretorio.exists()) {
            File arquivoCnpj = new File(diretorio, cnpj.getCnpj() + ".json");
            GeocodingResult[] result;
            if(arquivoCnpj.exists()) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                try(FileReader reader=new FileReader(arquivoCnpj)) {
                    result = gson.fromJson(reader, GeocodingResult[].class);
                }
            } else {
                result = buscarApi(cnpj);
            }
            alterado = atribuiLatLng(cnpj, result);
        }
        return alterado;
    }

    private GeocodingResult[] buscarApi(Cnpj cnpj) throws ApiException, IOException, InterruptedException {
        StringBuilder addressBuilder = new StringBuilder(cnpj.getLogradouro());
        addressBuilder.append(",").append(cnpj.getNumero());
        
        if(isNotNull(cnpj.getComplemento())) {
            addressBuilder.append(cnpj.getComplemento());            
        }
        
        addressBuilder.append(" - ").append(cnpj.getBairro());
        addressBuilder.append(", ").append(cnpj.getMunicipio());
        addressBuilder.append("/").append(cnpj.getUf());
        
        String address = addressBuilder.toString();
        LatLng southWestBound = new LatLng(-25.0, -49.0);
        LatLng northEastBound = new LatLng(-20.0, -45.0);
        GeocodingApiRequest request = GeocodingApi.newRequest(geoApiContext)
                .bounds(southWestBound, northEastBound).region("BR")
                .address(address);
        GeocodingResult[] result = request.await();
        File diretorio = new File(config.getBuffer());
        File arquivoCnpj = new File(diretorio, cnpj.getCnpj() + ".json");
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(result);
        
        Files.write(arquivoCnpj.toPath(), json.getBytes(Charset.forName("UTF-8")), StandardOpenOption.CREATE_NEW);
        return result;
    }

    private boolean isNotNull(String str) {
        return !isNull(str);
    }
    
    public boolean isNull(String str) {
        return (str == null || str.trim().equals(""));
    }

    private boolean atribuiLatLng(Cnpj cnpj, GeocodingResult[] result) {
        BigDecimal latitude = new BigDecimal(result[0].geometry.location.lat);
        BigDecimal longitude = new BigDecimal(result[0].geometry.location.lng);
        String placeId = result[0].placeId;
        System.out.println("Google Place Id : " + placeId);
        boolean alterado = false;
        alterado = cnpj.setLatitude(latitude) || alterado;
        alterado = cnpj.setLongitude(longitude) || alterado;
        alterado = cnpj.setPlaceId(placeId) || alterado;
        return alterado;
    }

}
