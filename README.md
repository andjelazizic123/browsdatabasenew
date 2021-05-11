# Getting Started

### Front End
POSTMAN

### Start DOCKER
docker-compose -f docker-compose-db.yml up

### GET H2 Database
http://localhost:8081/brows-database/h2-console
username : sa
password : ""

####################################################################################
############################# EXAMPLES  ###########################################
####################################################################################


#### Create connection

POST: http://localhost:8081/brows-database/addConnection

 
##### -- first connection request
{
"name":"books_local",
"host":"localhost",
"port":"3307",
"database":"books_db",
"user":"root",
"password":"admin"
}

##### -- second connection request
{
"name":"books_local_test",
"host":"localhost",
"port":"3307",
"database":"books_db",
"user":"test",
"password":"testing"
}


##find All connection

http://localhost:8081/brows-database/findAllConnection


## update  connection

http://localhost:8081/brows-database/updateConnection

{
"name":"connection_local",
"host":"localhost",
"port":"3307",
"database":"books_db",
"user":"test",
"password":"testing"
}


### delete connection
http://localhost:8081/brows-database/deleteConnection?connectionName=connection_local


#### get all tables

http://localhost:8081/brows-database/getTables?connectionName=connection_local


#### get all columnDBS

http://localhost:8081/brows-database/getColumns?connectionName=connection_local&tableName=BOOK

## get a table's data
http://localhost:8081/brows-database/getTableData?connectionName=connection_local&tableName=BOOK

### get column statistic

http://localhost:8081/brows-database/getColumnStatistic?connectionName=connection_local&tableName=BOOK&columnName=price


### get table statistic
http://localhost:8081/brows-database/getTableStatistic?connectionName=connection_local&tableName=BOOK