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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class DataLoader implements ApplicationRunner {

    Random random = new Random();
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
    private String startupDataLocation = "StartupData";
    private String filmDataFileName = "filmData.json";

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

    private Picture getPictureFromUrl(String url, Game game) throws IOException {
        Request pictureObjectsRequest = new Request.Builder().url(
                url).build();
        Response pictureObjectsResponse = client.newCall(pictureObjectsRequest).execute();
        Picture picture = new Picture(blobHelper.createBlob(pictureObjectsResponse.body().bytes()), game);
        pictureRepository.save(picture);
        return picture;
    }

    private Picture loadAndSavePicture(String imageUrl, Game game, String outputFileName) throws Exception {
        Request posterImageRequest = new Request.Builder().url(imageUrl).build();
        Response posterImageResponse = client.newCall(posterImageRequest).execute();
        Blob blob = blobHelper.createBlob(posterImageResponse.body().bytes());
        InputStream in = blob.getBinaryStream();
        OutputStream out = new FileOutputStream(outputFileName);
        byte[] buff = new byte[4096];  // how much of the blob to read/write at a time
        int len = 0;
        while ((len = in.read(buff)) != -1) {
            out.write(buff, 0, len);
        }
        out.close();
        Picture picture = new Picture(blob, game);
        try {
            pictureRepository.save(picture);
        } catch (Exception ex) {
            System.out.println(imageUrl);
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
        }
        return picture;
    }

    private void loadAndSaveScreenshots(String imdbId, Game game, File filmDir) throws Exception {
        Request posterObjectsRequest = new Request.Builder().url(
                "https://imdb-api.com/en/API/Images/k_r9n5k2r9/" + imdbId + "/Short").build();
        Response posterObjectsResponse = client.newCall(posterObjectsRequest).execute();
        JSONObject posterObjectsJSONObject = new JSONObject(posterObjectsResponse.body().string());
        JSONArray jsonArray = posterObjectsJSONObject.getJSONArray("items");
        int currentScreenshotsLoaded = 0;
        int maxScreenshots = 2;
        for (Object pictureJson : jsonArray) {
            JSONObject jsonObject = (JSONObject) pictureJson;
            loadAndSavePicture(jsonObject.getString("image"), game,
                    filmDir.toPath() + "/" + currentScreenshotsLoaded + ".jpg");
            currentScreenshotsLoaded++;
            if (maxScreenshots == currentScreenshotsLoaded) {
                return;
            }
        }
    }

    private void loadAndSaveScreenshotsFromDrive(String id, Game game, File filmDir) throws Exception {
        for (File file : filmDir.listFiles()) {
            if (file.getName().contains(".jpg") && !file.getName().equals("poster.jpg")) {
                Picture picture = loadPicture(file.getAbsolutePath(), game);
                pictureRepository.save(picture);
            }
        }
    }

    private void loadMovieFromDrive(String id, File filmDir, Tag movieTag, String tagName,
                                    ArrayList<Game> games)
            throws Exception {
        System.out.println("Loading : " + id + " from drive");
        String jsonFilmFileString = Files.readString(Path.of(filmDir.getAbsolutePath() + "/" + filmDataFileName),
                StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(jsonFilmFileString);
        String name = jsonObject.getString("name");
        String description = jsonObject.getString("description");
        Game game = new Game(name, Math.abs(random.nextInt()) % 500, description);
        game = gameRepository.save(game);
        Picture shortcut = loadPicture(filmDir.getAbsolutePath() + "/poster.jpg", game);
        pictureRepository.save(shortcut);
        game.setShortcut(shortcut);
        game.setTags(Arrays.asList(movieTag, tagRepository.findByName(tagName)));
        games.add(game);
        gameRepository.save(game);
        loadAndSaveScreenshotsFromDrive(id, game, filmDir);
    }

    private void loadMovieFromNetworkAndSave(String id, String tagName, String posterUrl, Tag movieTag,
                                             Set<String> movieTagsSet, ArrayList<Game> games, File filmDir)
            throws Exception {
        System.out.println("Loading : " + id + " from network");
        Request wikiRequest = new Request.Builder().url("https://imdb-api.com/ru/API/Wikipedia/k_r9n5k2r9/"
                + id).build();
        Response wikiResponse = client.newCall(wikiRequest).execute();
        JSONObject wikiJSONObject = new JSONObject(wikiResponse.body().string());
        String description = wikiJSONObject.getJSONObject("plotShort").getString("plainText");
        String name = wikiJSONObject.getString("title");
        JSONObject wholeFilmDataKJSONObject = new JSONObject();
        wholeFilmDataKJSONObject.put("name", name);
        wholeFilmDataKJSONObject.put("description", description);
        PrintWriter out = new PrintWriter(filmDir.getAbsolutePath() + "/" + filmDataFileName);
        out.write(wholeFilmDataKJSONObject.toString());
        out.close();
        if (!movieTagsSet.contains(tagName)) {
            movieTagsSet.add(tagName);
            tagRepository.save(new Tag(tagName));
        }
        Game game = new Game(name, Math.abs(random.nextInt()) % 500, description);
        game = gameRepository.save(game);
        Picture shortcut = loadAndSavePicture(posterUrl, game, filmDir.getAbsolutePath() + "/poster.jpg");
        game.setShortcut(shortcut);
        game.setTags(Arrays.asList(movieTag, tagRepository.findByName(tagName)));
        games.add(game);
        gameRepository.save(game);
        loadAndSaveScreenshots(id, game, filmDir);
    }

    private void loadMovies(ArrayList<Game> games) throws Exception {
        File downloadedFilmsDataDir = new File(startupDataLocation + "/../../downloadedFilmsData");
        if (!downloadedFilmsDataDir.exists()) {
            if (!downloadedFilmsDataDir.mkdir()) {
                throw new Exception("Could not create downloadedFilmsData dir");
            }
        }

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
            Set<String> movieTagsSet = new HashSet<>(Set.of(movieTag.getName()));
            for (Object obj : filmsJsonObjects) {
                JSONObject jsonObject = (JSONObject) obj;
                String id = jsonObject.getString("id");
                String tagName = jsonObject.getString("tag");
                String posterURL = jsonObject.getString("posterUrl");
                File filmDir = new File(downloadedFilmsDataDir.getAbsolutePath() + "/" + id);
                if (filmDir.exists()) {
                    loadMovieFromDrive(id, filmDir, movieTag, tagName, games);
                    continue;
                }
                filmDir.mkdir();
                loadMovieFromNetworkAndSave(id, tagName, posterURL, movieTag, movieTagsSet, games, filmDir);
            }
        }
    }

    private void loadGames(ArrayList<Game> games, ArrayList<Tag> gameTags) throws Exception {
        File gameDataDir = new File(startupDataLocation + "/pictures/Site");
        String pictureType = ".jpg";
        for (File dir : gameDataDir.listFiles()) {
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
    }

    private ArrayList<Tag> loadTags() {
        ArrayList<Tag> tags = new ArrayList<>(Arrays.asList(
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
        return tags;
    }

    private ArrayList<Review> addReviews(ArrayList<Game> games, ArrayList<User> users) {
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
        return reviews;
    }

    public void run(ApplicationArguments args) throws Exception {
        long startingTime = System.nanoTime();
        ArrayList<Tag> gameTags = loadTags();
        tagRepository.saveAll(gameTags);
        ArrayList<Game> games = new ArrayList<>();
        loadMovies(games);
        loadGames(games, gameTags);
        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("admin", "000___11AAaaaaa", UserRole.ADMIN),
                new User("qkql", "12_Passwsdfgdord", UserRole.USER)
//                new User("bob", "Passwdsdfgsdfg_00", UserRole.USER)
//                new User("michael", "000___11AAaaaaa", UserRole.USER),
//                new User("peter", "000___AAaaaaaBBB", UserRole.USER)
        ));
        userRepository.saveAll(users);
        ArrayList<Review> reviews = addReviews(games, users);
        for (Review review : reviews) {
            addComments(review, users, null, 2);
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startingTime;
        System.out.println("Initialization completed. Elapsed time : " + TimeUnit.SECONDS.convert(elapsedTime,
                TimeUnit.NANOSECONDS) + " s");
    }
}