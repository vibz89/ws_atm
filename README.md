# ws_atm
An api services to showcase simple simulation of ATM process

---------------------------------
After running springboot application access the apis through swagger ui

URL : http://localhost:8080/swagger-ui.html

-----------------------------------
There are two services which will provide Balance details and withdrawal details

## Service 1 - checkBalance
get /atm/api/v1/checkBalance/{account}/{pin} 

provide account number and pin and access the service

Eg:- /atm/api/v1/checkBalance/{123456789}/{1234} 


## Service 2 - withdraw
post /atm/api/v1/withdraw 

Used to raise withdrawal request with json as requestbody which contains account number , pin and withdrawal amount

Eg:-  
{
  "account": "123456789",
  "amount": 200,
  "pin": "1234"
}


-------------------------------------

Data is stored in H2 DB with base details. We use 2 tables to store account details and currency details.

---------------------------------
