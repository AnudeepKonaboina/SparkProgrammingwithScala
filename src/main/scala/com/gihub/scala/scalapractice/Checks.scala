//check for an api in spark
spark - shell -- master local[*] -- packages "org.scalaj:scalaj-http_2.11:2.4.2"

import scalaj.http.Http

val result = Http("http://mock-test-server:1080/fireshots/credentialsFetchWebService").postData(s"""{"credential_id":"3","credential_type_id":"1"}""")
  .header("Content-Type", "application/json")
  .header("Authorization", "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJGSVJFU0hPVFNfQVBQTElDQVRJT04iLCJzZXNzaW9uX2lkIjoxMzgxNDM3MDYsImV4cCI6MTYyMjIyMjExOSwidXNlcklkIjoiYWswMTA3IiwiaWF0IjoxNjIyMDkyNTE5fQ.bFi3zONmUqgW2V2tGHsKWKQQbnRIfBAEnUnnLG4_z2hF3B4Y8Vf0mG_R5HD5SbhcyyMI7vPeFec4BwBNrJcA6urlEZK9dAbeOpKdGmWRJqeWmc3aBATvfQr1-5kSrVk5y2LdACWxMAObWFwcuKcZdnejlfjTwufvCmIMGFUaTQltXgg0oSDc4NTSS9DEBf_oNKq6JnWHOcG7LaKP022y4-UQv6yUoQ-LdlkhsxTXBqTQW01SG1K0OktL0xM_CZ8h3UpPOfYMQAg1gO9KmErttrPIj8qn164_KvXbKT8RJxeQI0HuXgF4db_QCVs8uPdcIesPIbHkCdp5CTfPFlCMsg")
  .header("Accept", "application/json")
result.asString.body



///sample test df      
import spark.implicits._

val someDF = Seq(
  (1, 1),
  (64, 64),
  (-27, 27)
).toDF("datastore_id", "table_id")


