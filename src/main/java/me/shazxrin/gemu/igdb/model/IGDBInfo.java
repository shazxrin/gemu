package me.shazxrin.gemu.igdb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "igdb_info")
@Entity
public class IGDBInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDateTime expiryDateTime;

    private String accessToken;

    public IGDBInfo() { }

    public IGDBInfo(LocalDateTime expiryDateTime, String accessToken) {
        this.id = null;
        this.expiryDateTime = expiryDateTime;
        this.accessToken = accessToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getExpiryDateTime() {
        return expiryDateTime;
    }

    public void setExpiryDateTime(LocalDateTime expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
