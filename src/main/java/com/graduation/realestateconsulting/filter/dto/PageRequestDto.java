package com.graduation.realestateconsulting.filter.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Getter
@Setter
public class PageRequestDto {

    @Schema(defaultValue = "0")
    private Integer page = 0;
    @Schema(defaultValue = "10")
    private Integer size = 10;
    @Schema(defaultValue = "ASC")
    private Sort.Direction sort = Sort.Direction.ASC;
    @Schema(defaultValue = "id")
    private String sortByColumn = "id";

    public Pageable getPageable(PageRequestDto dto){
        if(dto == null){
            return PageRequest.of(this.page, this.size, this.sort, this.sortByColumn);
        }
        Integer page = Objects.nonNull(dto.getPage()) ? dto.getPage() : this.page;
        Integer size = Objects.nonNull(dto.getSize()) ? dto.getSize() : this.size;
        Sort.Direction sort = Objects.nonNull(dto.getSort()) ? dto.getSort() : this.sort;
        String sortByColumn = Objects.nonNull(dto.getSortByColumn()) ? dto.getSortByColumn() : this.sortByColumn;

        return PageRequest.of(page, size, sort, sortByColumn);
    }

}
