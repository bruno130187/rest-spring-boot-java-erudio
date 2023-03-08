# rest-spring-boot-java-erudio-server
Curso Spring Boot 3 RESTful Java Completo (backend)

# Link do Projeto Client em React.js, projeto que tem a view que consome o json gerado por este server

[https://github.com/bruno130187/rest-spring-boot-java-erudio-client]

## Após abrir o projeto com o a sua IDEA de preferência (Eclipse, Intellij, Spring Tools, Netbeans) basta rodar a aplicação que ficará disponível na porta 8090.

## Todas as versões dos módulos utilizados no projeto encontram-se no arquivo pom.xml

## O Banco de dados utilizado no projeto é o MSSQL Server 2019 Developer Edition que é Free e você baixa pelo link:

[https://softwaretested.com/file-library/file/sql2019-ssei-dev.exe-microsoft-corporation/]

## Para instalar o MSSQL Server 2019 Developer Edition siga o tutorial abaixo:

[https://www.visual-expert.com/EN/visual-expert-documentation/install-and-configure-visual-expert/sql-server-2019-installation-guide-visual-expert.html#:~:text=%3E%3E%20Download%20SQL%20Server%202019%20Developer%20Edition&text=Run%20the%20.exe%20file%20and,the%20ISO%20package%20file%20begins.]

O SQ Server roda na porta 1433 por padrão e o acesso ao banco de dados está no arquivo application.properties nas linhas:

spring.datasource.username e spring.datasource.password

## E para baixar o SQL Server Management Studio 18.12.1 clique abaixo:

[https://catalog.s.download.windowsupdate.com/d/msdownload/update/software/updt/2022/06/ssms-setup-enu_2d6b129259b4870fb12d735f8bcd34403950075c.exe]

## Para que o Flyway consiga criar as tabelas do projeto automaticamente você precisa criar uma nova database chamada "rest-spring-boot-erudio" no MSSQL usando o Management Studio. Só após isso você irá startar o projeto na sua IDEA.

## E para baixar o Postman que é o programa usado para testar os endpoints criados neste projeto backend clique abaixo:

[https://www.postman.com/downloads/]

## E abaixo está o link para você baixar toda a Collection do Postman usado neste projeto, basta importar com o Postman esta collection e lembre-se que para rodar um endpoint você tem que rodar primeiro o endpoint que se chama "http://localhost:8090/api/auth/signin (SIGNIN)" que fica na pasta "auth" para se autenticar no servidor via JWT:

[https://drive.google.com/file/d/1FPUGAd2eF4ERILmZOmUzZeUIKEvWmJ_a/view?usp=sharing]

Qualquer dúvida segue a fonte do projeto que foi feito seguindo um curso do Professor Leandro Costa da Udemy ou me chame pelo LinkedIn:

[https://www.udemy.com/course/restful-apis-do-0-a-nuvem-com-springboot-e-docker/]

Segue meu LinkedIn: [https://www.linkedin.com/in/bruno-araujo-oficial/]
