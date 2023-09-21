package com.promemory.s3.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.promemory.global.exception.CustomException;
import com.promemory.global.exception.type.ErrorCode;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFileForProfile(MultipartFile file, String userEmail) {

        log.info("[uploadFileForProfile 시작]" + " userEmail : " + userEmail);

        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        String fileKey = "profile/" + userEmail;

        try {
            amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);
            amazonS3Client.setObjectAcl(bucket, fileKey, CannedAccessControlList.PublicRead);

            log.info("[uploadFileForProfile 완료]" + " userEmail : " + userEmail);
            return amazonS3Client.getUrl(bucket, fileKey).toString();

        } catch (SdkClientException | IOException e) {
            throw new CustomException(ErrorCode.FAILED_UPLOAD);
        }
    }


}
