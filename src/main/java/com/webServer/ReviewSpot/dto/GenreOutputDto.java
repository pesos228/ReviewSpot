package com.webServer.ReviewSpot.dto;

public class GenreOutputDto extends GenreInputDto{
    private int id;

    public GenreOutputDto(String name, int id) {
        super(name);
        this.id = id;
    }

    public GenreOutputDto(int id) {
        this.id = id;
    }

    protected GenreOutputDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
