package ba.unsa.etf.nwt.ingredient_service.rest;

import ba.unsa.etf.nwt.ingredient_service.model.IngredientRecipeDTO;
import ba.unsa.etf.nwt.ingredient_service.service.IngredientRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/api/ingredientRecipes", produces = MediaType.APPLICATION_JSON_VALUE)
public class IngredientRecipeController {

    @Autowired
    private final IngredientRecipeService ingredientRecipeService;

    public IngredientRecipeController(final IngredientRecipeService ingredientRecipeService) {
        this.ingredientRecipeService = ingredientRecipeService;
    }

    @GetMapping
    public ResponseEntity<List<IngredientRecipeDTO>> getAllIngredientRecipes() {
        return ResponseEntity.ok(ingredientRecipeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientRecipeDTO> getIngredientRecipe(@PathVariable final UUID id) {
        return ResponseEntity.ok(ingredientRecipeService.get(id));
    }

    @PostMapping
    public ResponseEntity<UUID> createIngredientRecipe(
            @RequestBody @Valid final IngredientRecipeDTO ingredientRecipeDTO) {
        return new ResponseEntity<>(ingredientRecipeService.create(ingredientRecipeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateIngredientRecipe(@PathVariable final UUID id,
                                                         @RequestBody @Valid final IngredientRecipeDTO ingredientRecipeDTO) {
        ingredientRecipeService.update(id, ingredientRecipeDTO);
        return ResponseEntity.ok("Successfully updated!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIngredientRecipe(@PathVariable final UUID id) {
        ingredientRecipeService.delete(id);
        return ResponseEntity.ok("Successfully deleted!");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAll() {
        ingredientRecipeService.deleteAll();
        return ResponseEntity.ok("Successfully deleted all!");
    }
}
