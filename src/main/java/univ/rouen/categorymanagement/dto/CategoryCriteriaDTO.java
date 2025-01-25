package univ.rouen.categorymanagement.dto;

import java.time.LocalDate;

public class CategoryCriteriaDTO {

    private String search;       // Name-based search
    private LocalDate dateAfter;
    private LocalDate dateBefore;
    private Boolean isRoot;      // true => only root categories, false => only non-root, null => no filter

    // NEW: Filter by minimum or exact descendant count
    private Integer minDescendants;
    private Integer exactDescendants; // optional if you want exact matching

    // Getters & Setters
    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public LocalDate getDateAfter() { return dateAfter; }
    public void setDateAfter(LocalDate dateAfter) { this.dateAfter = dateAfter; }

    public LocalDate getDateBefore() { return dateBefore; }
    public void setDateBefore(LocalDate dateBefore) { this.dateBefore = dateBefore; }

    public Boolean getIsRoot() { return isRoot; }
    public void setIsRoot(Boolean root) { isRoot = root; }

    public Integer getExactDescendants() { return exactDescendants; }
    public void setExactDescendants(Integer exactDescendants) { this.exactDescendants = exactDescendants; }
}