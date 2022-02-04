package bg.sofia.uni.fmi.mjt.newsfeed.dto;

import java.util.Objects;

public class Source {
    private String id;
    private String name;

    public Source(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Source)) return false;
        Source source = (Source) o;
        return Objects.equals(getId(), source.getId()) && Objects.equals(getName(), source.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }
}
