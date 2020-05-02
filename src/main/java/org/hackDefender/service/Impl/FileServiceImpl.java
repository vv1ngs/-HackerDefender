package org.hackDefender.service.Impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.hackDefender.service.FileService;
import org.hackDefender.util.FtpUtil;
import org.hackDefender.util.UUIDUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author vvings
 * @version 2020/5/1 20:19
 */
@Slf4j
@Service("fileService")
public class FileServiceImpl implements FileService {
    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String filExtionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUIDUtil.getUUID8() + "." + filExtionName;
        log.info("上传文件{},路径{}", uploadFileName, path);
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);
            FtpUtil.uploadFile(Lists.newArrayList(targetFile));
            targetFile.delete();
        } catch (IOException e) {
            log.error("上传文件异常");
            return null;
        }
        return targetFile.getName();
    }
}
