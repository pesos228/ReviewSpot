package com.webServer.ReviewSpot.dto;

public class ClientOutputDto extends ClientInputDto{
    private int id;

    public ClientOutputDto(int id) {
        this.id = id;
    }

    public ClientOutputDto(String name, String email, String password, String photoUrl, int id) {
        super(name, email, password, photoUrl);
        this.id = id;
    }

    protected ClientOutputDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
