package net.cartola.cnpj.tolatlong;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
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

    public static void main(String args[]) {
        File configFile = new File("config.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();;
        if (configFile.exists()) {
            try (FileReader fileReader = new FileReader(configFile)) {
                CnpjLatLongConfig llc = gson.fromJson(fileReader, CnpjLatLongConfig.class);
                System.out.println("API-KEY : " + llc.getApiKey());
                System.out.println("CNAES   : " + llc.getCnaes());
                System.out.println("Cidades : " + llc.getCidades());
            } catch (IOException ex) {
                System.out.println("Erro ao ler o arquivo de configuracao : " + ex.getMessage());
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

//        File file = new File(args.length > 0 ? args[0] : "certificado.txt");
//        if (file.exists()) {
//            try {
//                List<String> lines = Files.readAllLines(file.toPath());
//                if (lines.size() > 0) {
//                    System.out.println("Olá CNPJ!!" + lines.get(0));
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(ConsultarLatLongDoCnpj.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//            System.out.println("Informar o local do arquivo do certificado");
//        }
    }

}
