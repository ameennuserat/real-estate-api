package com.graduation.realestateconsulting.trait;

import com.graduation.realestateconsulting.model.dto.request.ReportSearchCriteria;
import com.graduation.realestateconsulting.model.entity.Report;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ReportSpecification {

    public static Specification<Report> findByCriteria(ReportSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.reportedUserId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("reportedUser").get("id"), criteria.reportedUserId()));
            }
            if (criteria.reporterUserId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("reporterUser").get("id"), criteria.reporterUserId()));
            }
            if (criteria.categoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), criteria.categoryId()));
            }
            if (criteria.descriptionKeyword() != null && !criteria.descriptionKeyword().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("description"), "%" + criteria.descriptionKeyword() + "%"));
            }
            if (criteria.startDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), criteria.startDate().atStartOfDay()));
            }
            if (criteria.endDate() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("createdAt"), criteria.endDate().plusDays(1).atStartOfDay()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}