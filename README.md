# challenge
## Run
mvn clean spring-boot:run
### Example:
#### Put new transaction
curl -H "Content-type: application/json" -X PUT -d '{"amount":100,"type":"car"}' http://localhost:8080/transactionservice/transaction/1
#### Get the transaction
curl localhost:8080/transactionservice/transaction/1
#### Get transaction list by type
curl localhost:8080/transactionservice/types/car
#### Get sum of linked transaction
curl localhost:8080/transactionservice/sum/1
