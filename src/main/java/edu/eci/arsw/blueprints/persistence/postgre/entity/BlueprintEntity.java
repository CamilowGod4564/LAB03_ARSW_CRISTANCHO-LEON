package edu.eci.arsw.blueprints.persistence.postgre.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blueprints", uniqueConstraints = {
        @UniqueConstraint(name = "uk_author_name", columnNames = {"author", "name"})
})
public class BlueprintEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "blueprint", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderColumn(name = "point_order")
    private List<PointEntity> points = new ArrayList<>();

    protected BlueprintEntity() {
    }

    public BlueprintEntity(String author, String name) {
        this.author = author;
        this.name = name;
    }

    public Long getId() { return id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<PointEntity> getPoints() { return points; }

    public void addPoint(PointEntity p) {
        p.setBlueprint(this);
        points.add(p);
    }
}