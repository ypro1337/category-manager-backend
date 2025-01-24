package univ.rouen.categorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import univ.rouen.categorymanagement.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Additional custom queries if needed
}