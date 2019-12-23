package io.mykit.weixin.vo;

import lombok.Data;

import java.io.Serializable;
@Data
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
	 * 团队名称
	 */
	private String teamName = "";
	
	/**
	 * 状态
	 * 1:正常
	 * 2：删除
	 */
	private Integer status = 1;


}
