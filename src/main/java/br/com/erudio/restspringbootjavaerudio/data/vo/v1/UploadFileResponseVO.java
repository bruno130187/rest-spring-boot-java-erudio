package br.com.erudio.restspringbootjavaerudio.data.vo.v1;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UploadFileResponseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private Long size;

    public UploadFileResponseVO() {
    }

    public UploadFileResponseVO(String fileName, String fileDownloadUri, String fileType, Long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }
}
