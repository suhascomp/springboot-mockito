package org.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    Book RECORD_1 = new Book(1L,"Java","Java is Programming language, written by Sandip B.", 800.10, 7);
    Book RECORD_2 = new Book(2L,"Algorithms","Algorithms is Programming Algorithms, written by Dr. Sandip B.", 1100.70, 8);

    Book RECORD_3 = new Book(3L,"Dot Connected","This is life lessons book, written by Waliya Sandip B.", 120.75, 6);

   @BeforeEach
   public void setUp() {
       MockitoAnnotations.initMocks(this);
       this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
   }

    @Test
    public void getAllBooks() throws Exception {
        List<Book> records = new ArrayList<>(Arrays.asList(RECORD_1,RECORD_2,RECORD_3));
        Mockito.when(bookRepository.findAll()).thenReturn(records);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/book/get-all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("Dot Connected")));
    }

    @Test
    void findByBookId() throws Exception {
       Mockito.when(bookRepository.findById(RECORD_1.getId())).thenReturn(Optional.of(RECORD_1));
       mockMvc.perform(MockMvcRequestBuilders
               .get("/book/get-by-id/1")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name", is("Java")));
    }

    @Test
    void saveBook() throws Exception {
       Book newRecord = Book.builder()
                       .id(4L)
                               .name("Java By Sandip")
                                       .summery("One of the top selling book by Sandip")
                                               .price(300.90)
                                                       .rating(8).build();
       Mockito.when(bookRepository.save(RECORD_1)).thenReturn(RECORD_1);
       mockMvc.perform(MockMvcRequestBuilders
               .post("/book/save")
                       .accept(MediaType.APPLICATION_JSON)
                       .content(objectWriter.writeValueAsString(RECORD_1))
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name", is("Java")))
               .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void updateBook() {
    }
}