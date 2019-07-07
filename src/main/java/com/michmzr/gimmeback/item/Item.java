package com.michmzr.gimmeback.item;

import com.michmzr.gimmeback.core.audit.Auditable;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Item extends Auditable<String>  implements Serializable {
    @Id
    @Getter
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    @Nullable
    private BigDecimal value;

    //todo currency

    @NotNull
    private ItemType type;
}
