package com.michmzr.gimmeback.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.michmzr.gimmeback.model.audit.Auditable;
import com.michmzr.gimmeback.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
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
