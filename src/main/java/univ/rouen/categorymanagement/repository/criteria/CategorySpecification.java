package univ.rouen.categorymanagement.repository.criteria;

import org.springframework.data.jpa.domain.Specification;
import univ.rouen.categorymanagement.entity.Category;

import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategorySpecification implements Specification<Category> {

    private final CategoryCriteria criteria;

    public CategorySpecification(CategoryCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // 1) search => name like '%search%'
        if (criteria.getSearch() != null && !criteria.getSearch().isEmpty()) {
            String lowerSearch = criteria.getSearch().toLowerCase();
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + lowerSearch + "%"));
        }

        // 2) isRoot => parent is null / not null
        if (criteria.getIsRoot() != null) {
            if (Boolean.TRUE.equals(criteria.getIsRoot())) {
                predicates.add(cb.isNull(root.get("parent")));
            } else {
                predicates.add(cb.isNotNull(root.get("parent")));
            }
        }

        if (criteria.getDateAfter() != null) {
            LocalDateTime after = criteria.getDateAfter(); // Use directly if already LocalDateTime
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), after));
        }

// 4) dateBefore => createdAt < dateBefore + 1 day
        if (criteria.getDateBefore() != null) {
            LocalDateTime before = criteria.getDateBefore(); // Add 1 day to LocalDateTime
            predicates.add(cb.lessThan(root.get("createdAt"), before));
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}