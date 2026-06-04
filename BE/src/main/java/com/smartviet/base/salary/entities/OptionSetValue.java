package com.smartviet.base.salary.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "option_set_value")
public class OptionSetValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_set_value_id", nullable = false)
    private Long optionSetValueId;

    @Column(name = "option_set_id")
    private Long optionSetId;

    @Column(name = "value", length = 1000)
    private String value;

    @Column(name = "name", length = 512)
    private String name;

    @Column(name = "status", length = 1)
    private String status;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @Column(name = "create_user", length = 50)
    private String createUser;

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;

    @Column(name = "update_user", length = 50)
    private String updateUser;

    @Column(name = "display_order")
    private Long displayOrder;

}
