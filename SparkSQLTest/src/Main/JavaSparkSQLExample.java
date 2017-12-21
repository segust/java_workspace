package Main;

import java.sql.Timestamp;
// $example off:programmatic_schema$
// $example on:create_ds$
import java.io.Serializable;
import java.util.HashMap;
// $example off:create_ds$

// $example on:schema_inferring$
// $example on:programmatic_schema$
// $example off:programmatic_schema$
// $example on:create_ds$
// $example on:create_df$
// $example on:run_sql$
// $example on:programmatic_schema$
// $example off:programmatic_schema$
// $example off:create_df$
// $example off:run_sql$
// $example off:create_ds$
// $example off:schema_inferring$
// $example on:init_session$
import Bean.Vehicle;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
// $example off:init_session$
// $example on:programmatic_schema$
// $example off:programmatic_schema$


// $example on:untyped_ops$
// col("...") is preferable to df.col("...")

// $example off:untyped_ops$

public class JavaSparkSQLExample {
    // $example on:create_ds$

    // $example off:create_ds$

    public static void main(String[] args) throws AnalysisException {
        // $example on:init_session$
        SparkSession spark = SparkSession
                .builder().master("local")
                .appName("JavaSparkSQLtest")
                .config("spark.some.config.option", "some-value")
                .getOrCreate();
        // $example off:init_session$

        runInferSchemaExample(spark);

        spark.stop();
    }

    private static void runInferSchemaExample(SparkSession spark) {
        // $example on:schema_inferring$
        // Create an RDD of Person objects from a text file
        Encoder<Vehicle> vehicleEncoder = Encoders.bean(Vehicle.class);
        Dataset<Vehicle> test = spark.read().option("header", "true").csv("H:\\01\\01\\01\\2017-01-01.csv").as(vehicleEncoder);
        test.show();
//        JavaRDD<Vehicle> vehicleRDD = spark.read()
//                .textFile("H:/01/01/01/2017-01-01.csv")
//                .javaRDD()
//                .map(line -> {
//                    String[] parts = line.split(",");
//                    Vehicle vehicle = new Vehicle();
//                    vehicle.setLicense_no(parts[0]);
//                    vehicle.setPass_time(Timestamp.valueOf(parts[1]));
//                    vehicle.setPass_port(parts[2]);
//                    vehicle.setDevice(parts[3]);
//                    vehicle.setDirection(parts[4]);
//                    vehicle.setLicense_type(parts[5]);
//                    vehicle.setLicense_color(parts[6]);
//                    vehicle.setVehicle(parts[7]);
//                    vehicle.setVehicle_len(parts[8]);
//                    vehicle.setVehicle_type(parts[9]);
//                    vehicle.setSpeed(parts[10]);
//                    vehicle.setFeature_image(parts[11]);
//                    vehicle.setPanorama_image(parts[12]);
//                    vehicle.setPass_port_name(parts[13]);
//                    vehicle.setDirection_name(parts[14]);
//                    vehicle.setLane(parts[15]);
//                    vehicle.setLane_name(parts[16]);
//                    vehicle.setVehicle_body_color(parts[17]);
//                    vehicle.setVehicle_body_type(parts[18]);
//                    vehicle.setData_source(parts[19]);
//                    vehicle.setLat(parts[20]);
//                    vehicle.setLng(parts[21]);
//                    vehicle.setEtc1(parts[22]);
//                    vehicle.setEtc2(parts[23]);
//                    vehicle.setEtc3(parts[24]);
//                    return vehicle;
//                });
//
//        // Apply a schema to an RDD of JavaBeans to get a DataFrame
//        Dataset<Row> vehicleDF = spark.createDataFrame(vehicleRDD, Vehicle.class);
//        // Register the DataFrame as a temporary view
//        vehicleDF.createOrReplaceTempView("vehicle");
//        vehicleDF.show();
////        // SQL statements can be run by using the sql methods provided by spark
////        Dataset<Row> teenagersDF = spark.sql("SELECT name FROM people WHERE age BETWEEN 13 AND 19");
////
////        // The columns of a row in the result can be accessed by field index
////        Encoder<String> stringEncoder = Encoders.STRING();
////        Dataset<String> teenagerNamesByIndexDF = teenagersDF.map(
////                (MapFunction<Row, String>) row -> "Name: " + row.getString(0),
////                stringEncoder);
////        teenagerNamesByIndexDF.show();
////        // +------------+
////        // |       value|
////        // +------------+
////        // |Name: Justin|
////        // +------------+
////
////        // or by field name
////        Dataset<String> teenagerNamesByFieldDF = teenagersDF.map(
////                (MapFunction<Row, String>) row -> "Name: " + row.<String>getAs("name"),
////                stringEncoder);
////        teenagerNamesByFieldDF.show();
//        // +------------+
//        // |       value|
//        // +------------+
//        // |Name: Justin|
//        // +------------+
//        // $example off:schema_inferring$
    }
}