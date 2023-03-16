# User Register Fullstack - Backend
Projeto Backend feito em Java 11 com o CRUD que gerencia usuários num banco de dados MYSQL, em paralelo roda a mensageria Active MQ embedado do qual envia informações de log para o segundo microserviço User Register Fullstack - Backend Log responsavel pela gravação e gerenciamento dos logs.

# Gerar imagem no Docker
Necessário descomentar a primeira linha no application.properties para o maven utilizar as configurações do perfil docker.

No momento é necessário descomentar a variavel brokerUrl no JMSConfig.java pois a porta tcp deve ser outro valor e a anotação @Value do spring não funcionou nesse caso.

Tambem é necessário descomentar o responseEntity no método createLog do serviço UserServiceImpl.java pois tambem não funcionou o @Value.

Após isso gerar a imagem e utilizar a porta 9191:9191 para executar o container.