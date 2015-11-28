package com.ch.bean;
/**
 * 文件属性
 * @author ch
 *
 */
public class FileAtrribute {
	
	private int fileNum;
	//文件路径
	private String filePath;
	
	public FileAtrribute(int fileNum, String filePath) {
		this.fileNum = fileNum;
		this.filePath = filePath;
	}

	public int getFileNum() {
		return fileNum;
	}

	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
}
