package process;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;

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
import preprocess.WeblogBean;
import utils.WeblogUtils;

/**
 * ClickStreamPageView recording user clicking behavior
 * @author gengwuli
 *
 */
public class ClickStreamPageView {
	/**
	 * Mapper
	 * @author gengwuli
	 *
	 */
	static class ClickStreamPageViewMapper extends Mapper<LongWritable, Text, Text, WeblogBean> {
		//output key
		Text k = new Text();
		//output value
		WeblogBean v = new WeblogBean();

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			String[] fields = line.split(IConstant.DELIMITER);
			if (fields.length < 9) {
				return;
			}
			v.set(fields[0].equals("true"), fields[1], fields[2], fields[3], fields[4], fields[5], fields[6], fields[7],
					fields[8]);
			if (v.isValid()) {
				//only write valid log out, set the key to remote address
				k.set(v.getRemote_addr());
				context.write(k, v);
			}
		}
	}

	/**
	 * Reducer
	 * @author gengwuli
	 *
	 */
	static class ClickStreamPageViewReducer extends Reducer<Text, WeblogBean, NullWritable, Text> {
		//Out put value
		Text v = new Text();
		
		//Weblog bean priority queue 
		Queue<WeblogBean> q;
		
		/**
		 * Need to some setup before moving on
		 */
		protected void setup(Context context) throws IOException ,InterruptedException {
			//Compare according to date
			q = new PriorityQueue<WeblogBean>((a, b) -> {
				try {
					return toDate(a.getTime_local()).compareTo(toDate(b.getTime_local()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			});
		};
		
		@Override
		protected void reduce(Text key, Iterable<WeblogBean> values, Context context)
				throws IOException, InterruptedException {
			for (WeblogBean bean : values) {
				//Need to get a copy cause bean is just a iterator, won't change
				q.offer(bean.getCopy());
			}
			int step = 1;
			//Generate a random fake session id
			String sessionId = UUID.randomUUID().toString();
			WeblogBean pre = q.poll();
			while (!q.isEmpty()) {
				WeblogBean cur = q.poll();
				long diff = 0;
				try {
					diff = timeDiff(toDate(cur.getTime_local()), toDate(pre.getTime_local()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				// If the difference is less than 30 minutes, indicating it's a continuous click
				if (diff < 30 * 60 * 1000) {
					v.set(buildValue(pre, sessionId, step, diff / 1000));
					context.write(NullWritable.get(), v);
					step++;
				} else {
					v.set(buildValue(pre, sessionId, step, 60));
					context.write(NullWritable.get(), v);
					step = 1;
					sessionId = UUID.randomUUID().toString();
				}
				pre = cur;
			}
			v.set(buildValue(pre, sessionId, step, 60));
			context.write(NullWritable.get(), v);
		}
	}
	
	/**
	 * Build value
	 * @param b web log bean
	 * @param session session id
	 * @param step which step
	 * @param timeDiff time difference
	 * @return the built value
	 */
	private static String buildValue(WeblogBean b, String session, int step, long timeDiff) {
		StringBuilder sb = new StringBuilder();
		sb.append(session);
		sb.append(IConstant.DELIMITER).append(b.getRemote_addr());
		sb.append(IConstant.DELIMITER).append(b.getRemote_user());
		sb.append(IConstant.DELIMITER).append(b.getTime_local());
		sb.append(IConstant.DELIMITER).append(b.getRequest());
		sb.append(IConstant.DELIMITER).append(step);
		sb.append(IConstant.DELIMITER).append(timeDiff);
		sb.append(IConstant.DELIMITER).append(b.getHttp_referer());
		sb.append(IConstant.DELIMITER).append(b.getHttp_user_agent());
		sb.append(IConstant.DELIMITER).append(b.getBody_bytes_sent());
		sb.append(IConstant.DELIMITER).append(b.getStatus());
		return sb.toString();
	}
	
	/**
	 * Get a time difference
	 * @param d1 date one
	 * @param d2 date two
	 * @return time difference
	 */
	private static long timeDiff(Date d1, Date d2) {
		return d1.getTime() - d2.getTime();
	}
	
	/**
	 * Get the date object from input date string
	 * @param date input date string
	 * @return The date object
	 * @throws ParseException if the date cannot be parsed
	 */
	private static Date toDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		return sdf.parse(date);
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		
		//Set main class mapper and reducer class
		job.setJarByClass(ClickStreamPageView.class);
		job.setMapperClass(ClickStreamPageViewMapper.class);
		job.setReducerClass(ClickStreamPageViewReducer.class);
		
		//set mapper output key and value
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(WeblogBean.class);
		
		//set output key and value
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		//set input and output paths
//		FileInputFormat.setInputPaths(job, new Path("./output"));
//		WeblogUtils.deleteDir(new File("./output1"));
//		FileOutputFormat.setOutputPath(job, new Path("./output1"));
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		boolean status = job.waitForCompletion(true);
		System.exit(status ? 0 : 1);
	}
}
