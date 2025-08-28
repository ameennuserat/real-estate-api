package com.graduation.realestateconsulting.filter.service;

import com.graduation.realestateconsulting.filter.dto.FilterItemRequestDto;
import com.graduation.realestateconsulting.filter.enums.GlobalOperator;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
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

                    case LIKE_NAME:
                        Expression<String> getFullName = criteriaBuilder.concat(root.join(requestDto.getJoinTable()).get("firstName"), criteriaBuilder.concat(" ", root.join(requestDto.getJoinTable()).get("lastName")));
                        Predicate fullName = criteriaBuilder.like(criteriaBuilder.lower(getFullName), "%" + requestDto.getValue().toLowerCase() + "%");
                        predicates.add(fullName);
                        break;

                    case LIKE_PROPERTY_OFFICE_NAME:
                        Expression<String> getName = criteriaBuilder.concat(root.join("office").join("user").get("firstName"), criteriaBuilder.concat(" ", root.join("office").join("user").get("lastName")));
                        Predicate propertyOfficeName = criteriaBuilder.like(criteriaBuilder.lower(getName), "%" + requestDto.getValue().toLowerCase() + "%");
                        predicates.add(propertyOfficeName);
                        break;

                    case LIKE_TICKET_CLIENT_NAME:
                        Expression<String> getName1 = criteriaBuilder.concat(root.join("client").join("user").get("firstName"), criteriaBuilder.concat(" ", root.join("client").join("user").get("lastName")));
                        Predicate ticketClientName = criteriaBuilder.like(criteriaBuilder.lower(getName1), "%" + requestDto.getValue().toLowerCase() + "%");
                        predicates.add(ticketClientName);
                        break;

                    case EQUAL_RATE:
                        Path<Double> totalRate = root.get("totalRate");
                        Path<Double> rateCount = root.get("rateCount");
                        Expression<Integer> averageRate = criteriaBuilder.toInteger(
                                criteriaBuilder.quot(totalRate, rateCount)
                        );
                        Predicate rating = criteriaBuilder.equal(averageRate, requestDto.getValue());
                        predicates.add(rating);
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