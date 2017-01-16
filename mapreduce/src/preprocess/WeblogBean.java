package preprocess;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import common.IConstant;

/**
 * Weblog bean stores the logging information
 * Standard Apache combined log output.
 * :remote-addr - :remote-user [:date] ":method :url HTTP/:http-version" :status :res[content-length] ":referrer" ":user-agent"
 * ::ffff:172.16.249.1 - - [14/Jan/2017:02:03:27 +0000] "GET /hadoop-hive-intro HTTP/1.1" 200 31 "http://localhost:8118/" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36"
 * @author gengwuli
 *
 */
public class WeblogBean implements Writable {

	/**
	 * Indicating whether this log is valid
	 */
	private boolean isValid = true;
	
	/**
	 * remote-addr
	 */
	private String remote_addr;
	
	/**
	 * remote-user
	 */
	private String remote_user;
	
	/**
	 * date
	 */
	private String time_local;
	
	/**
	 * request with a format of ":method :url HTTP/:http-version"
	 */
	private String request;
	
	/**
	 * status
	 */
	private String status;
	
	/**
	 * response content length :res[content-length]
	 */
	private String body_bytes_sent;
	
	/**
	 * referer ":referrer" who initiate the request
	 */
	private String http_referer;
	
	/**
	 * User agent
	 */
	private String http_user_agent;

	/**
	 * Set as a whole 
	 * @param isValid isValid
	 * @param remote_addr remote_addr
	 * @param remote_user remote_user
	 * @param time_local time_local
	 * @param request request
	 * @param status status
	 * @param body_bytes_sent body_bytes_sent
	 * @param http_referer http_referer
	 * @param http_user_agent http_user_agent
	 */
	public void set(boolean isValid, String remote_addr, String remote_user, String time_local, String request,
			String status, String body_bytes_sent, String http_referer, String http_user_agent) {
		this.isValid = isValid;
		this.remote_addr = remote_addr;
		this.remote_user = remote_user;
		this.time_local = time_local;
		this.request = request;
		this.status = status;
		this.body_bytes_sent = body_bytes_sent;
		this.http_referer = http_referer;
		this.http_user_agent = http_user_agent;
	}

	/**
	 * Get isValid
	 * @return
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * Set isValid
	 * @param isValid  the isValid to be set
	 */
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	/**
	 * get remote-user
	 * @return remote-user
	 */
	public String getRemote_user() {
		return remote_user;
	}

	/**
	 * set remote-user
	 * @param remote_user remote_user
	 */
	public void setRemote_user(String remote_user) {
		this.remote_user = remote_user;
	}

	/**
	 * get time_local
	 * @return time_local
	 */
	public String getTime_local() {
		return time_local;
	}

	/**
	 * set time_local
	 * @param time_local time_local
	 */
	public void setTime_local(String time_local) {
		this.time_local = time_local;
	}

	/**
	 * get request
	 * @return request
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * set request
	 * @param request request
	 */
	public void setRequest(String request) {
		this.request = request;
	}

	/**
	 * get status 
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * set status
	 * @param status status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * get response content length
	 * @return body_bytes_sent
	 */
	public String getBody_bytes_sent() {
		return body_bytes_sent;
	}

	/**
	 * set body_bytes_sent
	 * @param body_bytes_sent body_bytes_sent
	 */
	public void setBody_bytes_sent(String body_bytes_sent) {
		this.body_bytes_sent = body_bytes_sent;
	}

	/**
	 * get referrer
	 * @return http_referer
	 */
	public String getHttp_referer() {
		return http_referer;
	}

	/**
	 * set http_referer
	 * @param http_referer http_referer
	 */
	public void setHttp_referer(String http_referer) {
		this.http_referer = http_referer;
	}

	/**
	 * get http_user_agent
	 * @return http_user_agent
	 */
	public String getHttp_user_agent() {
		return http_user_agent;
	}

	/**
	 * set http_user_agent
	 * @param http_user_agent http_user_agent
	 */
	public void setHttp_user_agent(String http_user_agent) {
		this.http_user_agent = http_user_agent;
	}

	/**
	 * get remote_addr
	 * @return remote_addr
	 */
	public String getRemote_addr() {
		return remote_addr;
	}

	/**
	 * set remote_addr
	 * @param remote_addr remote_addr
	 */
	public void setRemote_addr(String remote_addr) {
		this.remote_addr = remote_addr;
	}

	/**
	 * Override function from Writable, used in serialization
	 */
	@Override
	public void readFields(DataInput in) throws IOException {
		this.isValid = in.readBoolean();
		this.remote_addr = in.readUTF();
		this.remote_user = in.readUTF();
		this.time_local = in.readUTF();
		this.request = in.readUTF();
		this.status = in.readUTF();
		this.body_bytes_sent = in.readUTF();
		this.http_referer = in.readUTF();
		this.http_user_agent = in.readUTF();
	}

	/**
	 * Override function from Writable, used in serialization
	 * the order should be the same with readFields
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeBoolean(this.isValid);
		out.writeUTF(remote_addr == null ? "" : this.remote_addr);
		out.writeUTF(remote_user == null ? "" : this.remote_user);
		out.writeUTF(time_local == null ? "" : this.time_local);
		out.writeUTF(request == null ? "" : this.request);
		out.writeUTF(status == null ? "" : this.status);
		out.writeUTF(body_bytes_sent == null ? "" : this.body_bytes_sent);
		out.writeUTF(http_referer == null ? "" : this.http_referer);
		out.writeUTF(http_user_agent == null ? "" : this.http_user_agent);
	}

	/**
	 * Override string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.isValid);
		sb.append(IConstant.DELIMITER).append(this.remote_addr);
		sb.append(IConstant.DELIMITER).append(this.remote_user);
		sb.append(IConstant.DELIMITER).append(this.time_local);
		sb.append(IConstant.DELIMITER).append(this.request);
		sb.append(IConstant.DELIMITER).append(this.status);
		sb.append(IConstant.DELIMITER).append(this.body_bytes_sent);
		sb.append(IConstant.DELIMITER).append(this.http_referer);
		sb.append(IConstant.DELIMITER).append(this.http_user_agent);
		return sb.toString();
	}
	
	/**
	 * Get a new copy of this instance
	 * @return a new copy
	 */
	public WeblogBean getCopy() {
		WeblogBean bean = new WeblogBean();
		bean.set(isValid, remote_addr, remote_user, time_local, request, status, body_bytes_sent, http_referer, http_user_agent);
		return bean;
	}
}
