import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local");

        JavaSparkContext ctx = new JavaSparkContext(sparkConf);

        JavaRDD<String> lines = ctx.textFile("I:\\Spark\\examples\\src\\main\\resources\\people.txt");

        JavaRDD<String> words = lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator());

        JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<String, Integer>(s, 1));

        JavaPairRDD<String, Integer> counts = ones.reduceByKey(((v1,v2) -> v1 + v2));


        List<Tuple2<String, Integer>> output = counts.collect();

        for (Tuple2<?, ?> tuple : output) {

            System.out.println(tuple._1() + ":" + tuple._2());

        }

        ctx.stop();
    }
}
