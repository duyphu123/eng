package com.smartviet.base.salary.dto.requests.search;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommonSearch {
    String groupFilter;
    Integer page;
    Integer size;
    Boolean fullData;
}
