package io.mykit.weixin.vo;

import java.io.Serializable;

public class WechatQrcodeSubscribeInfo implements Serializable {
	private static final long serialVersionUID = -6776742644772835934L;
	private String id = "";
	
	/**
	 * 医生id
	 */
	private String docId = "";
	
	/**
	 * 医生联系方式
	 */
	private String docAccount = "";
	
	/**
	 * 医生姓名
	 */
	private String docName = "";
	
	
	/**
	 * 医院Id
	 */
	private String hospId = "";
	
	/**
	 * 医院名称
	 */
	private String hospName = "";
	
	
	/**
	 * 科室id
	 */
	private String secId = "";
	
	/**
	 * 科室名称
	 */
	private String secName = "";
	
	/**
	 * 指定关注后获取的团队id
	 */
	private String teamId = "";
	
	/**
	 * 状态
	 * 1:正常
	 * 2：删除
	 */
	private Integer status = 1;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getDocAccount() {
		return docAccount;
	}

	public void setDocAccount(String docAccount) {
		this.docAccount = docAccount;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getHospId() {
		return hospId;
	}

	public void setHospId(String hospId) {
		this.hospId = hospId;
	}

	public String getHospName() {
		return hospName;
	}

	public void setHospName(String hospName) {
		this.hospName = hospName;
	}

	public String getSecId() {
		return secId;
	}

	public void setSecId(String secId) {
		this.secId = secId;
	}

	public String getSecName() {
		return secName;
	}

	public void setSecName(String secName) {
		this.secName = secName;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
