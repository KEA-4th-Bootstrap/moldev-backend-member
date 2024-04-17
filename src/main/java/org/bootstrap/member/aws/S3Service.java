package org.bootstrap.member.aws;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.bootstrap.member.common.error.FileNotFoundException;
import org.bootstrap.member.common.error.FileUploadErrorException;
import org.bootstrap.member.common.error.GlobalErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Component
public class S3Service {
    private static final Logger log = LoggerFactory.getLogger(S3Service.class);
    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String fileName, @Nullable String directoryName) {
        try (InputStream inputStream = file.getInputStream()) {
            String fullPath = combineDirectoryAndFileName(directoryName, fileName);
            S3Resource resource = s3Template.upload(bucketName, fullPath, inputStream);
            return resource.getURL().toString();
        } catch (IOException e) {
            throw new FileUploadErrorException(GlobalErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    public String getFileUrl(String key) {
        try {
            return s3Template.download(bucketName, key).getURL().toString();
        } catch (IOException e) {
            throw new FileNotFoundException(GlobalErrorCode.FILE_NOT_FOUND);
        }
    }

    private String combineDirectoryAndFileName(String directory, String fileName) {
        return directory + "/" + fileName;
    }

}