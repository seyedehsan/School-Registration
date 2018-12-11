package Entities;

import javax.persistence.*;

@Entity
@Table(name="access_level")
public class AccessLevel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private short id;

    @Column(name="access_type")
    private String accessType;

    public AccessLevel() {

    }

    public AccessLevel(String accessType) {

        this.accessType = accessType;
    }

    public short getId() {

        return id;
    }

    public String getAccessType() {

        return accessType;
    }

    public void setAccessType(String accessType) {

        this.accessType = accessType;
    }
}
