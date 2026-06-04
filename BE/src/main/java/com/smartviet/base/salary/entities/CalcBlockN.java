package com.smartviet.base.salary.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "calc_block_n")
public class CalcBlockN {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sync_id", nullable = false)
    private Long syncId;

    @Column(name = "apply_date", nullable = false)
    private LocalDate applyDate;

    @Column(name = "branch", length = 100)
    private String branch;

    @Column(name = "block_date")
    private LocalDate blockDate;

    @Column(name = "n_type", length = 50)
    private String nType;

    @Column(name = "result", length = 50)
    private String result;

    @Column(name = "open_date")
    private LocalDate openDate;

    @Column(name = "duplicate", length = 50)
    private String duplicate;

    @Column(name = "branche_site", length = 100)
    private String brancheSite;

    @Column(name = "days_delay_open")
    private Integer daysDelayOpen;

    @Column(name = "first_connect_block")
    private LocalDate firstConnectBlock;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
