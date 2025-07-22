package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.filter.dto.FilterRequestDto;
import com.graduation.realestateconsulting.filter.dto.PageRequestDto;
import com.graduation.realestateconsulting.filter.service.FiltersSpecificationService;
import com.graduation.realestateconsulting.model.dto.request.FaqRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.entity.Faq;
import com.graduation.realestateconsulting.services.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/FAQs")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService service;

    private final FiltersSpecificationService<Faq> faqFilter;

    @PostMapping("/filter")
    public  ResponseEntity<?> filterFaq(@RequestBody FilterRequestDto request) {
        Specification<Faq> faqSpecification =  faqFilter.getSearchSpecification(request.getFilterItems(),request.getGlobalOperator());
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequest());
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.filterFaq(faqSpecification,pageable))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findAll(pageRequest)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> findAllByCategoryId(@PathVariable Long categoryId,
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findAllByCategoryId(pageRequest, categoryId)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findById(id)).build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody FaqRequest request) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.save(request)).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody FaqRequest request, @PathVariable Long id) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.update(id, request)).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        service.delete(id);
        GlobalResponse response = GlobalResponse.builder().status("success").data("Message => Faq deleted successfully").build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}