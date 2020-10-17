package com.michmzr.gimmeback.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.michmzr.gimmeback.model.audit.Auditable;
import com.michmzr.gimmeback.user.User;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Item extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    @Nullable
    private BigDecimal value;

    @NotNull
    private ItemType type;

    @NotNull
    @ManyToOne
    protected User author;

    @JsonCreator
    public Item() {
    }
}
