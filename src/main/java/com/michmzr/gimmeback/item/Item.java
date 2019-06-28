package com.michmzr.gimmeback.item;

import com.michmzr.gimmeback.core.audit.Auditable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Item extends Auditable<String> {
    @Id
    @Getter
    @GeneratedValue
    private long id;

    @Getter
    @Setter
    @NotEmpty
    private String name;

    @Getter @Setter
    @Nullable
    private BigDecimal value;

    @Getter @Setter
    @NotNull
    private ItemType type;
}
