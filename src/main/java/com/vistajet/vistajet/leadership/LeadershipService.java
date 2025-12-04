package com.vistajet.vistajet.leadership;

import com.vistajet.vistajet.exceptions.InvalidRequestException;
import com.vistajet.vistajet.exceptions.ResourceNotFoundException;
import com.vistajet.vistajet.file.LeadersImageStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;


import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeadershipService {

    private final LeadershipRepository repository;
    private final LeadersImageStorageService fileStorageService;

    public void register(LeadershipRequest request, MultipartFile file) {

        String filename = fileStorageService.saveLeadershipPhoto(file);

        Leadership leadership = Leadership.builder()
                .fullName(request.getFullName())
                .position(request.getPosition())
                .bio(request.getBio())
                .photo(filename)
                .build();

        repository.save(leadership);
    }

    public List<LeadershipResponse> getAllLeaders() {

        List<Leadership> leaders = repository.findAll();

                return leaders
                .stream()
                .map(this::toResponse)
                .toList();

    }

    public LeadershipResponse getALeadership(Integer id, String fullName) {
        Leadership leader;

        if (id != null) {
            leader = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Leader with ID " + id + " not found"));
        } else if (fullName != null && !fullName.isBlank()) {
            leader = repository.findByFullNameIgnoreCase(fullName)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Leader with full name '" + fullName + "' not found"));
        } else {
            throw new InvalidRequestException("Please provide id or fullName");
        }
        return toResponse(leader);
    }

    public void update(Integer id, LeadershipRequest request, MultipartFile file) {

        Leadership leader = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot update Leader with ID " + id + " not found"));

        leader.setFullName(request.getFullName());
        leader.setPosition(request.getPosition());
        leader.setBio(request.getBio());

        if (file != null && !file.isEmpty()) {
            String filename = fileStorageService.saveLeadershipPhoto(file);
            leader.setPhoto(filename);
        }

        repository.save(leader);
    }

    @Transactional
    public void delete(Integer id) {

        Leadership leader = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Leader with ID " + id + " does not exist"
                ));

        String photoFileName = leader.getPhoto();

        repository.delete(leader);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    fileStorageService.deleteLeadershipPhoto(photoFileName);
                } catch (Exception e) {
                    log.error("Failed to delete file '{}' after leader delete commit", photoFileName, e);
                }
            }
        });
    }


    private LeadershipResponse toResponse(Leadership leader) {
        return LeadershipResponse.builder()
                .id(leader.getId())
                .fullName(leader.getFullName())
                .position(leader.getPosition())
                .bio(leader.getBio())
                .image(fileStorageService.loadLeadershipPhoto(leader.getPhoto()))
                .build();
    }
}

