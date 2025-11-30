package com.vistajet.vistajet.about;

import com.vistajet.vistajet.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AboutService {

    private final AboutRepository repository;

    public void createAbout(AboutRequest request) {
        About about = About.builder()
                .overview(request.getOverview())
                .mission(request.getMission())
                .vision(request.getVision())
                .corePillars(request.getCorePillars())
                .build();
        repository.save(about);

    }

    public List<AboutResponse> getAllAbout() {
        List<About> list = repository.findAll();

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No About record found");
        }

        return list.stream()
                .map(this::toResponse)
                .toList();
    }

    public void updateAbout(Integer id, AboutRequest request) {
        About about = repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("About with ID: "+ id +" not found"));

        about.setOverview(request.getOverview());
        about.setMission(request.getMission());
        about.setVision(request.getVision());
        about.setCorePillars(request.getCorePillars());
        repository.save(about);

    }

    public AboutResponse toResponse(About about){
        return AboutResponse.builder()
                .id(about.getId())
                .overview(about.getOverview())
                .mission(about.getMission())
                .vision(about.getVision())
                .corePillars(about.getCorePillars())
                .build();
    }

}
