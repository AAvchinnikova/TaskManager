package hexlet.code.service;

import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.dto.labels.LabelDTO;
import hexlet.code.dto.labels.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class LabelService {

    private LabelRepository labelRepository;

    private LabelMapper labelMapper;

    public List<LabelDTO> getAll() {
        var result = labelRepository.findAll();
        return result.stream().map(labelMapper::map).toList();
    }

    public LabelDTO findById(long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user with id " + id + " not found"));
        return labelMapper.map(label);
    }

    public LabelDTO create(LabelCreateDTO data) {
        try {
            var label = labelMapper.map(data);
            labelRepository.save(label);
            return labelMapper.map(label);
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public LabelDTO update(long id, LabelUpdateDTO data) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user with id " + id + " not found"));
        labelMapper.update(data, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public void delete(long id) {
        labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user with id " + id + " not found"));
        labelRepository.deleteById(id);
    }
}
