import etf.unsa.ba.nwt.recipe_service.model.CategoryDTO;
import etf.unsa.ba.nwt.recipe_service.service.CategoryService;
import etf.unsa.ba.nwt.recipe_service.service.PictureService;
import etf.unsa.ba.nwt.recipe_service.service.RecipeService;
import etf.unsa.ba.nwt.recipe_service.service.StepService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:./application-test.properties")
@SpringBootTest(classes={etf.unsa.ba.nwt.recipe_service.RecipeServiceApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private StepService stepService;

    private UUID pictureID;
    private UUID picture;

    @BeforeEach
    public void setUpTest() {
        stepService.deleteAll();
        recipeService.deleteAll();
        categoryService.deleteAll();
        pictureService.deleteAll();

        try {
            File file = new File("src/main/java/etf/unsa/ba/nwt/recipe_service/image/image.jpg");
            FileInputStream fis = new FileInputStream(file);
            MockMultipartFile multipart = new MockMultipartFile("file", file.getName(), "image/jpeg", fis);
            pictureID=pictureService.create(multipart);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

   @Test
    public void createCategorySuccessTest() throws Exception{
        String name = "Category"+pictureID;
        mockMvc.perform(post("/api/categorys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"name\":\"%s\",\n" +
                                "    \"categoryPicture\":\"%s\"}", name, pictureID)))
                .andExpect(status().isCreated());
    }
/*    @Test
    public void createCategoryAlreadyExistTest() throws Exception{
        String name = "Category"+pictureID;

        try {
            File file = new File("src/main/java/etf/unsa/ba/nwt/recipe_service/image/image.jpg");
            FileInputStream fis = new FileInputStream(file);
            MockMultipartFile multipart = new MockMultipartFile("file", file.getName(), "image/jpeg", fis);
            picture=pictureService.create(multipart);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mockMvc.perform(post("/api/categorys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"name\":\"%s\",\n" +
                                "    \"categoryPicture\":\"%s\"}", name, picture)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", is("422 UNPROCESSABLE_ENTITY \"Category with this name already exists\"")));
    }*/
    @Test
    public void updateCategoryTest() throws Exception{
        UUID categoryID = categoryService.create(new CategoryDTO("Category"+pictureID, pictureID));
        try {
            File file = new File("src/main/java/etf/unsa/ba/nwt/recipe_service/image/image.jpg");
            FileInputStream fis = new FileInputStream(file);
            MockMultipartFile multipart = new MockMultipartFile("file", file.getName(), "image/jpeg", fis);
            picture=pictureService.create(multipart);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String name = "Category"+picture;

        mockMvc.perform(put(String.format("/api/categorys/%s", categoryID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"name\":\"%s\",\n" +
                                "    \"categoryPicture\":\"%s\"}", name, pictureID)))
                .andExpect(status().isOk());
    }
    @Test
    public void createCategoryValidationsBlank() throws Exception {
        mockMvc.perform(post("/api/categorys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"name\":\"\",\n" +
                                "    \"categoryPicture\":\"%s\"}", pictureID)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder("Category name is required!")))
                ;
    }

    @Test
    public void deleteCategorySuccess() throws Exception {
        UUID categoryID = categoryService.create(new CategoryDTO("TestCategory"+pictureID, pictureID));
        mockMvc.perform(delete(String.format("/api/categorys/%s", categoryID)))
                .andExpect(status().isOk());
    }

    @Test
    public void getCategoryByIdSuccess() throws Exception {
        UUID categoryID = categoryService.create(new CategoryDTO("TestCategory"+pictureID, pictureID));

        mockMvc.perform(get(String.format("/api/categorys/%s", categoryID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(categoryID.toString())));
    }
    @Test
    public void getCategoryByIdError() throws Exception {
        mockMvc.perform(get(String.format("/api/categorys/01011001-e012-1111-bd11-2c2a4faef0fc")))
                .andExpect(status().isNotFound());
    }
    @Test
    public void getCategoryByNameTest() throws Exception {
        String name = "TestCategory"+pictureID;
        UUID categoryID = categoryService.create(new CategoryDTO(name, pictureID));

        mockMvc.perform(get(String.format("/api/categorys/name?name=%s",name)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(categoryID.toString())));
    }
}
