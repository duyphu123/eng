package com.smartviet.base.salary.utils.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class InFileTypeValidator implements ConstraintValidator<InFileType, List<MultipartFile>> {

    private List<String> allowedTypes;

    @Override
    public void initialize(InFileType constraintAnnotation) {
        this.allowedTypes = Arrays.asList(constraintAnnotation.allowedTypes());
    }

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (files == null || files.isEmpty()) {
            return true;
        }
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if (StringUtils.isBlank(fileName) || !isValidFileType(fileName)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidFileType(String fileName) {
        String extension = getFileExtension(fileName);
        return allowedTypes.contains(extension.toLowerCase());
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

}
