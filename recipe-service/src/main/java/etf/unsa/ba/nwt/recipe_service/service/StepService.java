package etf.unsa.ba.nwt.recipe_service.service;

import etf.unsa.ba.nwt.recipe_service.domain.Picture;
import etf.unsa.ba.nwt.recipe_service.domain.Recipe;
import etf.unsa.ba.nwt.recipe_service.domain.Step;
import etf.unsa.ba.nwt.recipe_service.model.RecipeDTO;
import etf.unsa.ba.nwt.recipe_service.model.StepDTO;
import etf.unsa.ba.nwt.recipe_service.repos.PictureRepository;
import etf.unsa.ba.nwt.recipe_service.repos.RecipeRepository;
import etf.unsa.ba.nwt.recipe_service.repos.StepRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class StepService {

    private final StepRepository stepRepository;
    private final PictureRepository pictureRepository;
    private final RecipeRepository recipeRepository;

    public StepService(final StepRepository stepRepository,
            final PictureRepository pictureRepository, final RecipeRepository recipeRepository) {
        this.stepRepository = stepRepository;
        this.pictureRepository = pictureRepository;
        this.recipeRepository = recipeRepository;
    }

    public List<StepDTO> findAll() {
        return stepRepository.findAll()
                .stream()
                .map(step -> mapToDTO(step, new StepDTO()))
                .collect(Collectors.toList());
    }

    public StepDTO get(final UUID id) {
        return stepRepository.findById(id)
                .map(step -> mapToDTO(step, new StepDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public List<StepDTO> getStepsForRecipe(UUID id) {
        return stepRepository.getStepsForRecipe(id.toString())
                .stream()
                .map(step -> mapToDTO(step, new StepDTO()))
                .collect(Collectors.toList());
    }
    public StepDTO getStepXForRecipe(final UUID id, final Integer oNumber) {
        return stepRepository.getStepXForRecipe(id.toString(), oNumber)
                .map(step -> mapToDTO(step, new StepDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public UUID create(final StepDTO stepDTO) {
        final Step step = new Step();
        mapToEntity(stepDTO, step);
        return stepRepository.save(step).getId();
    }

    public void update(final UUID id, final StepDTO stepDTO) {
        final Step step = stepRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(stepDTO, step);
        stepRepository.save(step);
    }

    public void delete(final UUID id) {
        stepRepository.deleteById(id);
    }
    public void deleteAll() {
        stepRepository.deleteAll();
    }

    private StepDTO mapToDTO(final Step step, final StepDTO stepDTO) {
        stepDTO.setId(step.getId());
        stepDTO.setDescription(step.getDescription());
        stepDTO.setONumber(step.getONumber());
        stepDTO.setStepPicture(step.getStepPicture() == null ? null : step.getStepPicture().getId());
        stepDTO.setStepRecipe(step.getStepRecipe() == null ? null : step.getStepRecipe().getId());
        return stepDTO;
    }

    private Step mapToEntity(final StepDTO stepDTO, final Step step) {
        step.setDescription(stepDTO.getDescription());
        step.setONumber(stepDTO.getONumber());
        if (stepDTO.getStepPicture() != null && (step.getStepPicture() == null || !step.getStepPicture().getId().equals(stepDTO.getStepPicture()))) {
            final Picture stepPicture = pictureRepository.findById(stepDTO.getStepPicture())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "stepPicture not found"));
            step.setStepPicture(stepPicture);
        }
        if (stepDTO.getStepRecipe() != null && (step.getStepRecipe() == null || !step.getStepRecipe().getId().equals(stepDTO.getStepRecipe()))) {
            final Recipe stepRecipe = recipeRepository.findById(stepDTO.getStepRecipe())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "stepRecipe not found"));
            step.setStepRecipe(stepRecipe);
        }
        return step;
    }

}
