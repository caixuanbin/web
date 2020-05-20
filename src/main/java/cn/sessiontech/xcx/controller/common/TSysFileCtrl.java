package cn.sessiontech.xcx.controller.common;

import cn.sessiontech.xcx.aspect.AnnotationLogs;
import cn.sessiontech.xcx.common.Result;
import cn.sessiontech.xcx.entity.TSysFile;
import cn.sessiontech.xcx.enums.ResultCodeEnum;
import cn.sessiontech.xcx.service.TSysFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Administrator
 * @classname TSaasFileCtrl
 * @description 文件管理类
 * @date 2019/8/12 14:22
 */
@Slf4j
@RestController
@RequestMapping("/sysFile")
public class TSysFileCtrl {
    @Autowired
    private TSysFileService tSysFileService;
    @Value("${file.networkPath}")
    private String networkPath;
    /**
     * @Description 删除文件信息
     * @param id 文件主键
     * @return
     * @data 2019/1/24 16:29
     * @Created by xbcai
     */
    @GetMapping(value = "/delete/{id}")
    @AnnotationLogs(description = "删除图片接口")
    public Result delete(@PathVariable(value = "id")String id) {
        if(StringUtils.isBlank(id)){
            log.error("图片ID为空，删除失败！！！");
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        tSysFileService.deleteById(id);
        log.info("图片ID："+id+"删除成功");
        return Result.success();
    }
    /**
     * 实现文件上传
     * */
    @PostMapping("/fileUpload")
    @AnnotationLogs(description = "上传图片接口")
    public Result fileUpload(@RequestParam("file") MultipartFile uploadFile,String folder){
        if(uploadFile.isEmpty()){
            log.error("上传图片流为空，上传失败");
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }else {
            String path = "/company/";
            if(StringUtils.isNotEmpty(folder)){
                path = path+folder+"/";
            }
            TSysFile file = tSysFileService.fileUpload(uploadFile,path);
            return Result.success(file);
        }
    }
}
