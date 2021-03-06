package PageRank;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class PageRankDirver
{
    public static final int numNodes=5;  //节点数
    public static final int maxiter=10;   //最大收敛次数
	public static void main(String[] args) throws Exception
	{
		long count=0;  //缓存已经接近收敛的节点个数
		int it=1;
		int num=1;
		String input="/Graph/input/";
		String output="/Graph/output1";
		do{
			Job job=getPageRankJob(input, output);
			job.waitForCompletion(true);
			
			Counters counter = job.getCounters();
			count = counter.findCounter(PageRankJob.MidNodes.Reduce).getValue();
			
			
			input="/Graph/output"+it;
			it++;
			output="/Graph/output"+it;
			
		    Job job1=getDistrbuteJob(input,output);
		    job1.waitForCompletion(true);
		    
		    input="/Graph/output"+it;
			it++;
			output="/Graph/output"+it;
			
			if(num<maxiter)
			System.out.println("it:"+it+" "+count);
			num++;
		}while(count!=numNodes);

	}
	
	public static Job getPageRankJob(String inPath,String outPath) throws Exception
	{
		Configuration conf = new Configuration();
		@SuppressWarnings("deprecation")
		Job job=new Job(conf,"PageRank job");
		
		job.getConfiguration().setInt("NodeCount", numNodes);
	    job.getConfiguration().setBoolean("mapred.map.tasks.speculative.execution", false);
	    job.getConfiguration().setBoolean("mapred.reduce.tasks.speculative.execution", false);
	    
	    job.getConfiguration().set("PageRankMassPath", "/mass");
	    

		job.setJarByClass(PageRankDirver.class);
		
		job.setNumReduceTasks(5);

		job.setMapperClass(PageRankJob.PageRankMaper.class);
		job.setReducerClass(PageRankJob.PageRankJobReducer.class);
		job.setPartitionerClass(RangePartitioner.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		
		FileInputFormat.addInputPath(job, new Path(inPath));
		FileOutputFormat.setOutputPath(job, new Path(outPath));
		
		FileSystem.get(job.getConfiguration()).delete(new Path(outPath), true);//如果文件已存在删除
		
		return job;	
	}

	public static Job getDistrbuteJob(String inPath,String outPath) throws Exception
	{
		Configuration conf = new Configuration();
		@SuppressWarnings("deprecation")
		Job job=new Job(conf,"Ditribute job");
		
		double mass = Double.NEGATIVE_INFINITY;                //一下是读取dangling节点的PR值，将其分配到其他节点
	    FileSystem fs = FileSystem.get(conf);
	    for (FileStatus f : fs.listStatus(new Path("/mass/missMass")))
	    {
	      FSDataInputStream fin = fs.open(f.getPath());
	      mass = fin.readDouble();
	      fin.close();
	    }
	    job.getConfiguration().setFloat("MissingMass",(float)mass);
		job.getConfiguration().setInt("NodeCount", numNodes);
		job.getConfiguration().setInt("NodeCount", numNodes);
	    job.getConfiguration().setBoolean("mapred.map.tasks.speculative.execution", false);
	    job.getConfiguration().setBoolean("mapred.reduce.tasks.speculative.execution", false);
	    
	    job.getConfiguration().set("PageRankMassPath", "/mass");
	    

		job.setJarByClass(PageRankDirver.class);
		
		job.setNumReduceTasks(5);

		job.setMapperClass(PageRankJob.PageRankMaper.class);
		job.setReducerClass(PageRankJob.PageRankJobReducer.class);
		job.setPartitionerClass(RangePartitioner.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		
		FileInputFormat.addInputPath(job, new Path(inPath));
		FileOutputFormat.setOutputPath(job, new Path(outPath));
		
		FileSystem.get(job.getConfiguration()).delete(new Path(outPath), true);//如果文件已存在删除
		
		return job;	
	}
}
