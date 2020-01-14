package Entity;

import java.io.File;
import java.io.Serializable;

/**
 * 
 *
 *         an entity witch holdsa file and some details about it
 */
public class Document implements Serializable {

	private String FileID;
	private String fileName = null;
	private int size = 0;
	public byte[] mybytearray;
	private ChangeRequest request;

	public void initArray(int size) {
		mybytearray = new byte[size];
	}

	public Document(String fileName) {
		this.fileName = fileName;
	}

//input 
	public void setChangeRequest(ChangeRequest request) {
		this.request = request;
	}

	public void setFileID(String FileID) {
		this.FileID = FileID;
	}

	public void setByteArr(byte[] mybytearray) {
		this.mybytearray = mybytearray;
	}

	public void setMybytearray(byte[] mybytearray) {
		this.mybytearray =new byte[mybytearray.length];
		for (int i = 0; i < mybytearray.length; i++)
			this.mybytearray[i] = mybytearray[i];
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

// output
	public int getSize() {
		return size;
	}

	public String getFileID() {
		return FileID;
	}

	public byte[] getByteArr() {
		return mybytearray;
	}

	public String getFileName() {
		return fileName;
	}

	public byte[] getMybytearray() {
		return mybytearray;
	}

	public byte getMybytearray(int i) {
		return mybytearray[i];
	}
	public int getChangeRequestID() {
		 return request.getRequestID();
	}
	public ChangeRequest getChangeRequest() {
		 return request;
	}
	
}
