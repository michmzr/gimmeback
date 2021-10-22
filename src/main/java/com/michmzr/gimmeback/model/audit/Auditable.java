package com.michmzr.gimmeback.model.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt", "createdBy", "lastModifiedBy", "lastModifiedAt"},
        allowGetters = true
)
public abstract class Auditable<U> {
    @CreatedBy
    private U createdBy;

    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedBy
    private U lastModifiedBy;

    @LastModifiedDate
    private Timestamp lastModifiedAt;
}
