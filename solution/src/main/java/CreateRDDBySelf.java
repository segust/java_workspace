import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;
import java.util.List;

public class CreateRDDBySelf {
    /**
     * 自己创建List<T>来建立JavaRDD，使用JavaSparkContext.parallelize(List<T>)方法完成转化
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSession.builder().master("local").appName("hello").getOrCreate();
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        JavaRDD<Integer> data = sc.parallelize(list);
        data.cache();//持久化到内存以便复用
        JavaRDD<Integer> map = data.map(a -> a);
        int count = map.reduce((a, b) -> a + b);
        System.out.println(count);

        spark.stop();
    }
}