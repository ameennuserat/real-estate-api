package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.FaqCategoryRequest;
import com.graduation.realestateconsulting.model.dto.response.FaqCategoryResponse;
import com.graduation.realestateconsulting.model.entity.FaqCategory;
import com.graduation.realestateconsulting.model.mapper.FaqCategoryMapper;
import com.graduation.realestateconsulting.repository.FaqCategoryRepository;
import com.graduation.realestateconsulting.services.FaqCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqCategoryServiceImpl implements FaqCategoryService {

    private final FaqCategoryRepository repository;
    private final FaqCategoryMapper mapper;

    @Cacheable("faqCategoriesList")
    @Override
    public List<FaqCategoryResponse> findAll() {
        return mapper.toDtos(repository.findAll());
    }

    @Cacheable(value = "faqCategories" , key = "#id")
    @Override
    public FaqCategoryResponse findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Faq Category not found"));
    }

    @Caching(
            put = { @CachePut(value = "faqCategories", key = "#result.id") },
            evict = { @CacheEvict(value = "faqCategoriesList", allEntries = true) }
    )
    @Override
    public FaqCategoryResponse save(FaqCategoryRequest request) {
        FaqCategory faqCategory = mapper.toEntity(request);
        FaqCategory saved = repository.save(faqCategory);
        return mapper.toDto(saved);
    }


    @Caching(
            put = { @CachePut(value = "faqCategories", key = "#id") },
            evict = { @CacheEvict(value = "faqCategoriesList", allEntries = true) }
    )
    @Override
    public FaqCategoryResponse update(Long id, FaqCategoryRequest request) {
        FaqCategory faqCategory = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Faq Category not found"));
        mapper.toEntity(faqCategory,request);
        FaqCategory saved = repository.save(faqCategory);
        return mapper.toDto(saved);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "faqCategories", key = "#id"),
                    @CacheEvict(value = "faqCategoriesList", allEntries = true)
            }
    )
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}