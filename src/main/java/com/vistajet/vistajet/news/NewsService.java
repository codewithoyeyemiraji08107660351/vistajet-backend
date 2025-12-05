package com.vistajet.vistajet.news;

import com.vistajet.vistajet.exceptions.InvalidRequestException;
import com.vistajet.vistajet.exceptions.ResourceNotFoundException;
import com.vistajet.vistajet.file.NewsImageStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository repository;
    private final NewsImageStorage imageStorage;

    public void createNews(NewsRequest request, MultipartFile file) {

        String imageName = imageStorage.saveNewsPhoto(file);
            News news = News.builder()
                .title(request.getTitle())
                .content(request.getContent())
                    .author(request.getAuthor())
                    .preview(request.getPreview())
                .image(imageName)
                .build();

        repository.save(news);
    }

    public List<NewsResponse> getAllNews() {

        List<News> news = repository.findAll();
                return news
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public NewsResponse getANews(Integer id, String fullName) {

        News news;
        if (id != null) {
            news = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "News with ID " + id + " not found"));
        } else if (fullName != null && !fullName.isBlank()) {
            news = repository.findByTitleIgnoreCase(fullName)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "News with full name '" + fullName + "' not found"));
        } else {
            throw new InvalidRequestException("Please provide id or fullName");
        }

        return toResponse(news);
    }

    public void updateNews(Integer id, NewsRequest request, MultipartFile file) {
        News news = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot update News with ID " + id + " not found"));

       news.setTitle(request.getTitle());
       news.setContent(request.getContent());

        if (file != null && !file.isEmpty()) {
            String filename = imageStorage.saveNewsPhoto(file);
           news.setImage(filename);
        }

        repository.save(news);
    }

    @Transactional
    public void deleteNews(Integer id) {
        News news = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "News with ID " + id + " does not exist"
                ));

        String photoFileName = news.getImage();

        repository.delete(news);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                   imageStorage.deleteNewsPhoto(photoFileName);
                } catch (Exception e) {
                    log.error("Failed to delete file '{}' after leader delete commit", photoFileName, e);
                }
            }
        });
    }

    private NewsResponse toResponse(News news) {
        return NewsResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .author(news.getAuthor())
                .preview(news.getPreview())
                .imageUrl(imageStorage.loadNewsPhoto(news.getImage()))
                .createdAt(news.getCreatedAt())
                .build();
    }

}
