package org.marker.mushroom.beans;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileUploadBean {

    private CommonsMultipartFile file;

    public CommonsMultipartFile getFile() {
        return file;
    }

    public void setFile(CommonsMultipartFile file) {
        this.file = file;
    }
}
