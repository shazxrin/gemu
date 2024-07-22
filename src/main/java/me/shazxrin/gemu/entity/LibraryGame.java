package me.shazxrin.gemu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "library_games")
public class LibraryGame extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(optional = false)
    @JoinColumn(name = "platform_id", nullable = false)
    private Platform platform;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LibraryGameStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "ownership", nullable = false)
    private LibraryGameOwnership ownership;

    @Min(value = 0)
    @Column(name = "hours_played", nullable = false)
    private Integer hoursPlayed;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "progress", nullable = false)
    private Integer progress;

    public LibraryGame() {
    }

    public LibraryGame(
        Game game,
        Platform platform,
        LibraryGameStatus status,
        LibraryGameOwnership ownership,
        Integer hoursPlayed,
        Integer progress
    ) {
        this.game = game;
        this.platform = platform;
        this.status = status;
        this.ownership = ownership;
        this.hoursPlayed = hoursPlayed;
        this.progress = progress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public LibraryGameStatus getStatus() {
        return status;
    }

    public void setStatus(LibraryGameStatus status) {
        this.status = status;
    }

    public LibraryGameOwnership getOwnership() {
        return ownership;
    }

    public void setOwnership(LibraryGameOwnership ownership) {
        this.ownership = ownership;
    }

    public Integer getHoursPlayed() {
        return hoursPlayed;
    }

    public void setHoursPlayed(Integer hoursPlayed) {
        this.hoursPlayed = hoursPlayed;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                                   ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                                   : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                                      ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                                      : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Platform platform = (Platform) o;
        return getId() != null && Objects.equals(getId(), platform.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
               ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
               : getClass().hashCode();
    }
}
