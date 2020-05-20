package cn.sessiontech.xcx.service.impl;

import cn.sessiontech.xcx.constant.GlobalTimeConstants;
import cn.sessiontech.xcx.entity.TSysFile;
import cn.sessiontech.xcx.properties.FileProperties;
import cn.sessiontech.xcx.service.TSysFileService;
import cn.sessiontech.xcx.service.base.BaseServiceImpl;
import cn.sessiontech.xcx.utils.DateJdk8Utils;
import cn.sessiontech.xcx.utils.GenerateUtils;
import cn.sessiontech.xcx.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.Objects;

/**
 * @author Administrator
 * @classname TSaasFileServiceImpl
 * @description 附件信息
 * @date 2019/8/12 14:18
 */
@Service
@Slf4j
public class TSysFileServiceImpl extends BaseServiceImpl<TSysFile> implements TSysFileService {
    @Autowired
    private FileProperties fileProperties;

    @Override
    public void deleteById(String id) {
        TSysFile tFile = this.findById(id);
        if(tFile!=null) {
            try {
                //删除数据库数据
                super.deleteById(id);
                //删除物理原文件
                File file = new File(fileProperties.getPhysicalPath()+tFile.getFilePath());
                file.delete();
                if(!Objects.isNull(tFile.getFileThumPath())){
                    //删除物理文件缩率图
                    file =  new File(fileProperties.getPhysicalPath()+tFile.getFileThumPath());
                    file.delete();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public TSysFile fileUpload(MultipartFile uploadFile, String folder){
        TSysFile f = new TSysFile();
        f.setId(GenerateUtils.generateUUID());
        //持久化文件数据
        String fileName = uploadFile.getOriginalFilename();
        //保存源文件名称
        f.setFileName(fileName);
        //保存在数据库中的相对文件目录路径 /company/时间/
        String catalog = folder+ DateJdk8Utils.getNowTime(GlobalTimeConstants.YYYYMMDD)+"/";
        //文件后缀
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        //原文件路径
        String filePath = catalog + f.getId() + "." + extension;
        //缩率图路径
        String thumPath = catalog+f.getId()+"-thumbnail."+ extension;
        if(ImageUtil.isImage(extension)){
            f.setFileThumPath(thumPath);
            //图片
            f.setFileType(1);
        }else{
            //附件
            f.setFileType(2);
        }
        f.setFilePath(filePath);
        f.setCreateTime(new Date());
        f.setUpdateTime(new Date());
        f = this.saveAndFlush(f);
        //将原图网络访问路径返回
        f.setNetPath(fileProperties.getNetworkPath()+filePath);
        //保存文件到服务器
        int size = (int) uploadFile.getSize();
        log.info("fileName:"+fileName+",size:"+size);
        File dest = new File(fileProperties.getPhysicalPath()+f.getFilePath());
        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try {
            //保存文件
            uploadFile.transferTo(dest);
            if(ImageUtil.isImage(extension)){
                //创建缩率图
                ImageUtil.generateThumbnail2Directory(0.5d,fileProperties.getPhysicalPath()+catalog,
                        fileProperties.getPhysicalPath()+filePath);
                //将缩率图网络路径返回
                f.setNetThumPath(fileProperties.getNetworkPath()+thumPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }


}
