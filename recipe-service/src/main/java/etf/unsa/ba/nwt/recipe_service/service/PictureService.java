package etf.unsa.ba.nwt.recipe_service.service;

import etf.unsa.ba.nwt.recipe_service.domain.Picture;
import etf.unsa.ba.nwt.recipe_service.model.PictureDTO;
import etf.unsa.ba.nwt.recipe_service.repos.PictureRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class PictureService {

    private final PictureRepository pictureRepository;

    public PictureService(final PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    public List<PictureDTO> findAll() {
        return pictureRepository.findAll()
                .stream()
                .map(picture -> mapToDTO(picture, new PictureDTO()))
                .collect(Collectors.toList());
    }

    public PictureDTO get(final Integer id) {
        return pictureRepository.findById(id)
                .map(picture -> mapToDTO(picture, new PictureDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Integer create(final PictureDTO pictureDTO) {
        final Picture picture = new Picture();
        mapToEntity(pictureDTO, picture);
        return pictureRepository.save(picture).getId();
    }

    public void update(final Integer id, final PictureDTO pictureDTO) {
        final Picture picture = pictureRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(pictureDTO, picture);
        pictureRepository.save(picture);
    }

    public void delete(final Integer id) {
        pictureRepository.deleteById(id);
    }

    private PictureDTO mapToDTO(final Picture picture, final PictureDTO pictureDTO) {
        pictureDTO.setId(picture.getId());
        pictureDTO.setPicData(picture.getPicData());
        return pictureDTO;
    }

    private Picture mapToEntity(final PictureDTO pictureDTO, final Picture picture) {
        picture.setPicData(pictureDTO.getPicData());
        return picture;
    }

}