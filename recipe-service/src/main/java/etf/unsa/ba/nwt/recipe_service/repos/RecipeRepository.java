package etf.unsa.ba.nwt.recipe_service.repos;

import etf.unsa.ba.nwt.recipe_service.domain.Recipe;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
    @Query(value = "SELECT * FROM Recipe r where r.deleted = FALSE", nativeQuery = true)
    List<Recipe> getRecipes();
    @Query(value = "SELECT * FROM Recipe r where r.recipe_category_id=:recipeCategory  AND r.deleted = FALSE", nativeQuery = true)
    List<Recipe> getRecipesFromCategory(@Param("recipeCategory") String recipeCategory);
    @Query(value = "SELECT * FROM Recipe r where r.userid=:recipeUser  AND r.deleted = FALSE", nativeQuery = true)
    List<Recipe> getRecipesFromUser(@Param("recipeUser") String recipeUser);
    @Query(value = "SELECT * from Recipe r where r.id = :id  AND r.deleted = FALSE", nativeQuery = true)
    Optional<Recipe> find(@NonNull UUID id);
}
