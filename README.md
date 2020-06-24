# cnpjLatLong

Este é mais um projeto relacionado aos dados cadastrais das empresas no Brasil.
A principal intenção aqui é preparar os dados para exibir no BigQuery e/do DataStudio.
Neste caso preenchemos os dados de latitude e longitude dos endereços das empresas utilizando a [API de Geolocalizaçaõ do Google] (https://developers.google.com/maps/documentation/geolocation/intro)

Para utilizar este projeto clone-o e execute, na primeira vez ele não vai encontrar o arquivo de configuração e então irá criá-lo, preencha com os dados que deseja e execute novamente.
# Markdown
'''
git clone https://github.com/murilotuvani/cnpjLatLong.git
cd cnpjLatLong
mvn package
java -jar target/cnpjLatLong.jar
'''


O projeto [cnpj-writer] (https://github.com/murilotuvani/cnpj-writer-springboot-mysql) é o responsável para criar a base de dados e inserir os dados.
O projeto [cnpj-reeader] (https://github.com/murilotuvani/cnpj-reader) é responsável por processar os arquivos da receita federal e enviar estes dados ao cnpj-writer.
