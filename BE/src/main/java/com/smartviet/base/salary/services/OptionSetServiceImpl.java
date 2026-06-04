package com.smartviet.base.salary.services;


import com.smartviet.base.salary.dto.OptionSetValueDTO;
import com.smartviet.base.salary.repositories.OptionSetValueRepository;
import com.smartviet.base.salary.services.iservice.OptionSetService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionSetServiceImpl implements OptionSetService {

    private final OptionSetValueRepository optionSetValueRepository;

    public OptionSetServiceImpl(OptionSetValueRepository optionSetValueRepository) {
        this.optionSetValueRepository = optionSetValueRepository;
    }

    @Override
    public List<OptionSetValueDTO> getOptionSetValues(String optionSetCode) {
        return null;
//        return optionSetValueRepository.findByOptionSetCode(optionSetCode, LocaleContextHolder.getLocale().getLanguage()).stream()
//                .map(this::parseOptionSetValueDTO).toList();
    }

//    private OptionSetValueDTO parseOptionSetValueDTO(OptionSetValue optionSetValue) {
//        return OptionSetValueDTO.builder()
//                .name(optionSetValue.getName())
//                .value(optionSetValue.getValue())
//                .language(optionSetValue.getLanguage())
//                .build();
//    }

}
