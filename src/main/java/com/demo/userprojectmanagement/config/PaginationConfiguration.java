package com.demo.userprojectmanagement.config;

import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PaginationConfiguration<T> {
  List<T> content;
  CustomPageable pageable;

  public PaginationConfiguration(Page<T> page) {
    this.content = page.getContent();
    this.pageable =
        new CustomPageable(
            page.getPageable().getPageNumber(),
            page.getPageable().getPageSize(),
            page.getTotalElements());
  }

  @Data
  class CustomPageable {
    int pageNumber;
    int pageSize;
    long totalElements;

    public CustomPageable(int pageNumber, int pageSize, long totalElements) {
      this.pageNumber = pageNumber;
      this.pageSize = pageSize;
      this.totalElements = totalElements;
    }
  }
}
