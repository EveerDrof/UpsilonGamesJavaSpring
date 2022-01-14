package com.diploma.UpsilonGames;

import com.diploma.UpsilonGames.comments.Comment;
import com.diploma.UpsilonGames.comments.CommentRepository;
import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.games.GameRepository;
import com.diploma.UpsilonGames.marks.Mark;
import com.diploma.UpsilonGames.marks.MarkRepository;
import com.diploma.UpsilonGames.pictures.Picture;
import com.diploma.UpsilonGames.pictures.PictureRepository;
import com.diploma.UpsilonGames.reviews.Review;
import com.diploma.UpsilonGames.reviews.ReviewRepository;
import com.diploma.UpsilonGames.security.UserRole;
import com.diploma.UpsilonGames.tags.Tag;
import com.diploma.UpsilonGames.tags.TagRepository;
import com.diploma.UpsilonGames.users.User;
import com.diploma.UpsilonGames.users.UserRepository;
import com.diploma.UpsilonGames.votes.Vote;
import com.diploma.UpsilonGames.votes.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class DataLoader implements ApplicationRunner {

    private UserRepository userRepository;
    private GameRepository gameRepository;
    private MarkRepository markRepository;
    private PictureRepository pictureRepository;
    private ReviewRepository reviewRepository;
    private TagRepository tagRepository;
    private VoteRepository voteRepository;
    private CommentRepository commentRepository;
    private BlobHelper blobHelper;


    @Autowired
    public DataLoader(UserRepository userRepository, GameRepository gameRepository, MarkRepository markRepository,
                      BlobHelper blobHelper, PictureRepository pictureRepository, ReviewRepository reviewRepository,
                      VoteRepository voteRepository, TagRepository tagRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.markRepository = markRepository;
        this.blobHelper = blobHelper;
        this.pictureRepository = pictureRepository;
        this.reviewRepository = reviewRepository;
        this.tagRepository = tagRepository;
        this.voteRepository = voteRepository;
        this.commentRepository = commentRepository;
    }

    public void run(ApplicationArguments args) throws Exception {
        ArrayList<Game> games = new ArrayList<>(Arrays.asList(
                new Game("Stalker", 500, "Game about Chernobyl"),
                new Game("Devil May Cry", 1000, "Game about demons"))
        );
        ArrayList<Tag> tags = new ArrayList<>(Arrays.asList(
                new Tag("shooter"),
                new Tag("demons"),
                new Tag("rpg"),
                new Tag("chernobyl"),
                new Tag("inventory management"),
                new Tag("sci-fi"),
                new Tag("open world"),
                new Tag("anomalies")
        ));
        for (int i = 0; i < 2; i++) {
            games.get(i).addTag(tags.get(i));
        }
        for (int i = 2; i < tags.size(); i++) {
            games.get(0).addTag(tags.get(i));
        }
        tagRepository.saveAll(tags);
        gameRepository.saveAll(games);
        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("admin", "Univac00Eniac_1", UserRole.ADMIN),
                new User("qkql", "12_Passwsdfgdord", UserRole.USER),
                new User("bob", "Passwdsdfgsdfg_00", UserRole.USER)
        ));
        userRepository.saveAll(users);
        ArrayList<Mark> marks = new ArrayList<>(Arrays.asList(
                new Mark((byte) 50, games.get(0), users.get(1)),
                new Mark((byte) 25, games.get(1), users.get(1)),
                new Mark((byte) 50, games.get(0), users.get(2)),
                new Mark((byte) 100, games.get(1), users.get(2))
        ));
        markRepository.saveAll(marks);
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        ArrayList<Picture> shortcuts = new ArrayList<>(Arrays.asList(
                new Picture(blobHelper.createBlob(
                        new FileInputStream("./pictures/S.T.A.L.K.E.R-logo.png").readAllBytes()),
                        games.get(0)),
                new Picture(blobHelper.createBlob(
                        new FileInputStream("./pictures/DMC.webp").readAllBytes()),
                        games.get(1)),
                new Picture(blobHelper.createBlob(
                        new FileInputStream("./pictures/STAKLERSCREENSHOT.jpg").readAllBytes()),
                        games.get(0)),
                new Picture(blobHelper.createBlob(
                        new FileInputStream("./pictures/STAKLERSCREENSHOT2.jpg").readAllBytes()),
                        games.get(0))
        ));
        pictureRepository.saveAll(shortcuts);
        for (int i = 0; i < 2; i++) {
            games.get(i).setShortcut(shortcuts.get(i));
            gameRepository.save(games.get(i));
        }
        ArrayList<Review> reviews = new ArrayList<>(Arrays.asList(
                new Review("This is a review for STALKER by USER 1", games.get(0), users.get(0)),
                new Review("This is a review for DMC by USER 1", games.get(1), users.get(0)),
                new Review("This is a review for STALKER by USER 2", games.get(0), users.get(1)),
                new Review("This is a review for DMC by USER 2", games.get(1), users.get(1))
        ));
        reviewRepository.saveAll(reviews);
        ArrayList<Comment> comments = new ArrayList<>(Arrays.asList(
                new Comment("First comment by admin", users.get(0), reviews.get(0), null)
        ));
        comments.add(new Comment("Second comment qkql", users.get(1), reviews.get(0), comments.get(0)));
        comments.add(new Comment("Third comment by bob", users.get(2), reviews.get(0), comments.get(1)));
        comments.add(new Comment("Fourth comment by bob", users.get(2), reviews.get(0), comments.get(0)));
        comments.add(new Comment("Fifth comment by admin", users.get(0), reviews.get(0), null));
        comments.add(new Comment("Sixth comment by qkql", users.get(1), reviews.get(0), comments.get(2)));
        comments.add(new Comment("Seventh comment by qkql", users.get(1), reviews.get(0), comments.get(5)));
        comments.add(new Comment("Eighth comment by qkql", users.get(1), reviews.get(0), comments.get(6)));
        commentRepository.saveAll(comments);

        ArrayList<Vote> votes = new ArrayList<>(Arrays.asList(
                new Vote(true, users.get(0), reviews.get(0)),
                new Vote(false, users.get(0), reviews.get(1)),
                new Vote(false, users.get(0), comments.get(0)),
                new Vote(true, users.get(0), comments.get(1)),
                new Vote(false, users.get(0), comments.get(2))
        ));
        voteRepository.saveAll(votes);
    }
}