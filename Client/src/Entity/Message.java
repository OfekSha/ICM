package Entity;

import java.io.Serializable;

public class Message  implements Serializable {
	private int id;
	private String from;
	private String to;
	private String messege;
	
	
	/**  for DB
	 * @param id
	 * @param from
	 * @param to
	 * @param messege
	 */
	public Message(int id, String from, String to, String messege) {
	
		this.id = id;
		this.from = from;
		this.to = to;
		this.messege = messege;
	}
	// for client
	public Message(String from, String to, String messege) {
		this.from = from;
		this.to = to;
		this.messege = messege;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getMessege() {
		return messege;
	}
	public void setMessege(String messege) {
		this.messege = messege;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Message: from:" + from + ", to:" + to + "\n"
				+ " messege=" + messege + "\n\n";
	}

	
	
}// END of Messages class
