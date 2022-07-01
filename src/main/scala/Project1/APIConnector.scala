package Project1

object APIConnector {

  def get(): Unit = {

    val data = requests.get(" https://data.cdc.gov/resource/9bhg-hcku.json")
    //    val hc = new org.apache.spark.sql.hive.HiveContext(sc)
    //    val result = hc.sql("SELECT * FROM projectone.salaries_extra;")
    //    result.show()


    val text = data.text()
    val json = ujson.read(text)
    os.write(os.pwd/"tmp.json",json)//test hdfs figure it out




  }


}
