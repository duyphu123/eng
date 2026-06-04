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
@Table(name = "option_set")
public class OptionSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_set_id", nullable = false)
    private Long optionSetId;

    @Column(name = "code", length = 100)
    private String code;

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

}
