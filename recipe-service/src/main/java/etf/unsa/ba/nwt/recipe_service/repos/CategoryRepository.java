package etf.unsa.ba.nwt.recipe_service.repos;

import etf.unsa.ba.nwt.recipe_service.domain.Category;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
