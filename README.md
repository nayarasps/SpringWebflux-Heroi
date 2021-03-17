<h2>Desenvolvimento de uma API REST para o gerenciamento de heróis.</h2>

API REST feita em Spring webflux para o gerenciamento de super heróis.
Métodos testados.
Banco de Dados utilizado: Dynamo - AWS


Executar dynamo: 

 java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb
 
 aws dynamodb list-tables --endpoint-url http://localhost:8000


swagger: http://localhost:8080/swagger-ui-heroes-reactive-api.html
