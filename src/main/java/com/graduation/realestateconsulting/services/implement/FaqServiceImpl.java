package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.FaqRequest;
import com.graduation.realestateconsulting.model.dto.response.FaqResponse;
import com.graduation.realestateconsulting.model.entity.Faq;
import com.graduation.realestateconsulting.model.mapper.FaqMapper;
import com.graduation.realestateconsulting.repository.FaqRepository;
import com.graduation.realestateconsulting.services.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqRepository repository;
    private final FaqMapper mapper;

    @Override
    public List<FaqResponse> findAll() {
        return mapper.toDtos(repository.findAll());
    }

    @Override
    public FaqResponse findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Faq not found"));
    }

    @Override
    public FaqResponse save(FaqRequest request) {
        Faq faq = mapper.toEntity(request);
        Faq saved = repository.save(faq);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}