package com.webServer.ReviewSpot.cfg;

import com.webServer.ReviewSpot.entity.*;
import com.webServer.ReviewSpot.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

//@Component
@Transactional
public class Clr implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final MediaRepository mediaRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final ReactionRepository reactionRepository;

    public Clr(ClientRepository clientRepository, MediaRepository mediaRepository,
               GenreRepository genreRepository, CommentRepository commentRepository,
               ReviewRepository reviewRepository, ReactionRepository reactionRepository) {
        this.clientRepository = clientRepository;
        this.mediaRepository = mediaRepository;
        this.genreRepository = genreRepository;
        this.commentRepository = commentRepository;
        this.reviewRepository = reviewRepository;
        this.reactionRepository = reactionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Создаем жанры
        Genre action = new Genre("Action", new ArrayList<>());
        Genre drama = new Genre("Drama", new ArrayList<>());
        genreRepository.save(action);
        genreRepository.save(drama);

        // Создаем медиа
        Media movie = new Media(
                "The Matrix",
                "https://m.media-amazon.com/images/M/MV5BNjAxYjkxNjktYTU0YS00NjFhLWIyMDEtMzEzMTJjMzRkMzQ1XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                "A computer programmer discovers a fantastic world...",
                5,
                new ArrayList<>(),
                new ArrayList<>(),
                Arrays.asList(action, drama)
        );
        mediaRepository.save(movie);

        // Создаем клиента
        Client client = new Client(
                "John Doe",
                "john@example.com",
                "password123",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTP4xlp7Az9BofS3TO91z_EaeLvHusgeBqt_A&s",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        clientRepository.save(client);

        // Создаем комментарий
        Comment comment = new Comment(
                client,
                movie,
                LocalDateTime.now(),
                "Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!"
        );
        commentRepository.save(comment);

        for (int i = 0; i < 20; i++) {
            Comment commentNew = new Comment(
                    client,
                    movie,
                    LocalDateTime.now(),
                    "Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!"
            );
            commentRepository.save(commentNew);
        }

        // Создаем обзор
        Review review = new Review(
                client,
                movie,
                LocalDateTime.now(),
                5,
                "COMPLETED",
                "One of the best movies ever!"
        );
        reviewRepository.save(review);
        System.out.println("Review ID: " + review.getId());

        // Создаем реакцию на комментарий
        Reaction reaction = new Reaction(
                client,
                comment,
                true
        );

        reactionRepository.save(reaction);
        var reviewReaction = reactionRepository.findByReviewId(review.getId());
        var commentReaction = reactionRepository.findByCommentId(comment.getId());
        System.out.println(reviewReaction.stream().toList());
        System.out.println(commentReaction.stream().toList());

        // Тестируем поиск
        System.out.println("Finding media by name: " + mediaRepository.findByName("The Matrix").getName());
        System.out.println("Finding client by email: " + clientRepository.findByEmail("john@example.com").getName());
        System.out.println("Finding reviews by media ID: " + reviewRepository.findByMediaId(movie.getId()).size());
    }
}
