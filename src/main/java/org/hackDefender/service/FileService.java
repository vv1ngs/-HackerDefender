package org.hackDefender.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author vvings
 * @version 2020/5/1 20:13
 */
public interface FileService {
    String upload(MultipartFile file, String path);
}
