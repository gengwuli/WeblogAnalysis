package process;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import common.IConstant;

/**
 * PageVisitBean
 * @author gengwuli
 *
 */
public class PageVisitBean implements Writable{

	/**
	 * Session id
	 */
	private String sessionId;
	
	/**
	 * remote address
	 */
	private String remoteAddr;
	
	/**
	 * inTime
	 */
	private String inTime;
	
	/**
	 * Out time
	 */
	private String outTime;
	
	/**
	 * start page
	 */
	private String inPage;
	
	/**
	 * End page
	 */
	private String outPage;
	
	/**
	 * referrer
	 */
	private String httpReferal;
	
	/**
	 * page visits
	 */
	private int pageVisits;
	
	public void set(String sessionId, String remoteAddr, String inTime, String outTime, String inPage, String outPage, String httpReferal, int pageVisits) {
		this.sessionId = sessionId;
		this.remoteAddr = remoteAddr;
		this.inTime = inTime;
		this.outTime = outTime;
		this.inPage = inPage;
		this.outPage = outPage;
		this.httpReferal = httpReferal;
		this.pageVisits = pageVisits;
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

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getInPage() {
		return inPage;
	}

	public void setInPage(String inPage) {
		this.inPage = inPage;
	}

	public String getOutPage() {
		return outPage;
	}

	public void setOutPage(String outPage) {
		this.outPage = outPage;
	}

	public String getHttpReferal() {
		return httpReferal;
	}

	public void setHttpReferal(String httpReferal) {
		this.httpReferal = httpReferal;
	}

	public int getPageVisits() {
		return pageVisits;
	}

	public void setPageVisits(int pageVisits) {
		this.pageVisits = pageVisits;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(sessionId);
		out.writeUTF(remoteAddr);
		out.writeUTF(inTime);
		out.writeUTF(outTime);
		out.writeUTF(inPage);
		out.writeUTF(outPage);
		out.writeUTF(httpReferal);
		out.writeInt(pageVisits);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.sessionId = in.readUTF();
		this.remoteAddr = in.readUTF();
		this.inTime = in.readUTF();
		this.outTime = in.readUTF();
		this.inPage = in.readUTF();
		this.outPage = in.readUTF();
		this.httpReferal = in.readUTF();
		this.pageVisits = in.readInt();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.sessionId);
		sb.append(IConstant.DELIMITER).append(this.remoteAddr);
		sb.append(IConstant.DELIMITER).append(this.inTime);
		sb.append(IConstant.DELIMITER).append(this.outTime);
		sb.append(IConstant.DELIMITER).append(this.inPage);
		sb.append(IConstant.DELIMITER).append(this.outPage);
		sb.append(IConstant.DELIMITER).append(this.httpReferal);
		sb.append(IConstant.DELIMITER).append(this.pageVisits);
		return sb.toString();
	}

}
