package ba.unsa.etf.nwt.ingredient_service.service;

import ba.unsa.etf.nwt.ingredient_service.domain.Ingredient;
import ba.unsa.etf.nwt.ingredient_service.domain.IngredientRecipe;
import ba.unsa.etf.nwt.ingredient_service.model.IngredientRecipeDTO;
import ba.unsa.etf.nwt.ingredient_service.repos.IngredientRecipeRepository;
import ba.unsa.etf.nwt.ingredient_service.repos.IngredientRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class IngredientRecipeService {

    private final IngredientRecipeRepository ingredientRecipeRepository;
    private final IngredientRepository ingredientRepository;

    public IngredientRecipeService(final IngredientRecipeRepository ingredientRecipeRepository,
            final IngredientRepository ingredientRepository) {
        this.ingredientRecipeRepository = ingredientRecipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientRecipeDTO> findAll() {
        return ingredientRecipeRepository.findAll()
                .stream()
                .map(ingredientRecipe -> mapToDTO(ingredientRecipe, new IngredientRecipeDTO()))
                .collect(Collectors.toList());
    }

    public IngredientRecipeDTO get(final Integer id) {
        return ingredientRecipeRepository.findById(id)
                .map(ingredientRecipe -> mapToDTO(ingredientRecipe, new IngredientRecipeDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Integer create(final IngredientRecipeDTO ingredientRecipeDTO) {
        final IngredientRecipe ingredientRecipe = new IngredientRecipe();
        mapToEntity(ingredientRecipeDTO, ingredientRecipe);
        return ingredientRecipeRepository.save(ingredientRecipe).getId();
    }

    public void update(final Integer id, final IngredientRecipeDTO ingredientRecipeDTO) {
        final IngredientRecipe ingredientRecipe = ingredientRecipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(ingredientRecipeDTO, ingredientRecipe);
        ingredientRecipeRepository.save(ingredientRecipe);
    }

    public void delete(final Integer id) {
        ingredientRecipeRepository.deleteById(id);
    }

    private IngredientRecipeDTO mapToDTO(final IngredientRecipe ingredientRecipe,
            final IngredientRecipeDTO ingredientRecipeDTO) {
        ingredientRecipeDTO.setId(ingredientRecipe.getId());
        ingredientRecipeDTO.setQuantity(ingredientRecipe.getQuantity());
        ingredientRecipeDTO.setRecipeID(ingredientRecipe.getRecipeID());
        ingredientRecipeDTO.setIngredientRecipe(ingredientRecipe.getIngredientRecipe() == null ? null : ingredientRecipe.getIngredientRecipe().getId());
        return ingredientRecipeDTO;
    }

    private IngredientRecipe mapToEntity(final IngredientRecipeDTO ingredientRecipeDTO,
            final IngredientRecipe ingredientRecipe) {
        ingredientRecipe.setQuantity(ingredientRecipeDTO.getQuantity());
        ingredientRecipe.setRecipeID(ingredientRecipeDTO.getRecipeID());
        if (ingredientRecipeDTO.getIngredientRecipe() != null && (ingredientRecipe.getIngredientRecipe() == null || !ingredientRecipe.getIngredientRecipe().getId().equals(ingredientRecipeDTO.getIngredientRecipe()))) {
            final Ingredient ingredientRecipeP = ingredientRepository.findById(ingredientRecipeDTO.getIngredientRecipe())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ingredientRecipe not found"));
            ingredientRecipe.setIngredientRecipe(ingredientRecipeP);
        }
        return ingredientRecipe;
    }

}