package com.onetuks.libraryapi.book.controller;

import com.onetuks.libraryapi.book.dto.request.BookPostRequest;
import com.onetuks.libraryapi.book.dto.response.BookIsbnGetResponse;
import com.onetuks.libraryapi.book.dto.response.BookResponse;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.service.BookService;
import com.onetuks.libraryexternal.book.handler.dto.IsbnResult;
import com.onetuks.libraryexternal.book.service.IsbnSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/books")
public class BookRestController {

  private final BookService bookService;
  private final IsbnSearchService isbnSearchService;

  public BookRestController(BookService bookService, IsbnSearchService isbnSearchService) {
    this.bookService = bookService;
    this.isbnSearchService = isbnSearchService;
  }

  /**
   * ISBN 도서 정보 조회
   *
   * @param isbn : ISBN
   * @return : 도서 정보
   */
  @GetMapping(path = "/isbn/{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookIsbnGetResponse> getBookWithIsbn(
      @PathVariable(name = "isbn") String isbn) {
    IsbnResult result = isbnSearchService.search(isbn);
    BookIsbnGetResponse response = BookIsbnGetResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 도서 등록
   *
   * @param request : 도서 등록 요청
   * @return : 도서 등록 결과
   */
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponse> postNewBook(
      @LoginId Long loginId,
      @RequestBody BookPostRequest request,
      @RequestPart(name = "cover-image", required = false) MultipartFile coverImage) {
    Book result = bookService.register(loginId, request.to(), coverImage);
    BookResponse response = BookResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
