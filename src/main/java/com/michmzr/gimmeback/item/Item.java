package com.michmzr.gimmeback.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.michmzr.gimmeback.model.audit.Auditable;
import com.michmzr.gimmeback.user.User;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Item extends Auditable<String> implements Serializable {
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

    @NotNull
    @ManyToOne
    protected User author;

    @JsonCreator
    public Item() {
    }
}
