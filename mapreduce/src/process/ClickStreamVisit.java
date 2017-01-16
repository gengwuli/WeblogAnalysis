package process;

import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import common.IConstant;
import utils.WeblogUtils;

/**
 * Click stream visit indicating the user activity
 * @author gengwuli
 *
 */
public class ClickStreamVisit {
	/**
	 * Mapper
	 * @author gengwuli
	 *
	 */
	static class ClickStreamVisitMapper extends Mapper<LongWritable, Text, Text, PageViewBean> {
		//output value
		PageViewBean bean = new PageViewBean();
		//output key
		Text k = new Text();

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			String[] fields = line.split(IConstant.DELIMITER);
			bean.set(fields[0], fields[1], fields[2], fields[3], fields[4], Integer.parseInt(fields[5]), fields[6],
					fields[7], fields[8], fields[9]);
			k.set(bean.getSessionId());
			context.write(k, bean);
		}
	}

	/**
	 * Reducer
	 * @author gengwuli
	 *
	 */
	static class ClickStreamVisitReducer extends Reducer<Text, PageViewBean, NullWritable, PageVisitBean> {

		@Override
		protected void reduce(Text key, Iterable<PageViewBean> values, Context context)
				throws IOException, InterruptedException {
			PageVisitBean visitBean = new PageVisitBean();
			//Need to sort according to visit step
			TreeSet<PageViewBean> set = new TreeSet<>((a, b) -> a.getStep() - b.getStep());
			for (PageViewBean pageViewBean : values) {
				//need to get a copy since pageViewBean is just a iterator
				set.add(pageViewBean.getCopy());
			}
			//Get first and last indicating when did the user start browsing and when did he end
			PageViewBean first = set.first(), last = set.last();
			visitBean.set(key.toString(), first.getRemoteAddr(), first.getTimeLocal(), last.getTimeLocal(),
					first.getRequest(), last.getRequest(), first.getHttpReferal(), set.size());
			context.write(NullWritable.get(), visitBean);
		}
	}

	public static void main(String[] args) throws Exception {
		//Set configuration
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);

		//set main class mapper and reducer class
		job.setJarByClass(ClickStreamVisit.class);
		job.setMapperClass(ClickStreamVisitMapper.class);
		job.setReducerClass(ClickStreamVisitReducer.class);

		//set mapper output key and value
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(PageViewBean.class);

		//set output key and value
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(PageVisitBean.class);

		//set input and output paths
//		FileInputFormat.setInputPaths(job, new Path("./output1"));
//		WeblogUtils.deleteDir(new File("./output2"));
//		FileOutputFormat.setOutputPath(job, new Path("./output2"));
//		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		boolean status = job.waitForCompletion(true);
		System.exit(status ? 0 : 1);

	}
}
