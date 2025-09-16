package com.spotify.user.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.spotify.user.constant.UserConstants;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners (AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class BaseEntity {

    @CreatedDate
    @Column (name="c_date",updatable = false)
    @JsonFormat (pattern = UserConstants.DATE_TIME_FORMAT_JSON)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name="c_user_id",updatable = false)
    private Integer createdBy;

    @LastModifiedDate
    @Column(name="u_date",insertable = false)
    @JsonFormat(pattern = UserConstants.DATE_TIME_FORMAT_JSON)
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name="u_user_id",insertable = false)
    private Integer updatedBy;

    @Column(name="status_id")
    private Integer statusId;
}
