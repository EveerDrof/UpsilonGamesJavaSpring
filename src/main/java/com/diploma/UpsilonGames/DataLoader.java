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
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private RandomTextGenerator randomTextGenerator;

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
        this.client = new OkHttpClient();
        this.randomTextGenerator = new RandomTextGenerator();
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
            Comment comment = new Comment(randomTextGenerator.getComment(),
                    user, review, parent);
            commentsNumber++;
            comment = commentRepository.save(comment);
            addComments(review, users, comment, level - 1);
        }
    }

    private Picture getPoster(String imdbId, Game game) throws IOException {
        Request posterObjectsRequest = new Request.Builder().url(
                "https://imdb-api.com/en/API/Posters/k_r9n5k2r9/" + imdbId).build();
        Response posterObjectsResponse = client.newCall(posterObjectsRequest).execute();
        JSONObject posterObjectsJSONObject = new JSONObject(posterObjectsResponse.body().string());
        JSONArray jsonArray = posterObjectsJSONObject.getJSONArray("posters");
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        Request posterImageRequest = new Request.Builder().url(jsonObject.getString("link")).build();
        Response posterImageResponse = client.newCall(posterImageRequest).execute();
        Picture picture = new Picture(blobHelper.createBlob(posterImageResponse.body().bytes()), game);
        pictureRepository.save(picture);
        return picture;
    }

    private void loadAndSaveScreenshots(String imdbId, Game game) throws IOException {
        Request posterObjectsRequest = new Request.Builder().url(
                "https://imdb-api.com/en/API/Images/k_r9n5k2r9/" + imdbId + "/Short").build();
        Response posterObjectsResponse = client.newCall(posterObjectsRequest).execute();
        JSONObject posterObjectsJSONObject = new JSONObject(posterObjectsResponse.body().string());
        JSONArray jsonArray = posterObjectsJSONObject.getJSONArray("items");
        int currentScreenshotsLoaded = 0;
        int maxScreenshots = 4;
        for (Object pictureJson : jsonArray) {
            JSONObject jsonObject = (JSONObject) pictureJson;
            Request posterImageRequest = new Request.Builder().url(jsonObject.getString("image")).build();
            Response posterImageResponse = client.newCall(posterImageRequest).execute();
            Picture picture = new Picture(blobHelper.createBlob(posterImageResponse.body().bytes()), game);
            pictureRepository.save(picture);
            currentScreenshotsLoaded++;
            if (maxScreenshots == currentScreenshotsLoaded) {
                return;
            }
        }
    }

    public void run(ApplicationArguments args) throws Exception {
        long startingTime = System.nanoTime();
        ArrayList<Tag> gameTags = new ArrayList<>(Arrays.asList(
                new Tag("game"),
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
                new Tag("action-rpg"),
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
        tagRepository.saveAll(gameTags);
        Random random = new Random();
        ArrayList<Game> games = new ArrayList<>();
        String startupDataLocation = "StartupData";
        File rootDataDir = new File(startupDataLocation + "/pictures/Site");
        File configFile = new File(startupDataLocation + "/config.json");
        if (configFile.exists()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(configFile), "UTF-8"));
            String configContent = "";
            for (String line : br.lines().toList()) {
                configContent += line;
            }
            br.close();
            JSONObject json = new JSONObject(configContent);
            JSONArray filmsJsonObjects = json.getJSONArray("filmsList");
            Tag movieTag = new Tag("movie");
            tagRepository.save(movieTag);
            Set<Tag> movieTagsSet = new HashSet<>(Set.of(movieTag));
            for (Object obj : filmsJsonObjects) {
                JSONObject jsonObject = (JSONObject) obj;
                String id = jsonObject.getString("id");
                Request wikiRequest = new Request.Builder().url("https://imdb-api.com/ru/API/Wikipedia/k_r9n5k2r9/"
                        + id).build();
                Response wikiResponse = client.newCall(wikiRequest).execute();
                JSONObject wikiJSONObject = new JSONObject(wikiResponse.body().string());
                String description = wikiJSONObject.getJSONObject("plotShort").getString("plainText");
                String name = wikiJSONObject.getString("title");
                String tagName = jsonObject.getString("tag");
                if (!movieTagsSet.contains(tagName)) {
                    movieTagsSet.add(new Tag(tagName));
                }
                Game game = new Game(name, Math.abs(random.nextInt()) % 500, description);
                game = gameRepository.save(game);
                Picture shortcut = getPoster(id, game);
                game.setShortcut(shortcut);
                game.setTags(Arrays.asList(movieTag, movieTagsSet.stream().filter(tag -> tag.getName().equals(tagName))
                        .collect(Collectors.toList()).get(0)));
                games.add(game);
                gameRepository.save(game);
                loadAndSaveScreenshots(id, game);
            }
        }
        String pictureType = ".jpg";
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
                if (!file.getName().equals("logo" + pictureType)) {
                    picturesNames.add(file.getAbsolutePath());
                }
            }
            Game game = new Game(gameName, (Math.abs(random.nextInt()) % 4000) + 1, description);
            HashSet<Tag> currentGameTags = new HashSet<>();
            for (int i = 0; i < 10; i++) {
                int tagId = Math.abs((random.nextInt())) % (gameTags.size() - 1) + 1;
                Tag nextTag = gameTags.get(tagId);
                if (!currentGameTags.contains(nextTag)) {
                    currentGameTags.add(nextTag);
                }
            }
            currentGameTags.add(gameTags.get(0));
            game.setTags(currentGameTags.stream().toList());
            if (Math.abs(random.nextInt()) % 4 == 1) {
                game.setDiscountPrice((Math.abs(random.nextInt()) % game.getPrice()) + 1);
            }
            game = gameRepository.save(game);
            games.add(game);
            Picture logo = loadPicture(dir.getAbsolutePath() + "/logo" + pictureType, game);
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
                new User("admin", "000___11AAaaaaa", UserRole.ADMIN),
                new User("qkql", "12_Passwsdfgdord", UserRole.USER)
//                new User("bob", "Passwdsdfgsdfg_00", UserRole.USER)
//                new User("michael", "000___11AAaaaaa", UserRole.USER),
//                new User("peter", "000___AAaaaaaBBB", UserRole.USER)
        ));
        userRepository.saveAll(users);
        ArrayList<Review> reviews = new ArrayList<>();
        for (Game game : games) {
            for (User user : users) {
                markRepository.save(new Mark(
                        (byte) (Math.abs(random.nextInt()) % 100), game, user)
                );
                Review review = new Review(randomTextGenerator.getReviewText(), game, user);
                reviews.add(review);
                reviewRepository.save(review);
            }
        }
        for (Review review : reviews) {
            addComments(review, users, null, 2);
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startingTime;
        System.out.println("Initialization completed. Elapsed time : " + TimeUnit.SECONDS.convert(elapsedTime,
                TimeUnit.NANOSECONDS) + " s");
    }
}