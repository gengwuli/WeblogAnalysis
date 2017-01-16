package preprocess;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import utils.WeblogUtils;

/**
 * Web log Preprocess
 * @author gengwuli
 *
 */
public class WeblogPreprocess {
	/**
	 * Mapper must be static 
	 * @author gengwuli
	 *
	 */
	static class WeblogPreprocessMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
		//output key 
		Text k = new Text();
		//output value
		NullWritable v = NullWritable.get();
		//pages containing a list of predefined url
		Set<String> pages = new HashSet<>();

		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			pages.add("/about");
			pages.add("/about/");
			pages.add("/black-ip-list/");
			pages.add("/cassandra-clustor/");
			pages.add("/finance-rhive-repurchase/");
			pages.add("/hadoop-family-roadmap/");
			pages.add("/hadoop-hive-intro/");
			pages.add("/hadoop-zookeeper-intro/");
			pages.add("/hadoop-mahout-roadmap/");
			pages.add("/black-ip-list");
			pages.add("/cassandra-clustor");
			pages.add("/finance-rhive-repurchase");
			pages.add("/hadoop-family-roadmap");
			pages.add("/hadoop-hive-intro");
			pages.add("/hadoop-zookeeper-intro");
			pages.add("/hadoop-mahout-roadmap");
		}

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			//Parse one line of log to the WeblogBean
			WeblogBean weblogBean = WeblogParser.parse(line);
			if (weblogBean != null) {
				//filtering invalid log
				WeblogParser.filtStaticResource(weblogBean, pages);
				k.set(weblogBean.toString());
				//once sent the k will be serialized and the reference will not affect anymore
				context.write(k, v);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		//first get a configuration instance
		Configuration conf = new Configuration();
		//And get a job instance
		Job job = Job.getInstance(conf);

		//Set the class where the main class reside
		job.setJarByClass(WeblogPreprocess.class);

		//Set mapper class
		job.setMapperClass(WeblogPreprocessMapper.class);
		//set output key and value class
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		//---------below is local mode--------------------------
		//set input and output paths
//		FileInputFormat.setInputPaths(job, new Path("./input"));
//		//Delete output file if exist
//		WeblogUtils.deleteDir(new File("./output"));
//		FileOutputFormat.setOutputPath(job, new Path("./output"));
		//------------------------------------------------------
		//This will be run on hdfs
		 FileInputFormat.setInputPaths(job, new Path(args[0]));
		 FileOutputFormat.setOutputPath(job, new Path(args[1]));
		// No need to have reducer here
		job.setNumReduceTasks(0);

		boolean status = job.waitForCompletion(true);
		System.exit(status ? 0 : 1);
	}
}
