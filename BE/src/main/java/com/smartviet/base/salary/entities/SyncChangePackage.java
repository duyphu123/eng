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
@Table(name = "sync_change_package")
public class SyncChangePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sync_date", nullable = false)
    private LocalDate syncDate;

    @Column(name = "isdn", length = 50)
    private String isdn;

    @Column(name = "branch", length = 100)
    private String branch;

    @Column(name = "code_sale", length = 50)
    private String codeSale;

    @Column(name = "channel", length = 100)
    private String channel;

    @Column(name = "old_package", length = 100)
    private String oldPackage;

    @Column(name = "new_package", length = 100)
    private String newPackage;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

}
