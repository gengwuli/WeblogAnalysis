package preprocess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Parser for web log
 * 
 * @author gengwuli
 *
 */
public class WeblogParser {

	/**
	 * Input date format in the log
	 */
	private static SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);

	/**
	 * Output date format
	 */
	private static SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

	/**
	 * Parse one line of log
	 * 
	 * @param line
	 *            The line to be parsed
	 * @return The parsed WeblogBean
	 */
	public static WeblogBean parse(String line) {
		List<String> fields = parseFields(line);
		if (fields.size() < 9) {
			return null;
		}
		WeblogBean bean = new WeblogBean();
		bean.setRemote_addr(fields.get(0));
		bean.setRemote_user(fields.get(2));
		String date = parseInputDate(fields.get(3));
		if (date == null || date.length() == 0) {
			date = "-invalid-time";
			bean.setValid(false);
		}
		bean.setTime_local(date);
		try {
			// parse url
			bean.setRequest(fields.get(4).split(" ")[1]);
		} catch (Exception e) {
			bean.setRequest("-invalid-");
		}
		bean.setStatus(fields.get(5));
		bean.setBody_bytes_sent(fields.get(6));
		bean.setHttp_referer(fields.get(7));
		bean.setHttp_user_agent(fields.get(8));
		// get rid of error loggin
		if (Integer.parseInt(bean.getStatus()) >= 400) {
			bean.setValid(false);
		}
		return bean;
	}

	/**
	 * Parse one line to a list of fields containing every piece of information
	 * 
	 * @param s
	 *            The line to be parsed
	 * @return A list of information fields
	 */
	private static List<String> parseFields(String s) {
		char[] cs = s.toCharArray();
		int len = cs.length;
		List<String> list = new ArrayList<>();
		char[] map = new char[128];
		map[' '] = ' ';
		map['\"'] = '\"';
		map['['] = ']';
		for (int i = 0, j = 0; i < len; i++) {
			for (; i < len && (cs[i] == ' ' || cs[i] == '\"' || cs[i] == '['); i++)
				;
			char k = i == 0 ? ' ' : cs[i - 1];
			for (j = i; i < len && cs[i] != map[k]; i++)
				;
			list.add(String.valueOf(cs, j, i - j));
		}
		return list;
	}

	/*
	 * Fielter resources
	 */
	public static void filtStaticResource(WeblogBean bean, Set<String> pages) {
		if (!pages.contains(bean.getRequest())) {
			bean.setValid(false);
		}
	}

	/**
	 * Parse input date
	 * 
	 * @param date
	 *            Input date string
	 * @return The output date string
	 */
	private static String parseInputDate(String date) {
		try {
			return outputFormat.format(inputFormat.parse(date));
		} catch (ParseException e) {
			return null;
		}
	}
}
