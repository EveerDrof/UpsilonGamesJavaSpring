package com.diploma.UpsilonGames;

import com.diploma.UpsilonGames.comments.Comment;
import com.diploma.UpsilonGames.comments.CommentRepository;
import com.diploma.UpsilonGames.foreignReviews.ForeignReviewsData;
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
import com.diploma.UpsilonGames.votes.VoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.*;

@Component
public class DataLoader implements ApplicationRunner {

    static int commentsNumber = 0;
    private UserRepository userRepository;
    private GameRepository gameRepository;
    private MarkRepository markRepository;
    private PictureRepository pictureRepository;
    private ReviewRepository reviewRepository;
    private TagRepository tagRepository;
    private VoteRepository voteRepository;
    private CommentRepository commentRepository;
    private BlobHelper blobHelper;
    private OkHttpClient client;

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
        client = new OkHttpClient();
    }

    private Picture loadPicture(String pictureName, Game game) throws Exception {
        return new Picture(blobHelper.createBlob(
                new FileInputStream(pictureName).readAllBytes()),
                game);
    }

    private void addComments(Review review, ArrayList<User> users, Comment parent, int level) {
        if (level == 0) {
            return;
        }

        for (User user : users) {
            Comment comment = new Comment("Comment " + commentsNumber + " by " + user.getName(),
                    user, review, parent);
            commentsNumber++;
            comment = commentRepository.save(comment);
            addComments(review, users, comment, level - 1);
        }
    }

    public void run(ApplicationArguments args) throws Exception {
        ArrayList<Tag> tags = new ArrayList<>(Arrays.asList(
                new Tag("shooter"),
                new Tag("demons"),
                new Tag("strategy"),
                new Tag("rpg"),
                new Tag("chernobyl"),
                new Tag("war"),
                new Tag("aliens"),
                new Tag("horror"),
                new Tag("multiplayer"),
                new Tag("fantasy"),
                new Tag("action"),
                new Tag("stealth"),
                new Tag("4x"),
                new Tag("steampunk"),
                new Tag("assassins"),
                new Tag("metroid"),
                new Tag("crime"),
                new Tag("2d"),
                new Tag("monsters"),
                new Tag("sci-fi"),
                new Tag("anomalies"),
                new Tag("space")
        ));
        tagRepository.saveAll(tags);
        Random random = new Random();
        ArrayList<Game> games = new ArrayList<>();
        File rootDataDir = new File("pictures/Site");

        for (File dir : rootDataDir.listFiles()) {
            String gameName = dir.getName();
            String description = "";
            int steamId = 0;
            ArrayList<String> picturesNames = new ArrayList<>();
            for (File file : dir.listFiles()) {

                if (file.getName().contains("txt")) {
                    steamId = Integer.parseInt(file.getName().substring(0,
                            file.getName().indexOf('.')));
                    description = Files.readString(file.toPath());
                    continue;
                }
                if (file.getName().equals("tags.txt")) {
                    continue;
                }
                if (!file.getName().equals("logo.png")) {
                    picturesNames.add(file.getAbsolutePath());
                }
            }
            Game game = new Game(gameName, (Math.abs(random.nextInt()) % 4000) + 1, description);
            HashSet<Tag> currentGameTags = new HashSet<>();
            for (int i = 0; i < 10; i++) {
                Tag nextTag = tags.get(Math.abs(random.nextInt()) % tags.size());
                if (!currentGameTags.contains(nextTag)) {
                    currentGameTags.add(nextTag);
                }
            }
            game.setTags(currentGameTags.stream().toList());
            if (Math.abs(random.nextInt()) % 4 == 1) {
                game.setDiscountPrice((Math.abs(random.nextInt()) % game.getPrice()) + 1);
            }
            game = gameRepository.save(game);
            games.add(game);
            Picture logo = loadPicture(dir.getAbsolutePath() + "/logo.png", game);
            game.setShortcut(logo);
            pictureRepository.save(logo);
            for (String pictureName : picturesNames) {
                pictureRepository.save(loadPicture(pictureName, game));
            }
            Request request = new Request.Builder()
                    .url("https://store.steampowered.com/appreviews/" + steamId + "?json=1")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                HashMap<String, Object> result = new ObjectMapper().readValue(response.body().string(), HashMap.class);
                game.setForeignReviewsDataSteam(new ForeignReviewsData(
                        ForeignReviewsData.SiteType.STEAM,
                        game,
                        result
                ));
                gameRepository.save(game);
            } catch (Exception ex) {

            }
        }
        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("admin", "Univac00Eniac_1", UserRole.ADMIN),
                new User("qkql", "12_Passwsdfgdord", UserRole.USER),
                new User("bob", "Passwdsdfgsdfg_00", UserRole.USER),
                new User("michael", "000___11AAaaaaa", UserRole.USER),
                new User("peter", "000___AAaaaaaBBB", UserRole.USER)
        ));
        userRepository.saveAll(users);
        ArrayList<Review> reviews = new ArrayList<>();
        for (Game game : games) {
            for (User user : users) {
                markRepository.save(new Mark(
                        (byte) (Math.abs(random.nextInt()) % 100), game, user)
                );
                Review review = new Review("Test review for " + game.getName() + " by " +
                        user.getName(), game, user);
                reviews.add(review);
                reviewRepository.save(review);
            }
        }
        for (Review review : reviews) {
            addComments(review, users, null, 2);
        }

        System.out.println("Initialization completed");
//        ArrayList<Comment> comments = new ArrayList<>(Arrays.asList(
//                new Comment("First comment by admin", users.get(0), reviews.get(0), null)
//        ));
//        comments.add(new Comment("Second comment qkql", users.get(1), reviews.get(0), comments.get(0)));
//        comments.add(new Comment("Third comment by bob", users.get(2), reviews.get(0), comments.get(1)));
//        comments.add(new Comment("Fourth comment by bob", users.get(2), reviews.get(0), comments.get(0)));
//        comments.add(new Comment("Fifth comment by admin", users.get(0), reviews.get(0), null));
//        comments.add(new Comment("Sixth comment by qkql", users.get(1), reviews.get(0), comments.get(2)));
//        comments.add(new Comment("Seventh comment by qkql", users.get(1), reviews.get(0), comments.get(5)));
//        comments.add(new Comment("Eighth comment by qkql", users.get(1), reviews.get(0), comments.get(6)));
//        commentRepository.saveAll(comments);
//
//        ArrayList<Vote> votes = new ArrayList<>(Arrays.asList(
//                new Vote(true, users.get(0), reviews.get(0)),
//                new Vote(false, users.get(0), reviews.get(1)),
//                new Vote(false, users.get(0), comments.get(0)),
//                new Vote(true, users.get(0), comments.get(1)),
//                new Vote(false, users.get(0), comments.get(2))
//        ));
//        voteRepository.saveAll(votes);
    }
}