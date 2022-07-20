package Project1

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

object HDFS extends App {//discontinued for now

  def write(uri: String, filePath: String, data: Array[Byte]) = {
    System.setProperty("HADOOP_USER_NAME", "ogarcia2834")
    val path = new Path(filePath)
    val conf = new Configuration()
    conf.set("fs.defaultFS", uri)
    val fs = FileSystem.get(conf)
    val os = fs.create(path)
    os.write(data)
    fs.close()
  }
}