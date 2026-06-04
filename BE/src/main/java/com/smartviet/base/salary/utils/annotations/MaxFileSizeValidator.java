package com.smartviet.base.salary.utils.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class MaxFileSizeValidator implements ConstraintValidator<MaxFileSize, List<MultipartFile>> {

    private long maxSize;

    @Override
    public void initialize(MaxFileSize constraintAnnotation) {
        this.maxSize = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (files == null || files.isEmpty()) {
            return true;
        }
        for (MultipartFile file : files) {
            if (file.getSize() > maxSize) {
                return false;
            }
        }
        return true;
    }

}

