package univ.rouen.categorymanagement.repository.criteria;

import java.time.LocalDateTime;

public class CategoryCriteria {

    private String search;       // Name-based search
    private LocalDateTime dateAfter;
    private LocalDateTime dateBefore;
    private Boolean isRoot;      // true => only root categories, false => only non-root, null => no filter

    // NEW: Filter by minimum or exact descendant count
    private Integer descendantsCount; // optional if you want exact matching

    // Getters & Setters
    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public LocalDateTime getDateAfter() { return dateAfter; }
    public void setDateAfter(LocalDateTime dateAfter) { this.dateAfter = dateAfter; }

    public LocalDateTime getDateBefore() { return dateBefore; }
    public void setDateBefore(LocalDateTime dateBefore) { this.dateBefore = dateBefore; }

    public Boolean getIsRoot() { return isRoot; }
    public void setIsRoot(Boolean root) { isRoot = root; }


    public Integer getDescendantsCount() { return this.descendantsCount; }
    public void setDescendantsCount(Integer exactDescendants) { this.descendantsCount = exactDescendants; }
}