package org.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/get-all")
    public List<Book> getAllBooks() {
       return bookRepository.findAll();
    }

    @GetMapping("/get-by-id/{id}")
    public Book findByBookId(@PathVariable("id") Long id) {
        return bookRepository.findById(id).get();
    }

    @PostMapping("/save")
    public Book saveBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping("/update")
    public Book updateBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }
}
