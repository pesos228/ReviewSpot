package com.webServer.ReviewSpot.dto;

public class GenreInputDto {
    private String name;

    public GenreInputDto(String name) {
        this.name = name;
    }

    protected GenreInputDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
