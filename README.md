# cnpjLatLong - Projeto de atribuíção de latitude e longitude aos endereços

Este é mais um projeto relacionado aos dados cadastrais das empresas no Brasil.
A principal intenção aqui é preparar os dados para exibir no BigQuery e/do DataStudio.
Neste caso preenchemos os dados de latitude e longitude dos endereços das empresas utilizando a [API de Geolocalizaçaõ do Google](https://developers.google.com/maps/documentation/geolocation/intro)

## Antes de executar o projeto
Antes de executar o projeto é necessário atualizar a base de dados do projeto anterior adicionando as colunas como abaixo:
```
alter table cnpj 
add column latitude decimal(11,8),
add column longitude decimal(11,8),
add column place_id varchar(255);
```

Para utilizar este projeto clone-o e execute, na primeira vez ele não vai encontrar o arquivo de configuração e então irá criá-lo, preencha com os dados que deseja e execute novamente.
# Para compilar e executar o projeto.
```
git clone https://github.com/murilotuvani/cnpjLatLong.git
cd cnpjLatLong
mvn package
java -jar target/cnpjLatLong.jar
```


O projeto [cnpj-writer](https://github.com/murilotuvani/cnpj-writer-springboot-mysql) é o responsável para criar a base de dados e inserir os dados.
O projeto [cnpj-reeader](https://github.com/murilotuvani/cnpj-reader) é responsável por processar os arquivos da receita federal e enviar estes dados ao cnpj-writer.

## Fazendo um backup da latitude, longitude e Google Place Id para uma nova importação dos CNPJs atualizados.
```
drop table if exists cnpj_lat_lng;
create table cnpj_lat_lng (
cnpj bigint(20) NOT NULL,
latitude decimal(11,8) DEFAULT NULL,
longitude decimal(11,8) DEFAULT NULL,
place_id varchar(255) DEFAULT NULL,
PRIMARY KEY (cnpj));

insert into cnpj.cnpj_lat_lng (select cnpj, latitude, longitude, place_id from cnpj.cnpj where latitude is not null);
```

## Como utilizar o [mysqldump](https://dev.mysql.com/doc/refman/8.0/en/mysqldump.html)
```
mysqldump cnpj cnpj_lat_lng -u [USUARIO] -h [SERVIDOR] -p > [ARQUIVO_DE_BACKUP]
mysqldump cnpj cnpj_lat_lng -u root -h 127.0.0.1 -P 3309 -p > 20200627cnpjLatLong.sql
```