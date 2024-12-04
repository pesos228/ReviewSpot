package com.webServer.ReviewSpot.cfg;

import com.github.javafaker.Faker;
import com.webServer.ReviewSpot.entity.*;
import com.webServer.ReviewSpot.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        String notFound = "https://juststickers.in/wp-content/uploads/2016/12/404-error-not-found-badge.png";
        Faker faker = new Faker();

        List<String> genreNames = Arrays.asList(
                "Action", "Drama", "Comedy", "Science Fiction", "Horror",
                "Fantasy", "Thriller", "Romance", "Adventure", "Documentary",
                "Animation", "Mystery", "Historical", "Musical", "Western"
        );

        List<Genre> genres = genreNames.stream()
                .map(name -> new Genre(name, new ArrayList<>()))
                .toList();
        genres.forEach(genreRepository::save);


        Set<String> usedTitles = new HashSet<>();
        List<Media> movies = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String title;
            do {
                title = faker.book().title();
            } while (usedTitles.contains(title));

            usedTitles.add(title);
            Media movie = new Media(
                    title,
                    notFound,
                    faker.lorem().sentence(15),
                    faker.number().numberBetween(1, 10),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    List.of(genres.get(faker.number().numberBetween(0, genres.size())))
            );
            movies.add(movie);
        }

        movies.forEach(mediaRepository::save);

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

        Client client2 = new Client(
                "Testik",
                "vladick@gmail.com",
                "password123",
                "https://png.pngtree.com/png-vector/20240123/ourlarge/pngtree-cute-little-orange-cat-cute-kitty-png-image_11459046.png",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        clientRepository.save(client2);

        Comment comment = new Comment(
                client,
                movies.getFirst(),
                LocalDateTime.now(),
                "Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!"
        );
        commentRepository.save(comment);

        for (int i = 0; i < 20; i++) {
            Comment commentNew = new Comment(
                    client,
                    movies.getFirst(),
                    LocalDateTime.now(),
                    "Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!Great movie!"
            );
            commentRepository.save(commentNew);
        }

        Review review = new Review(
                client,
                movies.getFirst(),
                LocalDateTime.now(),
                5,
                "COMPLETED",
                "One of the best movies ever!"
        );
        reviewRepository.save(review);
        System.out.println("Review ID: " + review.getId());

        Reaction reaction = new Reaction(
                client,
                comment,
                true
        );

        reactionRepository.save(reaction);


        Set<String> usedEmails = new HashSet<>();

        for (int i = 0; i < 50; i++) {
            String email;
            do {
                email = faker.internet().emailAddress();
            } while (usedEmails.contains(email));
            usedEmails.add(email);

            Client newClient = new Client(
                    faker.name().fullName(),
                    email,
                    faker.internet().password(8, 20),
                    notFound,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>()
            );
            clientRepository.save(newClient);
        }
    }
}
