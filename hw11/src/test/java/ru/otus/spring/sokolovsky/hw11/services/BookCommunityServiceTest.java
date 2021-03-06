package ru.otus.spring.sokolovsky.hw11.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.sokolovsky.hw11.changelogs.SeedCreator;
import ru.otus.spring.sokolovsky.hw11.domain.Book;
import ru.otus.spring.sokolovsky.hw11.domain.Comment;
import ru.otus.spring.sokolovsky.hw11.repository.CommentRepository;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {"/test-application.properties"})
class BookCommunityServiceTest {

    final String isbn = "978-5-9905833-8-2";

    @Autowired
    private BookCommunityService service;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void createFixtures(@Autowired SeedCreator seed) {
        seed.create();
    }

    @Test
    void registerComment() {
        Book book = libraryService.getBookByIsbn(isbn).block();
        Comment comment = service.registerBookComment(book, "Test comment").block();

        assertNotEquals(0, comment.getId());

        List<Comment> commentsWithTestText = commentRepository.findByText("Test comment").collectList().block();
        boolean isPersist = commentsWithTestText.stream().anyMatch(c -> c.getId().equals(comment.getId()));
        assertTrue(isPersist);
    }
}
