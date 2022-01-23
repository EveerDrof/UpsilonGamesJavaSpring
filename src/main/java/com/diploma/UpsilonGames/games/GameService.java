package com.diploma.UpsilonGames.games;

import com.diploma.UpsilonGames.IMarkAcceptableService;
import com.diploma.UpsilonGames.marks.MarkRepository;
import com.diploma.UpsilonGames.pictures.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.function.Function;


@Service
public class GameService implements IMarkAcceptableService {
    private GameRepository gameRepository;
    private MarkRepository markRepository;

    @Autowired
    public GameService(@Lazy GameRepository gameRepository, @Lazy MarkRepository markRepository) {
        this.gameRepository = gameRepository;
        this.markRepository = markRepository;
    }

    public Game findByName(String name) {
        return gameRepository.findByName(name);
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public boolean existsById(long gameId) {
        return gameRepository.existsById(gameId);
    }

    public Game findById(long id) {
        return gameRepository.getById(id);
    }

    public long getAvgMarkByGameId(Game gameId) {
        byte mark;
        try {
            mark = markRepository.getAverageMarkByGameId(gameId);
        } catch (Exception ex) {
            mark = -1;
        }
        return mark;
    }

    public Picture getShortcutByGameName(String gameName) {
        return gameRepository.findByName(gameName).getShortcut();
    }

    public ArrayList<Game> findAll() {
        return gameRepository.findAll();
    }

    public boolean existsByName(String name) {
        return gameRepository.existsByName(name);
    }

    public ArrayList<Game> select(String[] tagsArr, double maxPrice, double minPrice, byte minMark,
                                  String namePart, double minDiscountPercent, String sortType) {
        ArrayList<Game> games;
        if (tagsArr.length == 0) {
            games = gameRepository.select(maxPrice, minPrice, minMark, namePart, minDiscountPercent);
        } else {
            games = gameRepository.select(tagsArr, maxPrice, minPrice, minMark, namePart, minDiscountPercent, tagsArr.length);
        }
        Function<Double, Integer> comparatorConverterToInteger = (Double value) -> {
            if (value > 0) {
                return -1;
            }
            if (value < 0) {
                return 1;
            } else {
                return 0;
            }
        };
        switch (sortType) {
            case "discount":
                games.sort((a, b) -> {
                    double diff = ((a.getPrice() - a.getDiscountPrice()) / a.getPrice()) -
                            ((b.getPrice() - b.getDiscountPrice()) / b.getPrice());
                    return comparatorConverterToInteger.apply(diff);
                });
                break;
            case "mark":
                games.sort((a, b) -> {
                    double diff = (markRepository.getAverageMarkByGameId(a) -
                            markRepository.getAverageMarkByGameId(b));
                    return comparatorConverterToInteger.apply(diff);
                });
                break;
        }
        return games;
    }
}
