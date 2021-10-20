package com.diploma.UpsilonGames.marks;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Mark {
    private byte mark;
    @EmbeddedId
    private MarkId markId;

    public MarkId getMarkId() {
        return markId;
    }

    public Mark() {
    }

    public Mark(byte mark, long gameId, int userId) {
        this.mark = mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mark mark = (Mark) o;

        return Objects.equals(markId, mark.markId);
    }
    public int getMark() {
        return mark;
    }

    public long getGameId() {
        return markId.getGameId();
    }

    public long getUserId() {
        return markId.getUserId();
    }
}
