package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.FaqRequest;
import com.graduation.realestateconsulting.model.dto.response.FaqResponse;
import com.graduation.realestateconsulting.model.entity.Faq;
import com.graduation.realestateconsulting.model.mapper.FaqMapper;
import com.graduation.realestateconsulting.repository.FaqRepository;
import com.graduation.realestateconsulting.services.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqRepository repository;
    private final FaqMapper mapper;

    @Override
    public Page<FaqResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Page<FaqResponse> findAllByCategoryId(Pageable pageable,Long categoryId) {
        return repository.findAllByFaqCategoryId(pageable, categoryId).map(mapper::toDto);
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
    public FaqResponse update(Long id, FaqRequest request) {
        Faq faq = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Faq not found"));
        mapper.toEntity(faq,request);
        Faq saved = repository.save(faq);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<FaqResponse> filterFaq(Specification<Faq> faqSpecification, Pageable pageable) {
        return repository.findAll(faqSpecification, pageable).map(mapper::toDto);
    }
}