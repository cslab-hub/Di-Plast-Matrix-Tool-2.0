/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
        name = "VALIDATIONREQUEST",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "recycled_id"})
)
public class ValidationRequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity requestFrom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recycled_id")
    private RecycledEntity recycled;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RecycledEntity getRecycled() {
        return recycled;
    }

    public void setRecycled(RecycledEntity recycled) {
        this.recycled = recycled;
    }

    public UserEntity getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(UserEntity requestFrom) {
        this.requestFrom = requestFrom;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.id;
        hash = 13 * hash + Objects.hashCode(this.requestFrom);
        hash = 13 * hash + Objects.hashCode(this.recycled);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValidationRequestEntity other = (ValidationRequestEntity) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.requestFrom, other.requestFrom)) {
            return false;
        }
        if (!Objects.equals(this.recycled, other.recycled)) {
            return false;
        }
        return true;
    }

    

}
