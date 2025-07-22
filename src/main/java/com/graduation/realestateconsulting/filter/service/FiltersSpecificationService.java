package com.graduation.realestateconsulting.filter.service;

import com.graduation.realestateconsulting.filter.dto.FilterItemRequestDto;
import com.graduation.realestateconsulting.filter.enums.GlobalOperator;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FiltersSpecificationService<T> {

//    public Specification<T> getSearchSpecification(FilterItemRequestDto request) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(request.getColumn()), request.getValue());
//    }

    public Specification<T> getSearchSpecification(List<FilterItemRequestDto> requests, GlobalOperator globalOperator) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (globalOperator == null || requests == null || requests.isEmpty()) {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }

            for (FilterItemRequestDto requestDto : requests) {

                switch (requestDto.getOperation()) {

                    case EQUAL:
                        Predicate equal = criteriaBuilder.equal(root.get(requestDto.getColumn()), requestDto.getValue());
                        predicates.add(equal);
                        break;

                    case LIKE:
                        Predicate like = criteriaBuilder.like(criteriaBuilder.lower(root.get(requestDto.getColumn())), "%" + requestDto.getValue().toLowerCase() + "%");
                        predicates.add(like);
                        break;

                    case IN:
                        String[] split = requestDto.getValue().split(",");
                        Predicate in = root.get(requestDto.getColumn()).in(Arrays.asList(split));
                        predicates.add(in);
                        break;

                    case GREATER_THAN:
                        Predicate greaterThan = criteriaBuilder.greaterThan(root.get(requestDto.getColumn()), requestDto.getValue());
                        predicates.add(greaterThan);
                        break;

                    case LESS_THAN:
                        Predicate lessThan = criteriaBuilder.lessThan(root.get(requestDto.getColumn()), requestDto.getValue());
                        predicates.add(lessThan);
                        break;

                    case BETWEEN:
                        //"10, 20"
                        String[] split1 = requestDto.getValue().split(",");
                        Predicate between = criteriaBuilder.between(root.get(requestDto.getColumn()), Long.parseLong(split1[0]), Long.parseLong(split1[1]));
                        predicates.add(between);
                        break;

                    case JOIN:
                        Predicate join = criteriaBuilder.equal(root.join(requestDto.getJoinTable()).get(requestDto.getColumn()), requestDto.getValue());
                        predicates.add(join);
                        break;

                    case CONCAT:
                        String[] columnSplit = requestDto.getColumn().split(",");
                        Expression<String> concatColumns = criteriaBuilder.concat(root.get(columnSplit[0]), criteriaBuilder.concat(" ",root.get(columnSplit[1])));
                        Predicate concat = criteriaBuilder.like(criteriaBuilder.lower(concatColumns), "%" + requestDto.getValue().toLowerCase() + "%");
                        predicates.add(concat);
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + requestDto.getOperation());
                }

            }

            if (globalOperator.equals(GlobalOperator.AND)) {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            } else {
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
    }

}