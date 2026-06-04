package com.smartviet.base.salary.services.iservice;


import com.smartviet.base.salary.dto.OptionSetValueDTO;

import java.util.List;

public interface OptionSetService {

    List<OptionSetValueDTO> getOptionSetValues(String optionSetCode);

}
