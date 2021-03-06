DROP TABLE IF EXISTS ACCDETAILS;
 
CREATE TABLE ACCDETAILS (
  accdetid INT AUTO_INCREMENT  PRIMARY KEY,
  accountnum VARCHAR(250) NOT NULL,
  pin VARCHAR(6) NOT NULL,
  balance BIGINT DEFAULT 0,
  overdraft BIGINT DEFAULT 0,
  dateCreated TIMESTAMP
);

DROP TABLE IF EXISTS CASHBOX;

CREATE TABLE CASHBOX (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  fifty BIGINT DEFAULT 0,
  twenty BIGINT DEFAULT 0,
  ten BIGINT DEFAULT 0,
  five BIGINT DEFAULT 0
);