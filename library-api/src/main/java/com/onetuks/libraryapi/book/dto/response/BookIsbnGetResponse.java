package com.onetuks.libraryapi.book.dto.response;

import com.onetuks.librarydomain.book.handler.dto.IsbnResult;
import com.onetuks.libraryobject.enums.Category;
import java.util.List;

public record BookIsbnGetResponse(
    String title,
    String authorName,
    String introduction,
    String publisher,
    String isbn,
    List<Category> categories,
    String coverImageUrl) {

  public static BookIsbnGetResponse from(IsbnResult isbnResult) {
    return new BookIsbnGetResponse(
        isbnResult.title(),
        isbnResult.authorName(),
        isbnResult.introduction(),
        isbnResult.publisher(),
        isbnResult.isbn(),
        Category.parseRemainCode(isbnResult.kdc()),
        isbnResult.coverImageUrl());
  }
}
