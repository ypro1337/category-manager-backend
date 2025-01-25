package univ.rouen.categorymanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> children = new HashSet<>();

    @Column(name = "descendants_count", nullable = false)
    private int descendantsCount = 0;

    public String getName() {
        return name;
    }
    public Long getId() {
        return id;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public Category getParent() {
        return parent;
    }
    public Set<Category> getChildren() {
        return children;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }
    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    public int getDescendantsCount() { return this.descendantsCount; }
    public void setDescendantsCount(int exactDescendants) { this.descendantsCount = exactDescendants; }


    public void addChild(Category child) {
        child.parent = this;
        children.add(child);
    }
    public void removeChild(Category child) {
        children.remove(child);
        child.parent = null;
    }


    // Constructors, getters, setters...
}