package cn.sessiontech.xcx.entity;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Transient;


@Data
@Entity(name = "t_sys_file")
public class TSysFile extends BaseEntity {
	private static final long serialVersionUID = 6907138438294310713L;
	/**
	 * 源文件文件名称
	 */
	private String fileName;
	/**
	 * 文件类型：1：图片；2：附件；
	 */
	private Integer fileType;
	/**
	 * 原文件路径
	 */
	private String filePath;
	/**
	 * 缩略图路径
	 */
	private String fileThumPath;
	/**
	 * 水印图路径
	 */
	private String fileWaterPath;

	/**
	 * 原图图片访问地址
	 */
	@Transient
	private String netPath;
	/**
	 * 缩率图访问地址
	 */
	@Transient
	private String netThumPath;

	@Transient
	private String downloadPath;



}