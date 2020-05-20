package cn.sessiontech.xcx.service;

import cn.sessiontech.xcx.entity.TSysFile;
import cn.sessiontech.xcx.service.base.BaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Administrator
 * @classname TSaasFileService
 * @description 附件信息
 * @date 2019/8/12 14:17
 */
public interface TSysFileService extends BaseService<TSysFile> {
    /**
     * 上传文件（保存文件到本地磁盘、持久化文件数据）
     * @param uploadFile 上传文件
     * @param folder 存放文件的文件夹
     */
    TSysFile fileUpload(MultipartFile uploadFile, String folder);

    @Override
    void deleteById(String id);
}
