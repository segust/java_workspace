package hadoop;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class HelloHDFS {
	public static void main(String[] args) throws Exception {

		/*
		 * //从HDFS中读取文件 URL.setURLStreamHandlerFactory(new
		 * FsUrlStreamHandlerFactory()); URL url=new
		 * URL("hdfs://192.168.56.100:9000/hello.txt"); InputStream
		 * in=url.openStream(); IOUtils.copyBytes(in,System.out,4096,true);
		 */

		// 使用hadoop提供的类FileSystem
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://192.168.56.100:9000");
		conf.set("dfs.replication", "2");
		FileSystem fileSystem = FileSystem.get(conf);

		/*
		 * boolean success = fileSystem.mkdirs(new Path("/zmg"));
		 * System.out.println(success);
		 * 
		 * // 文件或目录是否存在 success = fileSystem.exists(new Path("/hello.txt"));
		 * System.out.println(success);
		 * 
		 * // 删除 success = fileSystem.delete(new Path("/zmg"), true);
		 * System.out.println(success);
		 * 
		 * success = fileSystem.exists(new Path("/zmg"));
		 * System.out.println(success);
		 */
		// 传输文件
		/*
		 * FSDataOutputStream out = fileSystem.create(new Path("/text.data"),
		 * true); FileInputStream fis = new
		 * FileInputStream("L:/hadoop/jdk-8u91-linux-x64.rpm");
		 * IOUtils.copyBytes(fis, out, 4096, true);
		 */

		/*
		 * FSDataOutputStream out = fileSystem.create(new Path("/text.data"),
		 * true); FileInputStream in = new
		 * FileInputStream("L:/hadoop/jdk-8u91-linux-x64.rpm"); byte[] buf = new
		 * byte[4096]; int len = in.read(buf); while (len != -1) {
		 * out.write(buf, 0, len); len = in.read(buf); } in.close();
		 * out.close();
		 */

		// 列举文件目录
		FileStatus[] statuses = fileSystem.listStatus(new Path("/"));
		System.out.println(statuses.length);
		for (FileStatus status : statuses) {
			System.out.println(status.getPath());
			System.out.println(status.getPermission());
			System.out.println(status.getReplication());
		}
	}
}
