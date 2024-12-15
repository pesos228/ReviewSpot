package com.webServer.ReviewSpot.service.impl;

import com.webServer.ReviewSpot.dto.*;
import com.webServer.ReviewSpot.entity.Genre;
import com.webServer.ReviewSpot.entity.Media;
import com.webServer.ReviewSpot.exceptions.GenreNotFoundException;
import com.webServer.ReviewSpot.exceptions.MediaAlreadyExistsException;
import com.webServer.ReviewSpot.exceptions.MediaNotFoundException;
import com.webServer.ReviewSpot.repository.GenreRepository;
import com.webServer.ReviewSpot.repository.MediaRepository;
import com.webServer.ReviewSpot.service.CommentService;
import com.webServer.ReviewSpot.service.MediaService;
import com.webServer.ReviewSpot.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableCaching
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final CommentService commentService;
    private final ReviewService reviewService;
    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MediaServiceImpl(MediaRepository mediaRepository, CommentService commentService, ReviewService reviewService, GenreRepository genreRepository, ModelMapper modelMapper) {
        this.mediaRepository = mediaRepository;
        this.commentService = commentService;
        this.reviewService = reviewService;
        this.genreRepository = genreRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void save(MediaInputDto mediaInputDto) {
        Media mediaSearch = mediaRepository.findByName(mediaInputDto.getName());
        if (mediaSearch != null){
            throw new MediaAlreadyExistsException("Media with name: "+ mediaInputDto.getName() + " already exists");
        }

        List<Genre> genreList = new ArrayList<>();
        for (String s: mediaInputDto.getGenre()){
            Genre genre = genreRepository.findByName(s);
            if (genre == null){
                throw new GenreNotFoundException("Genre with name: "+ s + " not found");
            }
            genreList.add(genre);
        }

        Media media = new Media(mediaInputDto.getName(), mediaInputDto.getPhotoUrl(), mediaInputDto.getDescription(),
                -1, null, null, genreList);
        mediaRepository.save(media);

    }

    @Override
    public List<MediaCardDto> findAll() {
        return mediaRepository.findAll().stream()
                .map(media -> modelMapper.map(media, MediaCardDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaCardDto> findByNameContaining(String name) {
        return mediaRepository.findByNameContaining(name).stream()
                .map(media -> modelMapper.map(media, MediaCardDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaCardDto> findByGenres(List<String> genres) {
        List<Genre> genreList = new ArrayList<>();
        for (String s: genres){
            Genre genre = genreRepository.findByName(s);
            genreList.add(genre);
        }
        return mediaRepository.findByGenres(genreList).stream()
                .map(media -> modelMapper.map(media, MediaCardDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "MEDIA_PAGE", key = "#id")
    public MediaOutputDto findById(int id) {
        Media media = mediaRepository.findById(id);
        if (media == null){
            throw new MediaNotFoundException("Media with id: "+ id +" not found");
        }
        List<CommentOutputDto> commentOutputDtos = commentService.findByMediaId(id);

        List<ReviewOutputDto> reviewOutputDtos = reviewService.findByMediaId(id);
        List<String> genres = new ArrayList<>();

        for (Genre genre: media.getGenreList()){
            genres.add(genre.getName());
        }

        return new MediaOutputDto(media.getName(), media.getPhotoUrl(), media.getDescription(),
                genres, media.getId(), media.getRating(), commentOutputDtos, reviewOutputDtos);
    }

    @Override
    @Cacheable("MEDIA_TOP")
    public List<MediaOutputDto> getMostPopularMediaByLastWeek(int count) {
        var lastWeek = LocalDateTime.now().minusDays(7);
        return mediaRepository.findAll().stream().map(
                media -> {
                    var reviews = reviewService.findByMediaIdAfterDate(media.getId(), lastWeek);
                    var comments = commentService.findByMediaIdAfterDate(media.getId(), lastWeek);

                    List<String> genres = new ArrayList<>();

                    for (Genre genre: media.getGenreList()){
                        genres.add(genre.getName());
                    }
                    return new MediaOutputDto(media.getName(), media.getPhotoUrl(), media.getDescription(),
                            genres, media.getId(), media.getRating(), comments, reviews);
                }
        ).sorted((m1, m2) -> Integer.compare(
                    m2.getComments().size() + m2.getReviews().size(),
                    m1.getComments().size() + m1.getReviews().size()
                ))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MediaCardDto> getMedia(String searchQuery, List<String> genres, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name"));

        if (searchQuery.isBlank() && genres.isEmpty()){
            return mediaRepository.findAll(pageable).map(media -> modelMapper.map(media, MediaCardDto.class));
        }

        if (!searchQuery.isBlank() && genres.isEmpty()){
            return mediaRepository.findByNameContaining(searchQuery, pageable).map(media -> modelMapper.map(media, MediaCardDto.class));
        }

        List<Genre> genreList = new ArrayList<>();
        for (String genre: genres){
            Genre genreFind = genreRepository.findByName(genre);
            if (genreFind == null){
                throw new GenreNotFoundException("Genre: " + genre + " not found");
            }
            genreList.add(genreFind);
        }

        if (searchQuery.isBlank() && !genres.isEmpty()){
            return mediaRepository.findByGenres(genreList, pageable).map(media -> modelMapper.map(media, MediaCardDto.class));
        }
        return mediaRepository.findByNameContainingAndByGenres(searchQuery, genreList, pageable).map(media -> modelMapper.map(media, MediaCardDto.class));
    }

    @Override
    public Page<MediaCardDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var mediaAll = mediaRepository.findAll(pageable);

        return mediaAll.map(media -> modelMapper.map(media, MediaCardDto.class));
    }

    @Override
    @Transactional
    @CacheEvict(value = "MEDIA_PAGE", key = "#id")
    public void update(int id, String name, String photoUrl, String description, List<String> genre) {
        var media = mediaRepository.findById(id);
        if (media == null){
            throw new MediaNotFoundException("Media with id:" + id + " not found");
        }
        List<Genre> genreList = new ArrayList<>();
        for (String g: genre){
            var genreFind = genreRepository.findByName(g);
            if (genreFind == null){
                throw new GenreNotFoundException("Genre with name: " + g + " not found");
            }
            genreList.add(genreFind);
        }

        media.setName(name);
        media.setPhotoUrl(photoUrl);
        media.setDescription(description);
        media.setGenreList(genreList);

        mediaRepository.save(media);
    }

    @Override
    @Transactional
    @CacheEvict(value = "MEDIA_PAGE", key = "#id")
    public void deleteById(int id) {
        var media = mediaRepository.findById(id);
        if (media == null){
            throw new MediaNotFoundException("Media with id: "+ id +" not found");
        }
        mediaRepository.deleteById(id);
    }
}
