package process;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * PageViewBean
 * @author gengwuli
 *
 */
public class PageViewBean implements Writable {

	/**
	 * A random fake session id
	 */
	private String sessionId;
	
	/**
	 * remote address
	 */
	private String remoteAddr;
	
	/**
	 * time
	 */
	private String timeLocal;
	
	/**
	 * request
	 */
	private String request;
	
	/**
	 * Which step 
	 */
	private int step;
	
	/**
	 * Time interval of user activity
	 */
	private String interval;
	
	/**
	 * referrer
	 */
	private String httpReferal;
	
	/**
	 * remote user
	 */
	private String remoteUser;
	
	/**
	 * response bytes sent
	 */
	private String bytesSent;
	
	/**
	 * Status
	 */
	private String status;

	public void set(String sessionId, String remoteAddr, String remoteUser, String timeLocal, String request, int step, String interval,
			String httpReferal, String bytesSent, String status) {
		this.sessionId = sessionId;
		this.remoteAddr = remoteAddr;
		this.remoteUser = remoteUser;
		this.timeLocal = timeLocal;
		this.request = request;
		this.step = step;
		this.interval = interval;
		this.httpReferal = httpReferal;
		this.bytesSent = bytesSent;
		this.status = status;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getTimeLocal() {
		return timeLocal;
	}

	public void setTimeLocal(String timeLocal) {
		this.timeLocal = timeLocal;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getHttpReferal() {
		return httpReferal;
	}

	public void setHttpReferer(String httpReferer) {
		this.httpReferal = httpReferer;
	}

	public String getBodyBytesSent() {
		return bytesSent;
	}

	public void setBodyBytesSent(String bodyBytesSent) {
		this.bytesSent = bodyBytesSent;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(sessionId);
		out.writeUTF(remoteAddr);
		out.writeUTF(remoteUser);
		out.writeUTF(timeLocal);
		out.writeUTF(request);
		out.writeInt(step);
		out.writeUTF(interval);
		out.writeUTF(httpReferal);
		out.writeUTF(bytesSent);
		out.writeUTF(status);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.sessionId = in.readUTF();
		this.remoteAddr = in.readUTF();
		this.remoteUser = in.readUTF();
		this.timeLocal = in.readUTF();
		this.request = in.readUTF();
		this.step = in.readInt();
		this.interval = in.readUTF();
		this.httpReferal = in.readUTF();
		this.bytesSent = in.readUTF();
		this.status = in.readUTF();
	}

	public String getUserAgent() {
		return remoteUser;
	}

	public void setUserAgent(String userAgent) {
		this.remoteUser = userAgent;
	}

	public PageViewBean getCopy() {
		PageViewBean bean = new PageViewBean();
		bean.set(sessionId, remoteAddr, remoteUser, timeLocal, request, step, interval, httpReferal, bytesSent, status);
		return bean;
	}

}
