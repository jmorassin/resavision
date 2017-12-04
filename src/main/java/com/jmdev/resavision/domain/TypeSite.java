package com.jmdev.resavision.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A TypeSite.
 */
@Entity
@Table(name = "type_site")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "typesite")
public class TypeSite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @OneToMany(mappedBy = "typeSite")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TypeBoutique> listTypeBoutiques = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public TypeSite nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<TypeBoutique> getListTypeBoutiques() {
        return listTypeBoutiques;
    }

    public TypeSite listTypeBoutiques(Set<TypeBoutique> typeBoutiques) {
        this.listTypeBoutiques = typeBoutiques;
        return this;
    }

    public TypeSite addListTypeBoutique(TypeBoutique typeBoutique) {
        this.listTypeBoutiques.add(typeBoutique);
        typeBoutique.setTypeSite(this);
        return this;
    }

    public TypeSite removeListTypeBoutique(TypeBoutique typeBoutique) {
        this.listTypeBoutiques.remove(typeBoutique);
        typeBoutique.setTypeSite(null);
        return this;
    }

    public void setListTypeBoutiques(Set<TypeBoutique> typeBoutiques) {
        this.listTypeBoutiques = typeBoutiques;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TypeSite typeSite = (TypeSite) o;
        if (typeSite.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), typeSite.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TypeSite{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
