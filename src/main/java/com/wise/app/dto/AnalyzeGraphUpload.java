// 업로드 DTO
package com.wise.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class AnalyzeGraphUpload {

    @Schema(description = "분석할 이미지 파일", type = "string", format = "binary")
    private MultipartFile file;
}
